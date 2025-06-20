package com.example.OrderMatchingService.dto;


import com.example.OrderMatchingService.domain.OperationType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderDtoIn {
  private UUID userId;
  private OperationType operationType;
  private String tickerName;
  private Long quantity;
  private BigDecimal price;
  private LocalDateTime createdAt;
}
