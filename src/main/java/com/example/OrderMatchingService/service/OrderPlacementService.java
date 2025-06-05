package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderPlacementService {

  private final OrderRepository orderRepository;

  public OrderPlacementService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
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
