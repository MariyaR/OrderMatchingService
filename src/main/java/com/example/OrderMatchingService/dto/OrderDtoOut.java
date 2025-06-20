package com.example.OrderMatchingService.dto;

import com.example.OrderMatchingService.domain.OperationType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
public class OrderDtoOut {
    private UUID orderId;
    private BigDecimal price;
    private String tickerName;
    private Long quantity;
    private LocalDateTime createdAt;
}
