package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderBookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderBookService {

    private final OrderBookFactory orderBookFactory;
    @Autowired
    private final OrderBookMapper orderBookMapper;

    public OrderBookService(OrderBookFactory orderBookFactory, OrderBookMapper orderBookMapper) {
        this.orderBookFactory = orderBookFactory;
        this.orderBookMapper = orderBookMapper;
    }

    public OrderBookDTO getOrderBook() {
        List<Order> sellOrders =
                orderBookFactory.getOrderBooks().values()
                        .stream()
                        .flatMap(ob ->
                                ob.getSellBook()
                                        .values()
                                        .stream())
                        .flatMap(timeMap ->
                                timeMap.values()
                                        .stream())
                        .flatMap(List::stream)
                        .toList();
        List<Order> buyOrders =
                orderBookFactory.getOrderBooks().values()
                        .stream()
                        .flatMap(ob ->
                                ob.getBuyBook()
                                        .values()
                                        .stream())
                        .flatMap(timeMap -> timeMap.values().stream())
                        .flatMap(List::stream)
                        .toList();


        return orderBookMapper.toDTO(buyOrders, sellOrders);
    }
}
