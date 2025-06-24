package com.example.OrderMatchingService.controller;


import com.example.OrderMatchingService.dto.OrderDtoIn;
import com.example.OrderMatchingService.service.OrderProcessingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/placeOrder")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderPlaceController {

  private OrderProcessingService orderProcessingService;

  public OrderPlaceController(OrderProcessingService orderProcessingService) {
    this.orderProcessingService = orderProcessingService;
  }

  @PostMapping
  public void placeOrder(@RequestBody OrderDtoIn orderDtoIn) {

    orderProcessingService.process(orderDtoIn);
  }

}
