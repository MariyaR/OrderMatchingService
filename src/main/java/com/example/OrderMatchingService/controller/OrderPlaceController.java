package com.example.OrderMatchingService.controller;


import com.example.OrderMatchingService.dto.OrderDtoIn;
import com.example.OrderMatchingService.service.OrderProcessingService;
import com.example.OrderMatchingService.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/placeOrder")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderPlaceController {

  private OrderProcessingService orderProcessingService;

  private UserService userService;

  public OrderPlaceController(OrderProcessingService orderProcessingService, UserService userService) {
    this.orderProcessingService = orderProcessingService;
    this.userService = userService;
  }

  //@PreAuthorize("hasRole('admin')")
  //@Authoperation(Authprocessor.class) //Aspectj
  @PostMapping
  public void placeOrder(@RequestBody OrderDtoIn orderDtoIn, @AuthenticationPrincipal Jwt jwt) {
    //todo intercepter
    String username = jwt.getClaim("preferred_username");
    UUID userID = userService.getUserIdByKeycloakUsername(username);
    orderDtoIn.setUserId(userID);
    orderProcessingService.process(orderDtoIn);
  }

}
