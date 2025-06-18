package com.example.OrderMatchingService.controller;


import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderDto;
import com.example.OrderMatchingService.service.OrderPlacementService;
import com.example.OrderMatchingService.service.OrderProcessingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/placeOrder")
public class OrderPlaceController {

  private OrderProcessingService orderProcessingService;

  public OrderPlaceController(OrderProcessingService orderProcessingService) {
    this.orderProcessingService = orderProcessingService;
  }

  @PostMapping
  public void placeOrder(@RequestBody OrderDto orderDto) {
    orderProcessingService.process(orderDto);
  }

}
