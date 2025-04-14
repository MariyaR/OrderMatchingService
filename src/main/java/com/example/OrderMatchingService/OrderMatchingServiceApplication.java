package com.example.OrderMatchingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrderMatchingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMatchingServiceApplication.class, args);
	}

}
