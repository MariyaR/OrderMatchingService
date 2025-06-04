package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.domain.Order;
import com.example.events.TradeCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMatchingService {

  private OrderMatcherFactory orderMatcherFactory;


  public OrderMatchingService( OrderMatcherFactory orderMatcherFactory) {
    this.orderMatcherFactory = orderMatcherFactory;
  }

  List<TradeCreatedEvent> match (Order order) {
    OrderMatcher matcher = orderMatcherFactory.get(order.getTickerName());
    return matcher.match(order);
  }
}
