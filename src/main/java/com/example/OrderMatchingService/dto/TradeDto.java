package com.example.OrderMatchingService.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TradeDto {
  public final UUID buyerId;
  public final UUID sellerId;
  public final long price;
  public final int quantity;
  public final long timestamp;
}
