package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.OperationType;
import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.repository.OrderRepository;
import com.example.OrderMatchingService.service.OrderPlacementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class OrderPlacementServiceTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderPlacementService orderPlacementService;

    @Test
    void shouldSaveOrderInDatabase() {
        Order order = new Order(
                null, UUID.randomUUID(),
                OperationType.BUY, "ticker", 100, 200L,
                new Date(), OrderStatus.CREATED
        );

        orderPlacementService.placeOrder(order);

        Optional<Order> savedOrder = orderRepository.findById(order.getOrderID());

        assertTrue(savedOrder.isPresent());
        assertEquals(order.getOrderID(), savedOrder.get().getOrderID());
    }
}
