package com.example.OrderMatchingService.service;

import com.example.events.OrderMatchedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderMatchedPublisher {

    @Value("${kafka.topics.trade-created}")
    private String tradeCreatedTopic;

    private final KafkaTemplate<String, OrderMatchedEvent> kafkaTemplate;

    public OrderMatchedPublisher(KafkaTemplate<String, OrderMatchedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderMatched(OrderMatchedEvent tradeEvent) {
        kafkaTemplate.send(tradeCreatedTopic, tradeEvent);
    }
}
