package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderMatchingService {

  private OrderMatcherFactory orderMatcherFactory;


  public OrderMatchingService( OrderMatcherFactory orderMatcherFactory) {
    this.orderMatcherFactory = orderMatcherFactory;
  }

  List<Trade> match (Order order) {
    order.setStatus(OrderStatus.PENDING);
    OrderMatcher matcher = orderMatcherFactory.get(order.getTickerName());
    return matcher.match(order);
  }
}
