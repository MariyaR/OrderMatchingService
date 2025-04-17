package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMatcher {

  private final OrderBookManager ordeBookManager;

  public OrderMatcher(OrderBookManager orderBookManager) {
    this.ordeBookManager = orderBookManager;
  }

  public void match(Order order) {
    if (!tryMatch(order)) {
      ordeBookManager.add(order);
    };

  }

  private boolean tryMatch(Order order) {

    return false;
  }

  //send event for execution service
  private void processMatch(Order order) {

  }
}
