package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.domain.TradeStatus;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;

import java.util.List;
import java.util.stream.Collectors;

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
      .build();
  }

  public static List<Trade> fromListEvents (List<TradeCreatedEvent> events) {
    return events.stream().map(TradeEventMapper::fromEvent).collect(Collectors.toList());
  }
}
