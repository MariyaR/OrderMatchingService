package com.example.OrderMatchingService.configuration;

import com.example.OrderMatchingService.domain.matching.MatchingStrategy;
import com.example.OrderMatchingService.domain.matching.PriceTimePriorityStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatchingConfiguration {

    @Bean
    public MatchingStrategy fifoMatchStrategy() {
        return new PriceTimePriorityStrategy();
    }
}
