package com.example.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime createdAt();
}
