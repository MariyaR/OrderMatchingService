package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.events.OrderMatchedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumerService {

  private OrderMatchedEvent event;
  @KafkaListener(topics = "${kafka.topics.order-created}", groupId = "my-group")
  public void listen(OrderMatchedEvent event) {
    this.event = event;
  }

  public OrderMatchedEvent getMessage() {
    return event;
  }

}

