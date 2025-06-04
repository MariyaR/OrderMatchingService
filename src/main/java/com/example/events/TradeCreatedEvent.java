package com.example.events;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class TradeCreatedEvent extends AbstractEvent{

  public TradeCreatedEvent(UUID tradeId, UUID buyOrderId, UUID sellOrderId, String tickerName,
                           BigDecimal price, Long quantity, UUID buyUserId, UUID sellUserId,
                           LocalDateTime buyOrderDate, LocalDateTime sellOrderDate) {
    super(tradeId, buyOrderId, sellOrderId, tickerName, price, quantity, buyUserId, sellUserId,
            buyOrderDate, sellOrderDate);
  }

  public TradeCreatedEvent(Trade trade, Order buyOrder, Order sellOrder) {
    super(trade.getTradeID(), trade.getBuyOrderId(), trade.getSellOrderId(), trade.getTickerName(),
      trade.getPrice(), trade.getQuantity(), trade.getBuyerId(), trade.getSellerId(),
      buyOrder.getCreatedAt(), sellOrder.getCreatedAt());
  }

}
