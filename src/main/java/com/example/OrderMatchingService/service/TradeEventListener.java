package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.events.TradeExecutedEvent;
import com.example.OrderMatchingService.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TradeEventListener {

    @KafkaListener(topics = "${kafka.topics.trade-executed}")
    public void handleIncomingOrder(TradeExecutedEvent executedEvent) {
    }


}
