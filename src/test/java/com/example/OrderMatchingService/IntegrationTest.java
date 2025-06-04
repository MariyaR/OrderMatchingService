package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.OperationType;
import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderDto;
import com.example.OrderMatchingService.repository.OrderRepository;
import com.example.OrderMatchingService.service.OrderProcessingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

  @Value("${test.buyOrder.account}")
  private String buyOrderAccount;

  @Value("${test.sellOrder.account}")
  private String sellOrderAccount;


  @BeforeEach
  public void setUp() {
     buyOrder = OrderDto.builder()
      .userId(UUID.fromString(buyOrderAccount))
      .operationType(OperationType.BUY)
      .tickerName(TICKER)
      .quantity(5L)
      .price(new BigDecimal("100"))
      .createdAt(LocalDateTime.now())
      .build();

     sellOrder = OrderDto.builder()
      .userId(UUID.fromString(sellOrderAccount))
      .operationType(OperationType.SELL)
      .tickerName(TICKER)
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
