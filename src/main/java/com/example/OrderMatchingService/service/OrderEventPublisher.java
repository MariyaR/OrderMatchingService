package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.events.OrderMatchedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

  @Value("${kafka.topics.order-created}")
  private String orderCreatedTopic;

  private final KafkaTemplate<String, OrderMatchedEvent> kafkaTemplate;

  public OrderEventPublisher(KafkaTemplate<String, OrderMatchedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishOrderMatchedEvent(OrderMatchedEvent orderMatchedEvent) {
    kafkaTemplate.send(orderCreatedTopic, orderMatchedEvent);
  }
}
