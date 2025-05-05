package com.example.OrderMatchingService.domain.matching;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;

import java.util.List;

public interface MatchingStrategy {

    List<TradeCreatedEvent> match(Order incomingOrder,
                                  OrderBook orderBook);
}
