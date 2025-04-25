package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.events.OrderMatchedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

  private final String ORDER_EVENT_TOPIC = "order_event";
  //private final String TEST_TOPIC = "test_topic";

  private final KafkaTemplate<String, OrderMatchedEvent> kafkaTemplate;

  public OrderEventPublisher(KafkaTemplate<String, OrderMatchedEvent> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

//  public void sendOrder(Order order) {
//    String message = order.toString();
//    kafkaTemplate.send(ORDER_EVENT_TOPIC, message);
//  }
//
//  public void sendMessage(String message) {
//    kafkaTemplate.send(TEST_TOPIC, message);
//  }


  public void publishOrderMathedEvent(OrderMatchedEvent orderMatchedEvent) {
    kafkaTemplate.send(ORDER_EVENT_TOPIC, orderMatchedEvent);
  }
}
