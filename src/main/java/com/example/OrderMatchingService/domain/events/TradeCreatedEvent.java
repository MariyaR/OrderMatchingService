package com.example.OrderMatchingService.domain.events;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class TradeCreatedEvent implements DomainEvent{

    private final UUID tradeCreatedEventId = UUID.randomUUID();
    private final UUID tradeId;
    private final UUID buyOrderId;
    private final UUID sellOrderId;
    private final String tickerName;
    private final BigDecimal price;
    private final Long quantity;
    private final UUID buyUserId;
    private final UUID sellUserId;
    private final LocalDateTime buyOrderDate;
    private final LocalDateTime sellOrderDate;

    private final LocalDateTime createdAt;


  @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

  public TradeCreatedEvent(Trade trade, Order buyOrder, Order sellOrder) {

    this(trade.getTradeID(), trade.getBuyOrderId(), trade.getSellOrderId(), trade.getTickerName(),
      trade.getPrice(), trade.getQuantity(), trade.getBuyerId(), trade.getSellerId(),
      buyOrder.getCreatedAt(), sellOrder.getCreatedAt());
  }

  public TradeCreatedEvent(UUID tradeId, UUID buyOrderId, UUID sellOrderId, String tickerName, BigDecimal price, Long quantity, UUID buyUserId, UUID sellUserId, LocalDateTime buyOrderDate, LocalDateTime sellOrderDate) {
    this.tradeId = tradeId;
    this.buyOrderId = buyOrderId;
    this.sellOrderId = sellOrderId;
    this.tickerName = tickerName;
    this.price = price;
    this.quantity = quantity;
    this.buyUserId = buyUserId;
    this.sellUserId = sellUserId;
    this.buyOrderDate = buyOrderDate;
    this.sellOrderDate = sellOrderDate;
    this.createdAt = LocalDateTime.now();
  }
}
