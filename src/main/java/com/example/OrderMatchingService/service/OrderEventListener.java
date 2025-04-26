package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.dto.OrderDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderEventListener {

  private final OrderProcessingService orderProcessingService;

  public OrderEventListener(OrderProcessingService orderProcessingService) {
    this.orderProcessingService = orderProcessingService;
  }

  @KafkaListener(topics = "order-event")
  public void handleIncomingOrder(OrderDto orderDto) {
    orderProcessingService.process(orderDto);
  }


}
