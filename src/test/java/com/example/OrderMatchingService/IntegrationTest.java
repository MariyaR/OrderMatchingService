package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.OperationType;
import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.events.TradeCreatedEvent;
import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import com.example.OrderMatchingService.domain.matching.PriceTimePriorityStrategy;
import com.example.OrderMatchingService.dto.OrderDto;
import com.example.OrderMatchingService.repository.OrderRepository;
import com.example.OrderMatchingService.service.OrderBookFactory;
import com.example.OrderMatchingService.service.OrderMatcher;
import com.example.OrderMatchingService.service.OrderProcessingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IntegrationTest {

  private static final String TICKER = "ticker";
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderProcessingService orderProcessingService;

  private OrderDto buyOrder;
  private OrderDto sellOrder;


  @BeforeEach
  public void setUp() {
     buyOrder = OrderDto.builder()
      .userId(UUID.fromString("3f6c2c26-56f3-4e75-9016-f1879e2cbd5d"))
      .operationType(OperationType.BUY)
      .tickerName("ticker")
      .quantity(5L)
      .price(new BigDecimal("100"))
      .createdAt(LocalDateTime.now())
      .build();

     sellOrder = OrderDto.builder()
      .userId(UUID.fromString("e8b1b4ce-2ef8-4e2a-9f6f-df4ab6e3bcb4"))
      .operationType(OperationType.SELL)
      .tickerName("ticker")
      .quantity(5L)
      .price(new BigDecimal("100"))
      .createdAt(LocalDateTime.now())
      .build();


  }

  @AfterEach
  void tearDown() {

  }

  @Test
  void givenBuyOrder_whenMatchesSellOrder_thenTradeIsCreated() {
      orderProcessingService.process(buyOrder);
      orderProcessingService.process(sellOrder);

    List<Order> savedOrders = orderRepository.findAll();

    assertFalse(savedOrders.isEmpty());

  }

}
