package com.example.OrderMatchingService.domain;

import com.example.OrderMatchingService.service.TradeFailureReasonListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "trade")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
  @Id
  @GeneratedValue
  @Column(name = "trade_id")
  private UUID tradeID;

  @Column(name = "buyer_id")
  @NonNull
  public UUID buyerId;

  @Column(name = "seller_id")
  @NonNull
  public UUID sellerId;

  @Column(name = "buy_order_id")
  @NonNull
  public UUID buyOrderId;

  @Column(name = "sell_order_id")
  @NonNull
  public UUID sellOrderId;

  @Column(name = "ticker_name", nullable = false)
  @NonNull
  private String tickerName;

  @Column(name = "price", nullable = false)
  @NonNull
  public BigDecimal price;

  @Column(name = "quantity", nullable = false)
  @NonNull
  public Long quantity;

  @Column(name = "created_at", nullable = false)
  @NonNull
  private LocalDateTime createdAt;

  @Column(name = "trade_status", nullable = false)
  @Enumerated(EnumType.STRING)
  @NonNull
  private TradeStatus status;

  @Convert(converter = TradeFailureReasonListConverter.class)
  @Column(name = "failure_reasons")
  private List<TradeFailureReason> failureReasons;

}
