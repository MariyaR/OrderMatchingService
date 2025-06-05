package com.example.OrderMatchingService.domain.matching;

import com.example.OrderMatchingService.domain.*;
import com.example.events.OrderMatchedEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class PriceTimePriorityStrategy implements MatchingStrategy{

  @Override
    public List<OrderMatchedEvent> match(Order order, OrderBook orderBook) {
        List<OrderMatchedEvent> tradeEvents = new ArrayList<>();

        while (canMatch(order, orderBook)) {
            BigDecimal bestPrice = orderBook.getBestPrice(order);
            ConcurrentSkipListMap<LocalDateTime, List<Order>> priceLevelQueue = orderBook.getPriceLevel(order);
            Order match = priceLevelQueue.firstEntry().getValue().get(0);
            Long tradedQty = Math.min(order.getQuantity(), match.getQuantity());

            order.decreaseQuantity(tradedQty);
            match.decreaseQuantity(tradedQty);

            Order buyOrder = order.isBuyOrder() ? order : match;
            Order sellOrder = order.isSellOrder() ? order : match;

            OrderMatchedEvent orderMatchedEvent = new OrderMatchedEvent(buyOrder.getOrderID(), sellOrder.getOrderID(),
              buyOrder.getTickerName(), bestPrice, tradedQty, buyOrder.getUserId(), sellOrder.getUserId(),
              buyOrder.getCreatedAt(), sellOrder.getCreatedAt());
            tradeEvents.add(orderMatchedEvent);

            if (match.getQuantity() == 0) {
                match.setStatus(OrderStatus.RESERVED);
                orderBook.reserveOrder(match);
                if (priceLevelQueue.isEmpty()) orderBook.getBook(order).remove(bestPrice);
            }
        }

        return tradeEvents;
    }


    private boolean canMatch(Order order, OrderBook fifoOrderBook) {


      if (order.getQuantity() <= 0 || fifoOrderBook.getBook(order).isEmpty()) {
            return false;
        }

        BigDecimal bestPrice = fifoOrderBook.getBestPrice(order);

        boolean isBuyOrderTooLow = order.isBuyOrder() && bestPrice.compareTo(order.getPrice()) > 0;
        boolean isSellOrderTooHigh = order.isSellOrder() && bestPrice.compareTo(order.getPrice()) < 0;

        return !(isBuyOrderTooLow || isSellOrderTooHigh);
    }
}
