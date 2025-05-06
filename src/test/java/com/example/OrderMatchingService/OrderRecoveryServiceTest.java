package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.OperationType;
import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import com.example.OrderMatchingService.domain.matching.PriceTimePriorityStrategy;
import com.example.OrderMatchingService.service.OrderBookFactory;
import com.example.OrderMatchingService.service.OrderMatcher;
import com.example.OrderMatchingService.service.OrderMatcherFactory;
import com.example.OrderMatchingService.service.OrderRecoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRecoveryServiceTest {

  @Mock
  private OrderBookFactory orderBookFactory;

  @Mock
  private OrderMatcherFactory orderMatcherFactory;

  @InjectMocks
  private OrderRecoveryService orderRecoveryService;

  private final String TICKER = "ticker";
  private UUID buyOrderId = UUID.randomUUID();
  private UUID sellOrderId = UUID.randomUUID();
  private Date now = new Date();
  private OrderBook orderBook = new OrderBook();
  private OrderMatcher orderMatcher = new OrderMatcher(TICKER, new PriceTimePriorityStrategy(), orderBook);

  private TradeCreatedEvent mockEvent;

  @BeforeEach
  void setUp() {
    when(orderBookFactory.getOrCreate(Mockito.eq(TICKER))).thenReturn(orderBook);


  }

  @Test
  void rollback_integrationTest() {
    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER,
      5,  100L,  now, OrderStatus.CREATED);
    Order sellOrder = new Order(sellOrderId, UUID.randomUUID(), OperationType.SELL, TICKER,
      5,  100L,  now, OrderStatus.CREATED);

    mockEvent = new TradeCreatedEvent(
      UUID.randomUUID(),
      buyOrderId,
      sellOrderId,
      TICKER,
      100L,
      5,
      UUID.randomUUID(),
      UUID.randomUUID(),
      buyOrder.getCreatedAt(),
      sellOrder.getCreatedAt()
    );

    orderMatcher.match(buyOrder);
    orderMatcher.match(sellOrder);

    assertTrue(orderBook.getBuyBook().isEmpty());
    assertTrue(orderBook.getSellBook().isEmpty());

    orderRecoveryService.rollback(mockEvent);

    assertEquals(5, buyOrder.getQuantity());
    assertEquals(5, sellOrder.getQuantity());
  }

  @Test
  void rollback_shouldRestoreOrderQuantities_andReinsertToOrderBook() {
    // given
    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER, 0, 100L, now, OrderStatus.CREATED);
    Order sellOrder = new Order(sellOrderId, UUID.randomUUID(), OperationType.SELL, TICKER, 0, 100L, now, OrderStatus.CREATED);

    // simulate matched (thus reserved) orders
    orderBook.reserveOrder(buyOrder);
    orderBook.reserveOrder(sellOrder);

    TradeCreatedEvent event = new TradeCreatedEvent(
      UUID.randomUUID(),
      buyOrderId,
      sellOrderId,
      TICKER,
      100L,
      5,
      buyOrder.getUserId(),
      sellOrder.getUserId(),
      buyOrder.getCreatedAt(),
      sellOrder.getCreatedAt()
    );

    // when
    orderRecoveryService.rollback(event);

    // then
    assertEquals(5, orderBook.getReservedOrder(buyOrderId).getQuantity());
    assertEquals(5, orderBook.getReservedOrder(sellOrderId).getQuantity());

    // Optionally also check re-added to main book if applicable

    assertTrue(orderBook.getBuyBook().get(100L).get(buyOrder.getCreatedAt()).get(0).equals(buyOrder));
    assertTrue(orderBook.getSellBook().get(100L).get(buyOrder.getCreatedAt()).get(0).equals(sellOrder));

  }

//  @Test
//  void finalize_shouldRemoveFromReservedOrders() {
//    Order order = new Order(buyOrderId, 10, "TICKER", 100L, OperationType.BUY, now);
//
//    orderRecoveryService.finalize(order);
//
//    verify(orderBookFactory).getOrCreate("TICKER");
//    verify(orderBook).removeFromReserved(order);
//  }

}
