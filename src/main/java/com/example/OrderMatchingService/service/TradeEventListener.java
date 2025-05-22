package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.events.TradeExecutedEvent;
import com.example.OrderMatchingService.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TradeEventListener {

  private final TradeExecutedHandler tradeExecutedHandler;

  public TradeEventListener(TradeExecutedHandler tradeExecutedHandler) {
    this.tradeExecutedHandler = tradeExecutedHandler;
  }

  @KafkaListener(topics = "${kafka.topics.trade-executed}")
    public void handleIncomingTrade(TradeExecutedEvent executedEvent) {
      tradeExecutedHandler.handleEvent(executedEvent);
    }
}
