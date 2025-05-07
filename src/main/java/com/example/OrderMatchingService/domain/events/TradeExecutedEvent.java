package com.example.OrderMatchingService.domain.events;

import com.example.OrderMatchingService.domain.TradeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final BigDecimal price;
    private final Long quantity;
    private final UUID buyUserId;
    private final UUID sellUserId;
    private final LocalDateTime buyOrderDate;
    private final LocalDateTime sellOrderDate;

    private final LocalDateTime createdAt;

    private final TradeStatus status;
    private boolean rollbackApplied;


    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

    public TradeExecutedEvent(UUID tradeId, UUID buyOrderId, UUID sellOrderId, String tickerName, BigDecimal price,
                              Long quantity, UUID buyUserId, UUID sellUserId, LocalDateTime buyOrderDate, LocalDateTime sellOrderDate,
                              LocalDateTime createdAt, TradeStatus status, boolean rollbackApplied) {
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
