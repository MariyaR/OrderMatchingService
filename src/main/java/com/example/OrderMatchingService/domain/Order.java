package com.example.OrderMatchingService.domain;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "trade_order")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private UUID orderID;

  @Column(name = "user_id", nullable = false)
  @NonNull
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_type", nullable = false)
  private OperationType operationType;

  @Column(name = "ticker_name", nullable = false)
  @NonNull
  private String tickerName;

  @Column(name = "quantity", nullable = false)
  @NonNull
  private Long quantity;

  @Column(name = "price", nullable = false)
  @NonNull
  private BigDecimal price;

  @Column(name = "created_at", nullable = false)
  @NonNull
  private LocalDateTime createdAt;

  @Column(name = "order_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public boolean isSellOrder() {
    return operationType == OperationType.SELL;
  }
  public boolean isBuyOrder() {
    return operationType == OperationType.BUY;
  }
  public void decreaseQuantity(Long delta) {
    quantity -= delta;
  }
}
