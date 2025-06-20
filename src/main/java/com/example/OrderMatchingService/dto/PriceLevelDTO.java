package com.example.OrderMatchingService.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
public class PriceLevelDTO {

    private BigDecimal price;
    private List<OrderDtoOut> orders;
}
