package com.example.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private final LocalDateTime createdAt;

    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

}
