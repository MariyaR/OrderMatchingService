package com.example.OrderMatchingService.domain;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;


public class OrderBook {

  private final ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> buyBook = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
  private final ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> sellBook = new ConcurrentSkipListMap<>();
  private final Map <UUID, Order> reservdedOrders = new ConcurrentHashMap<>();

  public ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> getBook(Order order) {
    return order.isBuyOrder() ? sellBook : buyBook;
  }

  public Long getBestPrice (Order order) {
    return order.isBuyOrder() ? sellBook.firstKey() : buyBook.firstKey();
  }

  public ConcurrentSkipListMap<Date, List<Order>> getPriceLevel(Order order) {
    Long bestPrice = getBestPrice(order);
    return order.isBuyOrder() ? sellBook.get(bestPrice) : buyBook.get(bestPrice);
  }

  public void reserveOrder(Order order) {
    ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> book = order.isSellOrder() ? sellBook : buyBook;
    ConcurrentSkipListMap<Date, List<Order>> priceLevel = book.get(order.getPrice());
    List <Order> orderLevel = priceLevel.get(order.getCreatedAt());
    orderLevel.remove(order);
    if (orderLevel.isEmpty()) {
      priceLevel.remove(order.getCreatedAt());
    }
    if (priceLevel.isEmpty()) {
      book.remove(order.getPrice());
    }

    reservdedOrders.computeIfAbsent(order.getOrderID(), k -> order);
  }

  public void addOrder(Order order) {
    ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> targetBook = order.isBuyOrder() ? buyBook : sellBook;

    ConcurrentSkipListMap<Date, List<Order>> timeMap =
      targetBook.computeIfAbsent(order.getPrice(), k -> new ConcurrentSkipListMap<>());

    List<Order> ordersAtTime =
      timeMap.computeIfAbsent(order.getCreatedAt(), k -> new ArrayList<>());

    ordersAtTime.add(order);
  }

  public void removeFromReserved(Order order) {
    reservdedOrders.remove(order.getOrderID());
  }

  public Order getReservedOrder(UUID orderId) {
    return reservdedOrders.get(orderId);
  }

  public ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> getBuyBook() {
    return buyBook;
  }

  public ConcurrentSkipListMap<Long, ConcurrentSkipListMap<Date, List<Order>>> getSellBook() {
    return sellBook;
  }
}
