package com.example.OrderMatchingService.domain;

import jakarta.persistence.*;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
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
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_type", nullable = false)
  private OperationType operationType;

  @Column(name = "ticker_name", nullable = false)
  private String tickerName;

  @Column(name = "quantity", nullable = false)
  @NonNull
  private Integer quantity;

  @Column(name = "price", nullable = false)
  @NonNull
  private Long price;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;

  public boolean isSellOrder() {
    return operationType == OperationType.SELL;
  }
  public boolean isBuyOrder() {
    return operationType == OperationType.BUY;
  }

  public void increaseQuantity(int delta) {
    quantity += delta;
  }

  public void decreaseQuantity(int delta) {
    quantity -= delta;
  }
}
