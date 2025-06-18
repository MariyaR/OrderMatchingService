package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.events.TradeExecutedEvent;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderRecoveryService {

  private final OrderBookFactory orderBookFactory;
  private final OrderPlacementService orderPlacementService;

  private static final Set<OrderStatus> ROLLBACK_ELIGIBLE_STATUSES = EnumSet.of(
          OrderStatus.RESERVED,
          OrderStatus.ACTIVE
  );

  public OrderRecoveryService(OrderBookFactory orderBookFactory, OrderPlacementService orderPlacementService) {
    this.orderBookFactory = orderBookFactory;
    this.orderPlacementService = orderPlacementService;
  }

  public void rollback(TradeExecutedEvent event) {
    checkValid(event);

    OrderBook orderBook = orderBookFactory.getOrCreate(event.getTickerName());

    UUID buyId = event.getBuyOrderId();
    UUID sellId = event.getSellOrderId();

    Optional<Order> buyOrderOpt = orderBook.getReservedOrder(buyId);

    if (buyOrderOpt.isEmpty()) {
      buyOrderOpt = Optional.ofNullable(orderBook.getBuyBook().get(event.getPrice()))
              .flatMap(dateMap -> Optional.ofNullable(dateMap.get(event.getBuyOrderDate())))
              .flatMap(list -> list.stream()
                      .filter(order -> order.getOrderID().equals(buyId))
                      .findFirst());
    }

    Optional<Order> sellOrderOpt = orderBook.getReservedOrder(sellId);

    if (sellOrderOpt.isEmpty()) {
      sellOrderOpt = Optional.ofNullable(orderBook.getSellBook().get(event.getPrice()))
              .flatMap(dateMap -> Optional.ofNullable(dateMap.get(event.getSellOrderDate())))
              .flatMap(list -> list.stream()
                      .filter(order -> order.getOrderID().equals(sellId))
                      .findFirst());
    }

    Order buyOrder = buyOrderOpt.orElseThrow(() ->
            new IllegalStateException("Buy order not found for rollback: " + buyId));
    Order sellOrder = sellOrderOpt.orElseThrow(() ->
            new IllegalStateException("Sell order not found for rollback: " + sellId));

    rollBack(buyOrder, event, orderBook);
    rollBack(sellOrder, event, orderBook);
    event.setRollbackApplied(true);
  }

  public void finalize(TradeExecutedEvent event) {
    OrderBook orderBook = orderBookFactory.getOrCreate(event.getTickerName());

    finalizeOrder(orderBook, event.getBuyOrderId());
    finalizeOrder(orderBook, event.getSellOrderId());
  }

  private void finalizeOrder(OrderBook orderBook, UUID orderId) {
    orderBook.getReservedOrder(orderId)
      .map(this::validateReservedOrder)
      .ifPresent(order -> completeOrder(orderBook, order));
  }

  private Order validateReservedOrder(Order order) {
    if (order.getStatus() != OrderStatus.RESERVED) {
      throw new IllegalStateException("Cannot finalize order: not in RESERVED state");
    }
    return order;
  }


  private void completeOrder(OrderBook orderBook, Order order) {
    orderBook.removeFromReserved(order);
    order.setStatus(OrderStatus.COMPLETED);
    orderPlacementService.save(order);
  }


  private void rollBack(Order order, TradeExecutedEvent event, OrderBook orderBook) {
    if (!ROLLBACK_ELIGIBLE_STATUSES.contains(order.getStatus())) {
      throw new IllegalStateException("Cannot roll-back order: not in RESERVED or READY FOR MATCHING state");
    }

    order.setQuantity(order.getQuantity() + event.getQuantity());
    order.setStatus(OrderStatus.ACTIVE);
    orderBook.addOrder(order);
  }

  private void checkValid(TradeExecutedEvent event) {
    // Check if the event is null
    if (event == null) {
      throw new IllegalArgumentException("The event is null. A valid TradeExecutedEvent must be provided.");
    }

    // Check if the buyOrderId is null
    if (event.getBuyOrderId() == null) {
      throw new IllegalArgumentException("The event has a null buyOrderId. A valid buyOrderId is required.");
    }

    // Check if the sellOrderId is null
    if (event.getSellOrderId() == null) {
      throw new IllegalArgumentException("The event has a null sellOrderId. A valid sellOrderId is required.");
    }

    // Check if rollback has already been applied to this event
    if (event.isRollbackApplied()) {
      throw new IllegalArgumentException("Rollback has already been applied to this event. Reapplying rollback is not allowed.");
    }
  }
}
