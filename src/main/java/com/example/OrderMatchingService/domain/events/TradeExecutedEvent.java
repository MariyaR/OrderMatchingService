package com.example.OrderMatchingService.domain.events;

import com.example.OrderMatchingService.domain.TradeFailureReason;
import com.example.OrderMatchingService.domain.TradeStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
@Builder
@AllArgsConstructor
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

    private TradeStatus status;
    private boolean rollbackApplied;

    private TradeFailureReason failureReason;


    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }
}
