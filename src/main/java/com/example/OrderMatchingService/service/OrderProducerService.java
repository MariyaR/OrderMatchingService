package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

  private final String ORDER_TOPIC = "order";
  private final String TEST_TOPIC = "test_topic";

  private final KafkaTemplate<String, String> kafkaTemplate;

  public OrderProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(Order order) {
    String message = order.toString();
    kafkaTemplate.send(ORDER_TOPIC, message);
  }

  public void sendMessage(String message) {
    kafkaTemplate.send(TEST_TOPIC, message);
  }
}
