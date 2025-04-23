package com.example.OrderMatchingService.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class TradeCreatedEvent implements DomainEvent{
    private final UUID tradeId;
    private final UUID buyOrderId;
    private final UUID sellOrderId;
    private final long price;
    private final int quantity;

    private final LocalDateTime occurredAt;

    public TradeCreatedEvent(UUID tradeId, UUID buyOrderId, UUID sellOrderId, long price, int quantity, LocalDateTime occurredAt) {
        this.tradeId = tradeId;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
        this.occurredAt = occurredAt;
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }
}
