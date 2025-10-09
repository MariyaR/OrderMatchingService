package com.example.OrderMatchingService.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafKaConfig {

  @Value("${kafka.topics.trade-created}")
  private String tradeCreatedTopic;

  @Value("${kafka.topics.order-created}")
  private String orderCreatedTopic;
  @Bean
  public NewTopic topic1() {
    return new NewTopic(tradeCreatedTopic, 3, (short) 1);
  }

  @Bean
  public NewTopic topic2() {
    return new NewTopic(orderCreatedTopic, 5, (short) 1);
  }
}
