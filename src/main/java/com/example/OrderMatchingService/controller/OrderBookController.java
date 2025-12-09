package com.example.OrderMatchingService.controller;

import com.example.OrderMatchingService.dto.OrderBookDTO;
import com.example.OrderMatchingService.service.OrderBookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade-api/orderbook")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderBookController {

    private final OrderBookService orderBookService;

    public OrderBookController(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }


    @GetMapping()
    public OrderBookDTO getOrderBook() {
      return orderBookService.getOrderBook();
    }
}
