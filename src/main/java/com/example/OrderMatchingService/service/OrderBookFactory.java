package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.OrderBook;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class OrderBookFactory {

  private final ConcurrentHashMap<String, OrderBook> orderBooks = new ConcurrentHashMap<>();

  public OrderBook getOrCreate(String ticker) {
    return orderBooks.computeIfAbsent(ticker, t-> new OrderBook());
  }

  public void clear() {
    orderBooks.clear();
  }

}
