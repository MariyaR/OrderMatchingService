package com.example.OrderMatchingService.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderPlacedEvent implements DomainEvent {
    private final UUID orderId;
    private final String ticker;
    private final int quantity;
    private final long price;
    private final LocalDateTime occurredAt;
    public OrderPlacedEvent(UUID orderId, String ticker, int quantity, long price) {
        this.orderId = orderId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.occurredAt = LocalDateTime.now(); // Timestamp when event is created
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

}
