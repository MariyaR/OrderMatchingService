package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

  private final String ORDER_TOPIC = "order_event";
  private final String TEST_TOPIC = "test_topic";

  private final KafkaTemplate<String, String> kafkaTemplate;

  public OrderEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(Order order) {
    String message = order.toString();
    kafkaTemplate.send(ORDER_TOPIC, message);
  }

  public void sendMessage(String message) {
    kafkaTemplate.send(TEST_TOPIC, message);
  }


  public void publishOrderPlaced(Order newOrder) {
    kafkaTemplate.send(ORDER_TOPIC, newOrder.toString());
  }
}
