package com.example.OrderMatchingService.domain.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredAt();
}
