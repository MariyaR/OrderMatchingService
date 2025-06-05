package com.example.events;

import com.example.OrderMatchingService.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class OrderMatchedEvent extends AbstractEvent{

  public OrderMatchedEvent(UUID buyOrderId, UUID sellOrderId, String tickerName,
                           BigDecimal price, Long quantity, UUID buyUserId, UUID sellUserId,
                           LocalDateTime buyOrderDate, LocalDateTime sellOrderDate) {
    super( buyOrderId, sellOrderId, tickerName, price, quantity, buyUserId, sellUserId,
            buyOrderDate, sellOrderDate);
  }

}
