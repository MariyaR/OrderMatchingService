package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import com.example.OrderMatchingService.domain.events.TradeExecutedEvent;
import com.example.OrderMatchingService.repository.OrderRepository;
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

    rollBack(buyOrder, trade, orderBook);
    rollBack(sellOrder, trade, orderBook);
    event.setRollbackApplied(true);
  }

  public void finalize(TradeExecutedEvent event) {
    OrderBook orderBook = orderBookFactory.getOrCreate(event.getTickerName());

    Order buyOrder = getReservedOrderOrThrow(orderBook, event.getBuyOrderId());
    Order sellOrder = getReservedOrderOrThrow(orderBook, event.getSellOrderId());

    completeOrder(orderBook, buyOrder);
    completeOrder(orderBook, sellOrder);
  }

  private Order getReservedOrderOrThrow(OrderBook orderBook, UUID orderId) {
    Order order = orderBook.getReservedOrder(orderId);
    if (order == null || order.getStatus() != OrderStatus.RESERVED) {
      throw new IllegalStateException("Cannot finalize order: not in RESERVED state");
    }
    return order;
  }

  private void completeOrder(OrderBook orderBook, Order order) {
    orderBook.removeFromReserved(order);
    order.setStatus(OrderStatus.COMPLETED);
    orderPlacementService.placeOrder(order);
  }


  private void rollBack(Order order, Trade trade, OrderBook orderBook) {
    if (!ROLLBACK_ELIGIBLE_STATUSES.contains(order.getStatus())) {
      throw new IllegalStateException("Cannot roll-back order: not in RESERVED or READY FOR MATCHING state");
    }

    order.setQuantity(order.getQuantity() + trade.getQuantity());
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
