package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.OperationType;
import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderDtoIn;
import com.example.OrderMatchingService.repository.OrderRepository;
import com.example.OrderMatchingService.service.OrderProcessingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@TestPropertySource(properties = {
//  "spring.datasource.url=jdbc:postgresql://localhost:5432/trade_db",
//  "spring.datasource.username=adminTraider",
//  "spring.datasource.password=adminTraider",
//  "spring.jpa.properties.hibernate.default_schema=order_matching",
//  "spring.jpa.hibernate.ddl-auto=update",
//  "spring.datasource.driver-class-name=org.postgresql.Driver",
//  "spring.flyway.schemas=order_matching"
//})
public class IntegrationTest {

//  private static final String TICKER = "ticker";
//  @Autowired
//  private OrderRepository orderRepository;
//
//  @Autowired
//  private OrderProcessingService orderProcessingService;
//
//  private OrderDtoIn buyOrder;
//  private OrderDtoIn sellOrder;
//
//  @Value("${test.buyOrder.account}")
//  private String buyOrderAccount;
//
//  @Value("${test.sellOrder.account}")
//  private String sellOrderAccount;
//
//
//  @BeforeEach
//  public void setUp() {
//     buyOrder = OrderDtoIn.builder()
//      .userId(UUID.fromString(buyOrderAccount))
//      .operationType(OperationType.BUY)
//      .tickerName(TICKER)
//      .quantity(5L)
//      .price(new BigDecimal("100"))
//      .createdAt(LocalDateTime.now())
//      .build();
//
//     sellOrder = OrderDtoIn.builder()
//      .userId(UUID.fromString(sellOrderAccount))
//      .operationType(OperationType.SELL)
//      .tickerName(TICKER)
//      .quantity(5L)
//      .price(new BigDecimal("100"))
//      .createdAt(LocalDateTime.now())
//      .build();
//
//
//  }
//
//  @AfterEach
//  void tearDown() {
//
//  }
//
//  @Test
//  void givenBuyOrder_whenMatchesSellOrder_thenTradeIsCreated() {
//      orderProcessingService.process(buyOrder);
//      orderProcessingService.process(sellOrder);
//    await()
//      .atMost(5, TimeUnit.SECONDS);
//      //.until(() -> orderRepository.findAll().size() == 2);
//
//
//    List<Order> savedOrders = orderRepository.findAll();
//
//    await()
//      .atMost(5, TimeUnit.SECONDS)
//      .until(() -> orderRepository.findAll().size() == 2);
//
//    assertFalse(savedOrders.isEmpty());
//
//  }

}
