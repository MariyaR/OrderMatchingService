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
  @Column(name = "order_id")  // Specify the column name in the database (order_id)
  private UUID orderID;

  @Column(name = "user_id", nullable = false)  // Specify the column name for user_id
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_type", nullable = false)  // Specify the column name for operation_type
  private OperationType operationType;

  @Column(name = "ticker_name", nullable = false)  // Specify the column name for ticker_name
  private String tickerName;

  @Column(name = "quantity", nullable = false)  // Specify the column name for quantity
  private Integer quantity;

  @Column(name = "price", nullable = false)  // Specify the column name for price
  private Double price;

  @Column(name = "created_at", nullable = false)  // Specify the column name for created_at
  private Date createdAt;

}
