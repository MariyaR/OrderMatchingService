package com.example.OrderMatchingService.domain.matching;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentSkipListMap;

public interface MatchingStrategy {

    List<Trade> match(Order incomingOrder,
                      ConcurrentSkipListMap<Long, Queue<Order>> matchBook);
}
