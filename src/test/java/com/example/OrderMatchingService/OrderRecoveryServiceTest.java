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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    lenient().when(orderBookFactory.getOrCreate(Mockito.eq(TICKER))).thenReturn(orderBook);
  }

  @Test
  void rollback_fullmatch_integrationTest() {
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
    assertEquals(buyOrder.getStatus(), OrderStatus.READY_FOR_MATCHING);
    assertEquals(sellOrder.getStatus(), OrderStatus.READY_FOR_MATCHING);
  }

  @Test
  void rollback_partiallMatch_integrationTest() {
    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER,
            10,  100L,  now, OrderStatus.CREATED);
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

    assertFalse(orderBook.getBuyBook().isEmpty());
    assertTrue(orderBook.getSellBook().isEmpty());

    assertEquals(buyOrder.getStatus(), OrderStatus.READY_FOR_MATCHING);
    assertEquals(sellOrder.getStatus(), OrderStatus.RESERVED);

    orderRecoveryService.rollback(mockEvent);

    assertEquals(10, buyOrder.getQuantity());
    assertEquals(5, sellOrder.getQuantity());

    assertEquals(buyOrder.getStatus(), OrderStatus.READY_FOR_MATCHING);
    assertEquals(sellOrder.getStatus(), OrderStatus.READY_FOR_MATCHING);
  }

  @Test
  void rollback_fullyMatched() {

    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER, 0, 100L, now, OrderStatus.CREATED);
    Order sellOrder = new Order(sellOrderId, UUID.randomUUID(), OperationType.SELL, TICKER, 0, 100L, now, OrderStatus.CREATED);

    //simulate full match
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

    orderRecoveryService.rollback(event);

    assertEquals(5, orderBook.getReservedOrder(buyOrderId).getQuantity());
    assertEquals(5, orderBook.getReservedOrder(sellOrderId).getQuantity());

    assertTrue(orderBook.getBuyBook().get(100L).get(buyOrder.getCreatedAt()).get(0).equals(buyOrder));
    assertTrue(orderBook.getSellBook().get(100L).get(buyOrder.getCreatedAt()).get(0).equals(sellOrder));

  }

  @Test
  void rollback_partiallyMatched() {

    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER, 10, 100L, now, OrderStatus.CREATED);
    Order sellOrder = new Order(sellOrderId, UUID.randomUUID(), OperationType.SELL, TICKER, 10, 100L, now, OrderStatus.CREATED);

    //simulate partially match
    orderBook.addOrder(buyOrder);
    orderBook.addOrder(sellOrder);

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

    orderRecoveryService.rollback(event);

    assertEquals(15, buyOrder.getQuantity());
    assertEquals(15, sellOrder.getQuantity());

    assertTrue(orderBook.getBuyBook().get(100L).get(buyOrder.getCreatedAt()).get(0).equals(buyOrder));
    assertTrue(orderBook.getSellBook().get(100L).get(buyOrder.getCreatedAt()).get(0).equals(sellOrder));

  }

  @Test
  void finalize_shouldRemoveFromReservedOrders() {
    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER, 10, 100L, now, OrderStatus.RESERVED);
    orderBook.reserveOrder(buyOrder);

    assertEquals(buyOrder.getStatus(), OrderStatus.RESERVED);

    orderRecoveryService.finalize(buyOrder);

    assertEquals(buyOrder.getStatus(), OrderStatus.COMPLETED);
    assertNull(orderBook.getReservedOrder(buyOrderId));

  }

  @Test
  void finalize_orderNotReserved_shouldThrowException() {
    // Create an order in "READY_FOR_MATCHING" state (not RESERVED)
    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER,
            5,  100L,  now, OrderStatus.READY_FOR_MATCHING);

    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> orderRecoveryService.finalize(buyOrder),
            "Expected finalize() to throw an exception for non-RESERVED order"
    );
  }

  @Test
  void rollback_orderAlreadyCancelled_throwsException() {
    Order buyOrder = new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER,
            5,  100L,  now, OrderStatus.CANCELLED);
    Order sellOrder = new Order(sellOrderId, UUID.randomUUID(), OperationType.SELL, TICKER,
            5,  100L,  now, OrderStatus.CANCELLED);

    // Mock the event for rollback
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

    // Attempting to roll back a cancelled order
    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> orderRecoveryService.rollback(mockEvent)
    );
  }
}
