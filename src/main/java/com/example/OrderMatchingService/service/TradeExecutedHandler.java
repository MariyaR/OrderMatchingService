package com.example.OrderMatchingService.service;

import com.example.events.TradeStatus;
import com.example.events.TradeExecutedEvent;
import org.springframework.stereotype.Service;

@Service
public class TradeExecutedHandler {

  private final OrderRecoveryService orderRecoveryService;

  public TradeExecutedHandler(OrderRecoveryService orderRecoveryService) {
    this.orderRecoveryService = orderRecoveryService;
  }

  public void handleEvent (TradeExecutedEvent  event) {
    if (event.getStatus() == TradeStatus.CONFIRMED) {
      orderRecoveryService.finalize(event);
    } else {
      orderRecoveryService.rollback(event);
    }
  }
}
