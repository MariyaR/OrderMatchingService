package com.example.OrderMatchingService.domain.events;

import com.example.OrderMatchingService.domain.TradeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor(force = true)
public class TradeExecutedEvent implements DomainEvent{

    private final UUID tradeExecutedEventId = UUID.randomUUID();
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

    private final TradeStatus status;
    private boolean rollbackApplied;


    @Override
    public Date createdAt() {
        return createdAt;
    }

    public TradeExecutedEvent(UUID tradeId, UUID buyOrderId, UUID sellOrderId, String tickerName, long price,
                              int quantity, UUID buyUserId, UUID sellUserId, Date buyOrderDate, Date sellOrderDate,
                              Date createdAt, TradeStatus status, boolean rollbackApplied) {
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
        this.createdAt = createdAt;
        this.status = status;
        this.rollbackApplied = rollbackApplied;
    }
}
