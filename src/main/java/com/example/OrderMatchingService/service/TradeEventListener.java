package com.example.OrderMatchingService.service;

import com.example.events.TradeExecutedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TradeEventListener {

  private final TradeExecutedHandler tradeExecutedHandler;

  public TradeEventListener(TradeExecutedHandler tradeExecutedHandler) {
    this.tradeExecutedHandler = tradeExecutedHandler;
  }

  @KafkaListener(topics = "${kafka.topics.trade-executed}", groupId = "trade-executed-group")
    public void handleIncomingTrade(TradeExecutedEvent executedEvent) {
      tradeExecutedHandler.handleEvent(executedEvent);
    }
}
