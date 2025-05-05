package com.example.OrderMatchingService.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "trade")
@Getter
@Setter
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Trade {
  @Id
  @GeneratedValue
  @Column(name = "trade_id")
  private UUID tradeID;

  @Column(name = "buyer_id")
  @NonNull
  public final UUID buyerId;

  @Column(name = "seller_id")
  @NonNull
  public final UUID sellerId;

  @Column(name = "buy_order_id")
  @NonNull
  public final UUID buyOrderId;

  @Column(name = "sell_order_id")
  @NonNull
  public final UUID sellOrderId;

  @Column(name = "ticker_name", nullable = false)
  @NonNull
  private String tickerName;

  @Column(name = "price", nullable = false)
  @NonNull
  public final long price;

  @Column(name = "quantity", nullable = false)
  @NonNull
  public final int quantity;

  @Column(name = "created_at", nullable = false)
  @NonNull
  private Date createdAt;

  @Column(name = "trade_status", nullable = false)
  @Enumerated(EnumType.STRING)
  @NonNull
  private TradeStatus status;
}
