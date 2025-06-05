package com.example.OrderMatchingService.domain.matching;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.events.OrderMatchedEvent;

import java.util.List;

public interface MatchingStrategy {

    List<OrderMatchedEvent> match(Order incomingOrder,
                                  OrderBook orderBook);
}
