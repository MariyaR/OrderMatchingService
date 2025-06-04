package com.example.events;

import com.example.OrderMatchingService.domain.TradeFailureReason;
import com.example.OrderMatchingService.domain.TradeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(force = true)
public class TradeExecutedEvent extends AbstractEvent{

    private final TradeStatus status;
    private boolean rollbackApplied = false;
    private List<TradeFailureReason> failureReasons = TradeFailureReason.getEmptyFailureList();

    public TradeExecutedEvent(
            UUID tradeId,
            UUID buyOrderId,
            UUID sellOrderId,
            String tickerName,
            BigDecimal price,
            Long quantity,
            UUID buyUserId,
            UUID sellUserId,
            LocalDateTime buyOrderDate,
            LocalDateTime sellOrderDate,
            TradeStatus status,
            List<TradeFailureReason> failureReasons
    ) {
        super(tradeId, buyOrderId, sellOrderId, tickerName, price, quantity, buyUserId, sellUserId, buyOrderDate, sellOrderDate);
        this.status = status;
        this.failureReasons = failureReasons;
    }
}
