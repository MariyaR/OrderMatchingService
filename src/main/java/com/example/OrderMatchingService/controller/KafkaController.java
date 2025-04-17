package com.example.OrderMatchingService.controller;


import com.example.OrderMatchingService.dto.MessageDto;
import com.example.OrderMatchingService.service.OrderProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

  private final OrderProducerService producerService;

  public KafkaController(OrderProducerService producerService) {
    this.producerService = producerService;
  }

  @PostMapping("/send")
  public String sendMessage(@RequestBody MessageDto messageDto) {
    producerService.sendMessage(messageDto.getText());
    return "Message sent to Kafka topic!";
  }
}
