package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

  private final OrderPlacementService orderPlacementService;

  public OrderEventListener(OrderPlacementService orderPlacementService) {
    this.orderPlacementService = orderPlacementService;
  }

  @KafkaListener(topics = "order-event", groupId = "my-group")
  public void handleIncomingOrder(OrderDto orderDto) {
    orderPlacementService.placeOrder(orderDto);
  }


}
