package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.matching.MatchingStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;


public class OrderMatcher {

    private final String ticker;
    private final ConcurrentSkipListMap<Long, Queue<Order>> buyBook = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    private final ConcurrentSkipListMap<Long, Queue<Order>> sellBook = new ConcurrentSkipListMap<>();
    private final MatchingStrategy matchingStrategy;
    private final AtomicLong totalMatchedVolume = new AtomicLong();
    private final AtomicLong totalTradeCount = new AtomicLong();
    private final AtomicLong totalLatencyMicros = new AtomicLong();
    private final AtomicLong matchCount = new AtomicLong();

    private final OrderBookManager orderBookManager;

    public OrderMatcher(String ticker, OrderBookManager orderBookManager, MatchingStrategy matchingStrategy) {
        this.ticker = ticker;
        this.orderBookManager = orderBookManager;
        this.matchingStrategy = matchingStrategy;
    }

    public List<Trade> match(Order order) {

        if (!order.getTickerName().equals(ticker)) {
            throw new IllegalArgumentException("wrong ticker name");
        }

        long startTime = System.nanoTime();
        List<Trade> pendingTrades;
        ConcurrentSkipListMap<Long, Queue<Order>> matchBook = order.isBuyOrder() ? sellBook : buyBook;
        pendingTrades = matchingStrategy.match(order, matchBook);
        pendingTrades.forEach(this::process);

        if (order.getQuantity() > 0) {
            ConcurrentSkipListMap<Long, Queue<Order>> targetBook = order.isBuyOrder() ? buyBook : sellBook;
            targetBook.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
        }

        long endTime = System.nanoTime(); // End timer
        long latencyMicros = (endTime - startTime) / 1000; // Convert to microseconds
        recordLatency(latencyMicros);

        return pendingTrades;

    }

    private void process(Trade trade) {
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
