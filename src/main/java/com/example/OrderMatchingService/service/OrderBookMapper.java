package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.Order;
import com.example.OrderMatchingService.dto.OrderBookDTO;
import com.example.OrderMatchingService.dto.OrderDtoOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBookMapper {

  @Autowired
  private UserService userService;

    public OrderBookDTO toDTO(List<Order> buyOrders, List<Order> sellOrders) {
        OrderBookDTO dto = new OrderBookDTO();
        dto.setBuyOrders(buyOrders.stream().map(this::toOrderDTO).toList());
        dto.setSellOrders(sellOrders.stream().map(this::toOrderDTO).toList());
        return dto;
    }

    private  OrderDtoOut toOrderDTO(Order order) {
        OrderDtoOut dto = new OrderDtoOut();
        dto.setTickerName(order.getTickerName());
        dto.setPrice(order.getPrice());
        dto.setQuantity(order.getQuantity()); // assuming this exists
        dto.setCreatedAt(order.getCreatedAt());
        String username = userService.getKeyCloackUserNameById(order.getUserId());
        dto.setKeycloakUsername(username);
        return dto;
    }
}

