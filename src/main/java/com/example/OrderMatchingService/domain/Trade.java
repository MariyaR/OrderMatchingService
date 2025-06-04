package com.example.OrderMatchingService.domain;

import com.example.OrderMatchingService.service.TradeFailureReasonListConverter;
import com.example.events.TradeExecutedEvent;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
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

  @Builder
  public Trade(UUID tradeID,
                UUID buyerId,
                UUID sellerId,
                UUID buyOrderId,
                UUID sellOrderId,
                String tickerName,
                Long quantity,
                BigDecimal price,
                LocalDateTime createdAt,
                TradeStatus status,
                List<TradeFailureReason> failureReasons) {
    this.tradeID = tradeID;
    this.buyerId = buyerId;
    this.sellerId = sellerId;
    this.buyOrderId = buyOrderId;
    this.sellOrderId = sellOrderId;
    this.tickerName = tickerName;
    this.quantity = quantity;
    this.price = price;
    this.createdAt = createdAt;
    this.status = status;
    this.failureReasons = failureReasons;
  }

  // ✅ Factory method for new trade creation (ID will be null)
  public static Trade createNew(UUID buyerId, UUID sellerId, UUID buyOrderId,
                                UUID sellOrderId, String tickerName, long quantity,
                                BigDecimal price) {
    return new Trade(null, buyerId, sellerId, buyOrderId, sellOrderId,
      tickerName, quantity, price, LocalDateTime.now(), TradeStatus.CREATED, TradeFailureReason.getEmptyFailureList());
  }

}
