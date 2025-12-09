package com.example.OrderMatchingService.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "account", schema = "trade_executing")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private UUID id;

    @Column(name = "keycloak_username", nullable = false, unique = true)
    private String keycloakUsername;


    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @ElementCollection
    @MapKeyColumn(name = "ticker_type")
    @Column(name = "ticker_quantity")
    private Map<String, Long> tickers = new HashMap<>();

  public Account(String keycloakUsername, String accountNumber, BigDecimal balance) {
    this.keycloakUsername = keycloakUsername;
    this.accountNumber = accountNumber;
    this.balance = balance;
  }
}
