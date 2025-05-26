package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.*;
import com.example.OrderMatchingService.domain.events.TradeExecutedEvent;
import com.example.OrderMatchingService.domain.matching.PriceTimePriorityStrategy;
import com.example.OrderMatchingService.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRecoveryServiceTest {

  @Mock
  private OrderBookFactory orderBookFactory;

  @Mock
  private OrderMatcherFactory orderMatcherFactory;

  @Mock
  private OrderPlacementService orderPlacementService;

  @InjectMocks
  private OrderRecoveryService orderRecoveryService;

  private final String TICKER = "ticker";
  private UUID buyOrderId = UUID.randomUUID();
  private UUID sellOrderId = UUID.randomUUID();
  private Date now = new Date();
  private OrderBook orderBook = new OrderBook();
  private OrderMatcher orderMatcher = new OrderMatcher(TICKER, new PriceTimePriorityStrategy(), orderBook);

  private TradeExecutedEvent mockEvent;

  private Order createBuyOrder(Long quantity) {
    return new Order(buyOrderId, UUID.randomUUID(), OperationType.BUY, TICKER, quantity, new BigDecimal(100), LocalDateTime.now(), OrderStatus.CREATED);
  }

  // Helper method for creating a sell order
  private Order createSellOrder(Long quantity) {
    return new Order(sellOrderId, UUID.randomUUID(), OperationType.SELL, TICKER, quantity, new BigDecimal(100), LocalDateTime.now(), OrderStatus.CREATED);
  }

  private TradeExecutedEvent createTradeEvent(Order buyOrder, Order sellOrder) {
    return new TradeExecutedEvent(UUID.randomUUID(),
            buyOrder.getOrderID(),
            sellOrder.getOrderID(),
            TICKER,
            new BigDecimal(100),
            Math.min(buyOrder.getQuantity(), sellOrder.getQuantity()),
            buyOrder.getUserId(),
            sellOrder.getUserId(),
            buyOrder.getCreatedAt(),
            sellOrder.getCreatedAt(),
            TradeStatus.FAILED,
            TradeFailureReason.getEmptyFailureList()
    );
  }


  @BeforeEach
  void setUp() {
    orderMatcher = new OrderMatcher(TICKER, new PriceTimePriorityStrategy(), orderBook);
    lenient().when(orderBookFactory.getOrCreate(Mockito.eq(TICKER))).thenReturn(orderBook);
  }

  @Test
  void rollback_fullMatch_shouldRestoreOrders() {
    Order buyOrder = createBuyOrder(5L);
    Order sellOrder = createSellOrder(5L);
    mockEvent = createTradeEvent(buyOrder, sellOrder);

    orderMatcher.match(buyOrder);
    orderMatcher.match(sellOrder);

    // Assert orders are removed from the order book
    assertTrue(orderBook.getBuyBook().isEmpty());
    assertTrue(orderBook.getSellBook().isEmpty());

    // Rollback the event
    orderRecoveryService.rollback(mockEvent);

    // Assert orders are restored to active status
    assertEquals(5, buyOrder.getQuantity());
    assertEquals(5, sellOrder.getQuantity());
    assertEquals(OrderStatus.ACTIVE, buyOrder.getStatus());
    assertEquals(OrderStatus.ACTIVE, sellOrder.getStatus());
  }

  @Test
  void rollback_partialMatch_shouldRestoreRemainingOrders() {
    Order buyOrder = createBuyOrder(10L);
    Order sellOrder = createSellOrder(5L);
    mockEvent = createTradeEvent(buyOrder, sellOrder);

    orderMatcher.match(buyOrder);
    orderMatcher.match(sellOrder);

    // Assert the partial match status
    assertFalse(orderBook.getBuyBook().isEmpty());
    assertTrue(orderBook.getSellBook().isEmpty());

    // Rollback the event
    orderRecoveryService.rollback(mockEvent);

    // Assert buy order quantity is restored
    assertEquals(10, buyOrder.getQuantity());
    assertEquals(5, sellOrder.getQuantity());
    assertEquals(OrderStatus.ACTIVE, buyOrder.getStatus());
    assertEquals(OrderStatus.ACTIVE, sellOrder.getStatus());
  }

  @Test
  void rollback_orderAlreadyCancelled_shouldThrowException() {
    Order buyOrder = createBuyOrder(5L);
    buyOrder.setStatus(OrderStatus.CANCELLED);
    Order sellOrder = createSellOrder(5L);
    sellOrder.setStatus(OrderStatus.CANCELLED);

    mockEvent = createTradeEvent(buyOrder, sellOrder);

    // Trying to rollback a cancelled order should throw an exception
    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> orderRecoveryService.rollback(mockEvent),
            "Expected rollback() to throw an exception for cancelled orders"
    );
  }

  @Test
  void finalize_shouldRemoveFromReservedOrders() {
    Order buyOrder = createBuyOrder(10L);
    Order sellOrder = createSellOrder(10L);

    buyOrder.setStatus(OrderStatus.RESERVED);
    sellOrder.setStatus(OrderStatus.RESERVED);
    orderBook.reserveOrder(buyOrder);
    orderBook.reserveOrder(sellOrder);

    assertEquals(OrderStatus.RESERVED, buyOrder.getStatus());

    orderRecoveryService.finalize(createTradeEvent(buyOrder, sellOrder));

    assertEquals(OrderStatus.COMPLETED, buyOrder.getStatus());
    assertEquals(OrderStatus.COMPLETED, sellOrder.getStatus());
    assertNull(orderBook.getReservedOrder(buyOrderId));
    assertNull(orderBook.getReservedOrder(sellOrderId));
  }

  @Test
  void finalize_orderNotReserved_shouldThrowException() {
    // Create an order that is not reserved
    Order buyOrder = createBuyOrder(5L);
    buyOrder.setStatus(OrderStatus.ACTIVE);

    Order sellOrder = createSellOrder(10L);
    sellOrder.setStatus(OrderStatus.RESERVED);
    orderBook.reserveOrder(sellOrder);

    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> orderRecoveryService.finalize(createTradeEvent(buyOrder, sellOrder)),
            "Expected finalize() to throw an exception for non-RESERVED order"
    );
  }

  @Test
  void rollback_nullEvent_shouldThrowException() {
    // Null event should throw an exception
    assertThrows(IllegalArgumentException.class, () -> orderRecoveryService.rollback(null));
  }

  @Test
  void rollback_missingOrderId_shouldThrowException() {
    Order buyOrder = createBuyOrder(5L);
    Order sellOrder = createSellOrder(5L);
    // Simulate a missing order ID in the event
    mockEvent = new TradeExecutedEvent(
            UUID.randomUUID(),
            null,  // Missing order ID
            sellOrderId,
            TICKER,
            new BigDecimal(100),
            buyOrder.getQuantity(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            buyOrder.getCreatedAt(),
            sellOrder.getCreatedAt(),
            TradeStatus.CONFIRMED,
            TradeFailureReason.getEmptyFailureList()
    );

    assertThrows(IllegalArgumentException.class, () -> orderRecoveryService.rollback(mockEvent));
  }

  @Test
  void rollback_multipleTimes() {
    Order buyOrder = createBuyOrder(10L);
    Order sellOrder = createSellOrder(5L);

    // Create trade event for rollback
    mockEvent = createTradeEvent(buyOrder, sellOrder);

    // Match the orders (simulate order processing)
    orderMatcher.match(buyOrder);
    orderMatcher.match(sellOrder);

    // First rollback - should succeed and restore the original quantities
    orderRecoveryService.rollback(mockEvent);
    assertEquals(10, buyOrder.getQuantity(), "After first rollback, buy order quantity should be restored.");
    assertEquals(5, sellOrder.getQuantity(), "After first rollback, sell order quantity should be restored.");

    // Try second rollback - should throw an exception since rollback is already applied
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> orderRecoveryService.rollback(mockEvent),
            "Expected rollback() to throw an exception when rollback is applied more than once."
    );

    // Verify the exception message to ensure it's the correct failure
    assertEquals("Rollback has already been applied to this event. Reapplying rollback is not allowed.",
            exception.getMessage(),
            "The exception message should indicate that the rollback cannot be applied more than once.");
  }
}
