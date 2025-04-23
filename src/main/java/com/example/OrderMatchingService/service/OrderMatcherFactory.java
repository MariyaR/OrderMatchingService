package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderMatcherFactory {

  private final OrderBookManager orderBookManager;
  private final MatchingStrategy matchingStrategy;
  private final Map<String, OrderMatcher> matchers = new ConcurrentHashMap<>();

  public OrderMatcherFactory(OrderBookManager orderBookManager, MatchingStrategy matchingStrategy) {
    this.orderBookManager = orderBookManager;
    this.matchingStrategy = matchingStrategy;
  }

  public OrderMatcher get(String ticker) {
    return matchers.computeIfAbsent(ticker, t ->
         new OrderMatcher(t, orderBookManager, matchingStrategy));
  }
}
