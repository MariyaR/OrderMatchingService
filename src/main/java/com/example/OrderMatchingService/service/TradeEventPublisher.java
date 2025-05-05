package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradeEventPublisher {

    private final String TRADE_TOPIC = "trade_event";

    private final KafkaTemplate<String, TradeCreatedEvent> kafkaTemplate;

    public TradeEventPublisher(KafkaTemplate<String, TradeCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTradeCreated(TradeCreatedEvent tradeEvent) {
        kafkaTemplate.send(TRADE_TOPIC, tradeEvent);
    }
}
