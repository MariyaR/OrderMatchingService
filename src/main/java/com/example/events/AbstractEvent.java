package com.example.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractEvent implements DomainEvent {

    private final UUID eventId = UUID.randomUUID();
    private final UUID buyOrderId;
    private final UUID sellOrderId;
    private final String tickerName;
    private final BigDecimal price;
    private final Long quantity;
    private final UUID buyUserId;
    private final UUID sellUserId;
    private final LocalDateTime buyOrderDate;
    private final LocalDateTime sellOrderDate;

    private final LocalDateTime createdAt = LocalDateTime.now();


    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

}
