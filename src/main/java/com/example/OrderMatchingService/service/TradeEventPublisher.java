package com.example.OrderMatchingService.service;

import com.example.events.TradeCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradeEventPublisher {

    @Value("${kafka.topics.trade-created}")
    private String tradeCreatedTopic;

    private final KafkaTemplate<String, TradeCreatedEvent> kafkaTemplate;

    public TradeEventPublisher(KafkaTemplate<String, TradeCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTradeCreated(TradeCreatedEvent tradeEvent) {
        kafkaTemplate.send(tradeCreatedTopic, tradeEvent);
    }
}
