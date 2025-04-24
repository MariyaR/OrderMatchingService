package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Trade;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradeEventPublisher {

    private final String TRADE_TOPIC = "trade_event";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TradeEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishTradeCreated(Trade trade) {
        kafkaTemplate.send(TRADE_TOPIC, trade.toString());
    }
}
