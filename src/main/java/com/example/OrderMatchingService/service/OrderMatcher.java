package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.*;
import com.example.events.OrderMatchedEvent;
import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import com.example.events.TradeExecutedEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class OrderMatcher {

    private final String ticker;
    private final OrderBook orderBook;
    private final MatchingStrategy matchingStrategy;
    private final AtomicLong totalMatchedVolume = new AtomicLong();
    private final AtomicLong totalTradeCount = new AtomicLong();
    private final AtomicLong totalLatencyMicros = new AtomicLong();
    private final AtomicLong matchCount = new AtomicLong();


    public OrderMatcher(String ticker, MatchingStrategy matchingStrategy, OrderBook orderBook) {
        this.ticker = ticker;
        this.matchingStrategy = matchingStrategy;
        this.orderBook = orderBook;
    }

    public List<OrderMatchedEvent> match(Order order) {

        if (!order.getTickerName().equals(ticker)) {
            throw new IllegalArgumentException("wrong ticker name");
        }

        long startTime = System.nanoTime();

        order.setStatus(OrderStatus.ACTIVE);
        List<OrderMatchedEvent> tradeEvents;
        tradeEvents = matchingStrategy.match(order, orderBook);
        tradeEvents.forEach(this::process);

        //todo update order state in base
        if (order.getQuantity() > 0) {
          orderBook.addOrder(order);
        } else if (!tradeEvents.isEmpty()) {
            order.setStatus(OrderStatus.RESERVED);
            orderBook.reserveOrder(order);
        }

        long endTime = System.nanoTime(); // End time
        long latencyMicros = (endTime - startTime) / 1000; // Convert to microseconds
        recordLatency(latencyMicros);

        return tradeEvents;

    }

    private void process(OrderMatchedEvent event) {
        totalMatchedVolume.addAndGet(event.getQuantity());
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
