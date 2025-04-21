package com.example.OrderMatchingService.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderMatcherFactory {

  private final OrderBookManager orderBookManager;
  private final Map<String, OrderMatcher> matchers = new ConcurrentHashMap();

  public OrderMatcherFactory(OrderBookManager orderBookManager) {
    this.orderBookManager = orderBookManager;
  }

  public OrderMatcher get(String ticker) {
    return matchers.computeIfAbsent(ticker, t -> {
      OrderMatcher matcher = new OrderMatcher(t, orderBookManager);
      //matcher.start();
      return matcher;
    });
  }
}
