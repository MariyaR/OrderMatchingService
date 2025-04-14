package com.example.OrderMatchingService.service;


import com.example.OrderMatchingService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMatchingService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderPlacementService orderPlacementService;


}
