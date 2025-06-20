package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.OrderStatus;
import com.example.OrderMatchingService.dto.OrderDtoIn;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  public Order mapToOrer (OrderDtoIn dto) {
    return Order.builder()
      .orderID(null)
      .userId(dto.getUserId())
      .operationType(dto.getOperationType())
      .tickerName(dto.getTickerName())
      .quantity(dto.getQuantity())
      .price(dto.getPrice())
      .createdAt(dto.getCreatedAt())
      .status(OrderStatus.ACTIVE)
      .build();
  }

  public OrderDtoIn mapToDto(Order order) {
    return OrderDtoIn.builder()
      .userId(order.getUserId())
      .operationType(order.getOperationType())
      .tickerName(order.getTickerName())
      .quantity(order.getQuantity())
      .price(order.getPrice())
      .createdAt(order.getCreatedAt())
      .build();
  }
}
