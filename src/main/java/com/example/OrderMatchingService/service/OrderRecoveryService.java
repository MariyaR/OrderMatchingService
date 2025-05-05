package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderRecoveryService {

  private final OrderBookFactory orderBookFactory;

  public OrderRecoveryService(OrderBookFactory orderBookFactory) {
    this.orderBookFactory = orderBookFactory;
  }

  public void rollback(TradeCreatedEvent event) {
    OrderBook orderBook = orderBookFactory.getOrCreate(event.getTickerName());

    UUID buyId = event.getBuyOrderId();
    UUID sellId = event.getSellOrderId();
    Trade trade = TradeEventMapper.fromEvent(event);

    Order buyOrder = orderBook.getReservedOrder(buyId);
    Order sellOrder = orderBook.getReservedOrder(sellId);

    if (buyOrder == null) {
      buyOrder = orderBook.getBuyBook().get(trade.getPrice()).get(event.getBuyOrderDate())
        .stream()
        .filter(order -> order.getOrderID().equals(buyId)).findFirst().get();
    }

    if (sellOrder == null) {
      sellOrder = orderBook.getSellBook().get(trade.getPrice()).get(event.getSellOrderDate())
        .stream()
        .filter(order -> order.getOrderID().equals(sellId)).findFirst().get();
    }

    if (buyOrder!= null) {
      rollBack(buyOrder, trade, orderBook);
    }

    if (sellOrder!= null) {
      rollBack(sellOrder, trade, orderBook);
    }
  }

  public void finalize(Order order) {
    OrderBook orderBook = orderBookFactory.getOrCreate(order.getTickerName());
    orderBook.removeFromReserved(order);
  }

  private void rollBack(Order order, Trade trade, OrderBook orderBook) {
    order.setQuantity(order.getQuantity() + trade.getQuantity());
    orderBook.addOrder(order);
  }
}
