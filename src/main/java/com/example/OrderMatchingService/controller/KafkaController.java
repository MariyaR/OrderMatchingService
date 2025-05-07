package com.example.OrderMatchingService.controller;

import com.example.OrderMatchingService.service.OrderEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

  private final OrderEventPublisher producerService;

  public KafkaController(OrderEventPublisher producerService) {
    this.producerService = producerService;
  }

}
