package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderRecoveryService {

  private final OrderBookFactory orderBookFactory;

  private static final Set<OrderStatus> ROLLBACK_ELIGIBLE_STATUSES = EnumSet.of(
          OrderStatus.RESERVED,
          OrderStatus.READY_FOR_MATCHING
  );

  public OrderRecoveryService(OrderBookFactory orderBookFactory) {
    this.orderBookFactory = orderBookFactory;
  }

  public void rollback(TradeCreatedEvent event) {
    OrderBook orderBook = orderBookFactory.getOrCreate(event.getTickerName());

    UUID buyId = event.getBuyOrderId();
    UUID sellId = event.getSellOrderId();
    Trade trade = TradeEventMapper.fromEvent(event);

    Optional<Order> buyOrderOpt = Optional.ofNullable(orderBook.getReservedOrder(buyId));

    if (buyOrderOpt.isEmpty()) {
      buyOrderOpt = Optional.ofNullable(
              Optional.ofNullable(orderBook.getBuyBook().get(trade.getPrice()))
                      .flatMap(dateMap -> Optional.ofNullable(dateMap.get(event.getBuyOrderDate())))
                      .flatMap(list -> list.stream()
                              .filter(order -> order.getOrderID().equals(buyId))
                              .findFirst())
                      .orElse(null)
      );
    }

    Optional<Order> sellOrderOpt = Optional.ofNullable(orderBook.getReservedOrder(sellId));

    if (sellOrderOpt.isEmpty()) {
      sellOrderOpt = Optional.ofNullable(
              Optional.ofNullable(orderBook.getSellBook().get(trade.getPrice()))
                      .flatMap(dateMap -> Optional.ofNullable(dateMap.get(event.getSellOrderDate())))
                      .flatMap(list -> list.stream()
                              .filter(order -> order.getOrderID().equals(sellId))
                              .findFirst())
                      .orElse(null)
      );
    }

    Order buyOrder = buyOrderOpt.orElseThrow(() ->
            new IllegalStateException("Buy order not found for rollback: " + buyId));
    Order sellOrder = sellOrderOpt.orElseThrow(() ->
            new IllegalStateException("Sell order not found for rollback: " + sellId));

    buyOrderOpt.ifPresent(order -> rollBack(order, trade, orderBook));
    sellOrderOpt.ifPresent(order -> rollBack(order, trade, orderBook));
  }

  public void finalize(Order order) {

    if (order.getStatus() != OrderStatus.RESERVED) {
      throw new IllegalStateException("Cannot finalize order: not in RESERVED state");
    }

    OrderBook orderBook = orderBookFactory.getOrCreate(order.getTickerName());
    orderBook.removeFromReserved(order);
    order.setStatus(OrderStatus.COMPLETED);
  }

  private void rollBack(Order order, Trade trade, OrderBook orderBook) {
    if (!ROLLBACK_ELIGIBLE_STATUSES.contains(order.getStatus())) {
      throw new IllegalStateException("Cannot roll-back order: not in RESERVED or READY FOR MATCHING state");
    }

    order.setQuantity(order.getQuantity() + trade.getQuantity());
    order.setStatus(OrderStatus.READY_FOR_MATCHING);
    orderBook.addOrder(order);
  }
}
