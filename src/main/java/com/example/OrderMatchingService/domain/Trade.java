package com.example.OrderMatchingService.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class Trade {
  public final UUID buyerId;
  public final UUID sellerId;
  public final long price;
  public final int quantity;
  public final long timestamp;
}
