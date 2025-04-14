package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

  private final String KAFKA_TOPIC = "order";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(Order order) {
    String message = order.toString();
    kafkaTemplate.send(KAFKA_TOPIC, message);
  }
}
