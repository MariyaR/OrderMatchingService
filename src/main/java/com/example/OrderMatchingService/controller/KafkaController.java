package com.example.OrderMatchingService.controller;


import com.example.OrderMatchingService.service.OrderProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

  private final OrderProducerService producerService;

  public KafkaController(OrderProducerService producerService) {
    this.producerService = producerService;
  }

  @PostMapping("/send")
  public String sendMessage(@RequestParam String message) {
    //.sendMessage(message);
    return "Message sent to Kafka topic!";
  }
}
