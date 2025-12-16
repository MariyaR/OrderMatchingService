package com.example.OrderMatchingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto {

    private String userName;
    private String accountNumber;
    private BigDecimal balance;
    private Map<String, Long> tickers;
}

