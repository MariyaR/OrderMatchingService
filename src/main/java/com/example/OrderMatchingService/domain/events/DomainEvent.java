package com.example.OrderMatchingService.domain.events;

import java.time.LocalDateTime;
import java.util.Date;

public interface DomainEvent {
    Date occurredAt();
}
