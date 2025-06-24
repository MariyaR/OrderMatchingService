package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderBookDTO;
import com.example.OrderMatchingService.dto.OrderDtoOut;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBookMapper {

    public static OrderBookDTO toDTO(List<Order> buyOrders, List<Order> sellOrders) {
        OrderBookDTO dto = new OrderBookDTO();
        dto.setBuyOrders(buyOrders.stream().map(OrderBookMapper::toOrderDTO).toList());
        dto.setSellOrders(sellOrders.stream().map(OrderBookMapper::toOrderDTO).toList());
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

