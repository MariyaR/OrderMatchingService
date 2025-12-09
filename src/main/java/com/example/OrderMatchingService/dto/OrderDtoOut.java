package com.example.OrderMatchingService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
public class OrderDtoOut {
    private BigDecimal price;
    private String tickerName;
    private Long quantity;
    private LocalDateTime createdAt;
    private String keycloakUsername;
}
