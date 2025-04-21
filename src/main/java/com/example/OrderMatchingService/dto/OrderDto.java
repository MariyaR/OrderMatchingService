package com.example.OrderMatchingService.dto;


import com.example.OrderMatchingService.domain.OperationType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class OrderDto {
  private UUID userId;
  private OperationType operationType;
  private String tickerName;
  private int quantity;
  private Long price;
  private Date createdAt;
}
