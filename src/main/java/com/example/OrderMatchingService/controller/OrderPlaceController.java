package com.example.OrderMatchingService.controller;


import com.example.OrderMatchingService.dto.OrderDto;
import com.example.OrderMatchingService.service.OrderPlacementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/placeOrder")
public class OrderPlaceController {

  private OrderPlacementService orderPlacementService;

  public OrderPlaceController(OrderPlacementService orderPlacementService) {
    this.orderPlacementService = orderPlacementService;
  }

  @PostMapping
  public void placeOrder(@PathVariable OrderDto orderDto) {
    orderPlacementService.placeOrder(orderDto);
  }

}
