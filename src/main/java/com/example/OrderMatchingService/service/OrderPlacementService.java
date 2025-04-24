package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.dto.OrderDto;
import com.example.OrderMatchingService.repository.OrderRepository;
import com.example.OrderMatchingService.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderPlacementService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  public OrderPlacementService(OrderRepository orderRepository, OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  public boolean isValid(Order order) {
    return order.getQuantity() > 0 && order.getPrice().doubleValue() > 0;
  }

  public Order save(Order order) {
    return orderRepository.save(order);
  }

  public Optional<Order> findById(UUID orderId) {
    return orderRepository.findById(orderId);
  }

  public void deleteById(UUID orderId) {
    orderRepository.deleteById(orderId);
  }

  public void placeOrder(Order order) {
    if (!isValid(order)) {
      throw new IllegalArgumentException("Invalid order");
    }
    save(order);
  }
}
