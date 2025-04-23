package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducerService {

  private final String ORDER_TOPIC = "order";
  private final String TEST_TOPIC = "test_topic";
  private final String TRADE_TOPIC = "trade_topic";

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

  public void sendTrade(Trade trade) {
    kafkaTemplate.send(TRADE_TOPIC, trade.toString());
  }
}
