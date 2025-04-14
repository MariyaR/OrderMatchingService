package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderDto;
import com.example.OrderMatchingService.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderPlacementService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderProducerService orderProducerService;

  public OrderPlacementService(OrderRepository orderRepository, OrderMapper orderMapper, OrderProducerService orderProducerService) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.orderProducerService = orderProducerService;
  }

  public boolean isValid(OrderDto orderDto) {
    return orderDto.getQuantity() > 0 && orderDto.getPrice().doubleValue() > 0;
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

  public void placeOrder(OrderDto orderDto) {
    if (!isValid(orderDto)) {
      throw new IllegalArgumentException("Invalid order");
    }

    var order = orderMapper.mapToOrer(orderDto);
    save(order);
    orderProducerService.sendMessage(order);
  }
}
