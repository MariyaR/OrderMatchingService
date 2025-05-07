package com.example.OrderMatchingService.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;


public class OrderBook {

  private final ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> buyBook = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
  private final ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> sellBook = new ConcurrentSkipListMap<>();
  private final Map <UUID, Order> reservedOrders = new ConcurrentHashMap<>();

  public ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> getBook(Order order) {
    return order.isBuyOrder() ? sellBook : buyBook;
  }

  public BigDecimal getBestPrice (Order order) {
    return order.isBuyOrder() ? sellBook.firstKey() : buyBook.firstKey();
  }

  public ConcurrentSkipListMap<LocalDateTime, List<Order>> getPriceLevel(Order order) {
    BigDecimal bestPrice = getBestPrice(order);
    return order.isBuyOrder() ? sellBook.get(bestPrice) : buyBook.get(bestPrice);
  }

  public void reserveOrder(Order order) {
    ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> book =
      order.isSellOrder() ? sellBook : buyBook;

    Optional.ofNullable(book.get(order.getPrice()))
      .ifPresent(priceLevel -> {
        Optional.ofNullable(priceLevel.get(order.getCreatedAt()))
          .ifPresent(orderLevel -> {
            orderLevel.remove(order);
            if (orderLevel.isEmpty()) {
              priceLevel.remove(order.getCreatedAt());
            }
          });
        if (priceLevel.isEmpty()) {
          book.remove(order.getPrice());
        }
      });

    order.setStatus(OrderStatus.RESERVED);
    reservedOrders.computeIfAbsent(order.getOrderID(), k -> order);
  }

  public void addOrder(Order order) {
    order.setStatus(OrderStatus.ACTIVE);

    ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> targetBook = order.isBuyOrder() ? buyBook : sellBook;

    ConcurrentSkipListMap<LocalDateTime, List<Order>> timeMap =
      targetBook.computeIfAbsent(order.getPrice(), k -> new ConcurrentSkipListMap<>());

    List<Order> ordersAtTime =
      timeMap.computeIfAbsent(order.getCreatedAt(), k -> new ArrayList<>());

    ordersAtTime.add(order);
  }

  public void removeFromReserved(Order order) {
    reservedOrders.remove(order.getOrderID());
  }

  public Order getReservedOrder(UUID orderId) {
    return reservedOrders.get(orderId);
  }

  public ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> getBuyBook() {
    return buyBook;
  }

  public ConcurrentSkipListMap<BigDecimal, ConcurrentSkipListMap<LocalDateTime, List<Order>>> getSellBook() {
    return sellBook;
  }
}
