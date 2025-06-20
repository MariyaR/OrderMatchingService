package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderBook;
import com.example.OrderMatchingService.dto.OrderBookDTO;
import com.example.OrderMatchingService.dto.OrderDtoOut;
import com.example.OrderMatchingService.dto.PriceLevelDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Component
public class OrderBookMapper {

    public static OrderBookDTO toDTO(List<Order> buyOrders, List<Order> sellOrders) {
        OrderBookDTO dto = new OrderBookDTO();
        dto.setBuyOrders(buyOrders.stream().map(OrderBookMapper::toOrderDTO).toList());
        dto.setSellOders(sellOrders.stream().map(OrderBookMapper::toOrderDTO).toList());
        return dto;
    }

    private static OrderDtoOut toOrderDTO(Order order) {
        OrderDtoOut dto = new OrderDtoOut();
        dto.setOrderId(order.getOrderID());
        dto.setTickerName(order.getTickerName());
        dto.setPrice(order.getPrice());
        dto.setQuantity(order.getQuantity()); // assuming this exists
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}

