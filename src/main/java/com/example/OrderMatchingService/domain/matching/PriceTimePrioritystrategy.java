package com.example.OrderMatchingService.domain.matching;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.TradeStatus;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class PriceTimePrioritystrategy implements MatchingStrategy{
    @Override
    public List<Trade> match(Order order, ConcurrentSkipListMap<Long, Queue<Order>> matchBook) {
        List<Trade> trades = new ArrayList<>();

        while (canMatch(order, matchBook)) {
            Long bestPrice = matchBook.firstKey();
            Queue<Order> priceLevelQueue = matchBook.get(bestPrice);
            Order match = priceLevelQueue.peek();
            int tradedQty = Math.min(order.getQuantity(), match.getQuantity());

            order.decreaseQuantity(tradedQty);
            match.decreaseQuantity(tradedQty);

            Order buyOrder = order.isBuyOrder() ? order : match;
            Order sellOrder = order.isSellOrder() ? order : match;

            Trade trade = new Trade(UUID.randomUUID(), buyOrder.getUserId(), sellOrder.getUserId(), order.getTickerName(),
                    bestPrice, tradedQty, new Date(), TradeStatus.PENDING);

            trades.add(trade);

            if (match.getQuantity() == 0) {
                match.setStatus(OrderStatus.FULLY_MATCHED);
                priceLevelQueue.poll();
                if (priceLevelQueue.isEmpty()) matchBook.remove(bestPrice);
            } else {
                match.setStatus(OrderStatus.PARTIALLY_MATCHED);
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
}
