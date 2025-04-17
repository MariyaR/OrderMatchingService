package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

  private final OrderMatcher orderMatcher;

  public OrderEventListener(OrderMatcher orderMatcher) {this.orderMatcher = orderMatcher;}

  @KafkaListener(topics = "order-event", groupId = "my-group")
  public void listen(Order order) {
    orderMatcher.match(order);
    System.out.println("Received message: ");
  }


}
