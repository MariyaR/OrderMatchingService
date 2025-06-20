package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.events.OrderMatchedEvent;
import com.example.OrderMatchingService.dto.OrderDtoIn;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessingService {

    private final OrderMapper orderMapper;
    private final OrderPlacementService orderPlacementService;
    private final OrderMatchingService orderMatchingService;
    private final OrderMatchedPublisher orderMatchedPublisher;

    public OrderProcessingService(
            OrderMapper orderMapper,
            OrderPlacementService orderPlacementService,
            OrderMatchingService orderMatchingService,
            OrderMatchedPublisher tradeEventPublisher
    ) {
        this.orderMapper = orderMapper;
        this.orderPlacementService = orderPlacementService;
        this.orderMatchingService = orderMatchingService;
        this.orderMatchedPublisher = tradeEventPublisher;
    }

    public void process(OrderDtoIn orderDtoIn) {
        Order newOrder = orderMapper.mapToOrer(orderDtoIn);
        orderPlacementService.placeOrder(newOrder);

        List<OrderMatchedEvent> orderMatchedEvents = orderMatchingService.match(newOrder);
        for (OrderMatchedEvent event : orderMatchedEvents) {
            orderMatchedPublisher.publishOrderMatched(event);
        }
    }
}
