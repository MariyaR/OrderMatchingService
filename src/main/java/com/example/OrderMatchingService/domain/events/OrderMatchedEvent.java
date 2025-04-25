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

public class OrderMatchedEvent implements DomainEvent {

    private UUID matchId;
    private UUID buyOrderId;
    private UUID sellOrderId;
    private final String ticker;
    private final int quantity;
    private final long price;
    private final Date occurredAt;

    @Override
    public Date occurredAt() {
        return occurredAt;
    }

}
