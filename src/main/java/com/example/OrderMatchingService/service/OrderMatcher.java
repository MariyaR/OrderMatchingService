package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;


public class OrderMatcher {

  private final String ticker;

  private final ConcurrentSkipListMap<Long, Queue<Order>> buyBook = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
  private final ConcurrentSkipListMap<Long, Queue<Order>> sellBook = new ConcurrentSkipListMap<>();

  private final AtomicLong totalMatchedVolume = new AtomicLong();
  private final AtomicLong totalTradeCount = new AtomicLong();

  private final AtomicLong totalLatencyMicros = new AtomicLong();
  private final AtomicLong matchCount = new AtomicLong();

  private final OrderBookManager orderBookManager;

  public OrderMatcher(String ticker, OrderBookManager orderBookManager) {
    this.ticker = ticker;
    this.orderBookManager = orderBookManager;
  }

  public List<Trade> match(Order order) {
    long startTime = System.nanoTime();

    List<Trade> pendingTrades;

    ConcurrentSkipListMap<Long, Queue<Order>> matchBook = order.isBuyOrder() ? sellBook : buyBook;

    pendingTrades = doMatch(order, matchBook);

    if (order.getQuantity() > 0) {
      ConcurrentSkipListMap<Long, Queue<Order>> targetBook = order.isBuyOrder() ? buyBook : sellBook;
      targetBook.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
    }

    long endTime = System.nanoTime(); // End timer
    long latencyMicros = (endTime - startTime) / 1000; // Convert to microseconds

    recordLatency(latencyMicros);

    return pendingTrades;

  }

  private List<Trade> doMatch(Order order, ConcurrentSkipListMap<Long, Queue<Order>> matchBook) {
    List<Trade> trades = new ArrayList<>();

    while (canMatch(order, matchBook)) {

      Long bestPrice = matchBook.firstKey();
      Queue<Order> priceLevelQueue = matchBook.get(bestPrice);
      Order match = priceLevelQueue.peek();
      int tradedQty = Math.min(order.getQuantity(), match.getQuantity());

      Order buyOrder = order.isBuyOrder() ? order : match;
      Order sellOrder = order.isSellOrder() ? order : match;

      buyOrder.increaseQuantity(tradedQty);
      sellOrder.decreaseQuantity(tradedQty);

      Trade trade = new Trade(
        buyOrder.getUserId(),
        sellOrder.getUserId(),
        bestPrice, tradedQty, System.currentTimeMillis()
      );

      processMatch(trade);
      trades.add(trade);

      if (match.getQuantity() == 0) {
        priceLevelQueue.poll();
        if (priceLevelQueue.isEmpty()) matchBook.remove(bestPrice);
      }
    }
    return trades;
  }

  private boolean canMatch(Order order, ConcurrentSkipListMap<Long, Queue<Order>> matchBook) {
    if (order.getQuantity() <= 0 || matchBook.isEmpty()) {
      return false;
    }

    Long bestPrice = matchBook.firstKey();

    boolean isBuyOrderTooLow = order.isBuyOrder() && bestPrice > order.getPrice();
    boolean isSellOrderTooHigh = order.isSellOrder() && bestPrice < order.getPrice();

    return !(isBuyOrderTooLow || isSellOrderTooHigh);
  }

  //send event for trade execution service
  private void processMatch(Trade trade) {
    totalMatchedVolume.addAndGet(trade.getQuantity());
    totalTradeCount.incrementAndGet();
  }

  private void recordLatency(long latencyMicros) {
    totalLatencyMicros.addAndGet(latencyMicros);
    matchCount.incrementAndGet();
  }

  public long getAverageLatencyMicros() {
    long count = matchCount.get();
    return count == 0 ? 0 : totalLatencyMicros.get() / count;
  }
}
