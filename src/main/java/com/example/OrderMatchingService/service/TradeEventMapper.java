package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.*;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import com.example.OrderMatchingService.domain.events.TradeExecutedEvent;

import java.util.ArrayList;
import java.util.Collections;

public class TradeEventMapper {

  public static Trade fromEvent(TradeCreatedEvent event) {
    return Trade.builder()
      .tradeID(event.getTradeId())
      .buyerId(event.getBuyUserId())
      .sellerId(event.getSellUserId())
      .buyOrderId(event.getBuyOrderId())
      .sellOrderId(event.getSellOrderId())
      .tickerName(event.getTickerName()) // or set properly if included in the event
      .price(event.getPrice())
      .quantity(event.getQuantity())
      .createdAt(event.getCreatedAt())
      .status(TradeStatus.PENDING)
      .failureReasons(new ArrayList<>(
              Collections.singletonList(TradeFailureReason.EMPTY_FAILURE_REASON)
      ))
      .build();
  }

  public static Trade fromEvent(TradeExecutedEvent event) {
    return Trade.builder()
            .tradeID(event.getTradeId())
            .buyerId(event.getBuyUserId())
            .sellerId(event.getSellUserId())
            .buyOrderId(event.getBuyOrderId())
            .sellOrderId(event.getSellOrderId())
            .tickerName(event.getTickerName()) // or set properly if included in the event
            .price(event.getPrice())
            .quantity(event.getQuantity())
            .createdAt(event.getCreatedAt())
            .status(event.getStatus())
            .failureReasons(event.getFailureReasons())
            .build();
  }

}
