package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.events.OrderMatchedEvent;
import com.example.OrderMatchingService.service.OrderEventConsumerService;
import com.example.OrderMatchingService.service.OrderEventPublisher;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmbededKafkaTest {

    @Autowired
    OrderEventConsumerService kafkaConsumerService;

    @Autowired
    OrderEventPublisher orderEventPublisher;

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived()
            throws Exception {
        OrderMatchedEvent event = new OrderMatchedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),  // Buy order ID
                UUID.randomUUID(),  // Sell order ID
                "ticker",
                80,
                150L,
                new Date()
        );


        orderEventPublisher.publishOrderMathedEvent(event);

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    OrderMatchedEvent receivedEvent = kafkaConsumerService.getMessage();
                    assertNotNull(receivedEvent);
                    assertEquals(receivedEvent.getTicker(), event.getTicker());
                });
    }
}
