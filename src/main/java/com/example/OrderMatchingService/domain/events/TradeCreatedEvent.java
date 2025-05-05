package com.example.OrderMatchingService.domain.events;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.Trade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private final long price;
    private final int quantity;
    private final UUID buyUserId;
    private final UUID sellUserId;
    private final Date buyOrderDate;
    private final Date sellOrderDate;

    private final Date createdAt;


  @Override
    public Date createdAt() {
        return createdAt;
    }

  public TradeCreatedEvent(Trade trade, Order buyOrder, Order sellOrder) {

    this(trade.getTradeID(), trade.getBuyOrderId(), trade.getSellOrderId(), trade.getTickerName(),
      trade.getPrice(), trade.getQuantity(), trade.getBuyerId(), trade.getSellerId(),
      buyOrder.getCreatedAt(), sellOrder.getCreatedAt());
  }

  public TradeCreatedEvent(UUID tradeId, UUID buyOrderId, UUID sellOrderId, String tickerName, long price, int quantity, UUID buyUserId, UUID sellUserId, Date buyOrderDate, Date sellOrderDate) {
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
    this.createdAt = new Date();
  }
}
