package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProcessingService {

    private final OrderMapper orderMapper;
    private final OrderPlacementService orderPlacementService;
    private final OrderMatchingService orderMatchingService;
    private final TradePlacementService tradePlacementService;
    private final OrderEventPublisher orderEventPublisher;
    private final TradeEventPublisher tradeEventPublisher;

    public OrderProcessingService(
            OrderMapper orderMapper,
            OrderPlacementService orderPlacementService,
            OrderMatchingService orderMatchingService,
            TradePlacementService tradePlacementService,
            OrderEventPublisher orderEventPublisher,
            TradeEventPublisher tradeEventPublisher
    ) {
        this.orderMapper = orderMapper;
        this.orderPlacementService = orderPlacementService;
        this.orderMatchingService = orderMatchingService;
        this.tradePlacementService = tradePlacementService;
        this.orderEventPublisher = orderEventPublisher;
        this.tradeEventPublisher = tradeEventPublisher;
    }

    public void process(OrderDto orderDto) {
        Order newOrder = orderMapper.mapToOrer(orderDto);
        orderPlacementService.placeOrder(newOrder);
        //orderEventPublisher.publishOrderPlaced(newOrder);

        List<Trade> trades = orderMatchingService.match(newOrder);
        for (Trade trade : trades) {
            tradePlacementService.placeTrade(trade);
            tradeEventPublisher.publishTradeCreated(trade);
        }
    }
}
