package com.example.OrderMatchingService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class OrderBookDTO {
    private List<OrderDtoOut> buyOrders;
    private List<OrderDtoOut> sellOrders;

}
