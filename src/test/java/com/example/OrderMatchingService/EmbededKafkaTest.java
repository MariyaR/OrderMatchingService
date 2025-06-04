package com.example.OrderMatchingService;

import com.example.events.OrderMatchedEvent;
import com.example.OrderMatchingService.service.OrderEventConsumerService;
import com.example.OrderMatchingService.service.OrderEventPublisher;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1)
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
                UUID.randomUUID(),
                UUID.randomUUID(),
                "ticker",
                80,
                150L,
                LocalDateTime.now()
        );


        orderEventPublisher.publishOrderMatchedEvent(event);

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    OrderMatchedEvent receivedEvent = kafkaConsumerService.getMessage();
                    assertNotNull(receivedEvent);
                    assertEquals(receivedEvent.getTicker(), event.getTicker());
                });
    }
}
