package com.example.OrderMatchingService.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TradeCreatedEvent implements DomainEvent{

    private UUID tradeCreatedEventId;
    private final UUID tradeId;
    private final UUID buyOrderId;
    private final UUID sellOrderId;
    private final long price;
    private final int quantity;

    private final Date occurredAt;


    @Override
    public Date occurredAt() {
        return occurredAt;
    }
}
