package com.example.OrderMatchingService.domain.matching;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

            Trade trade = new Trade( buyOrder.getUserId(), sellOrder.getUserId(), bestPrice, tradedQty,
                    System.currentTimeMillis());

           // process(trade);
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
}
