package com.example.OrderMatchingService;

import com.example.OrderMatchingService.domain.*;
import com.example.OrderMatchingService.repository.OrderRepository;
import com.example.OrderMatchingService.repository.TradeRepository;
import com.example.OrderMatchingService.service.OrderPlacementService;
import com.example.OrderMatchingService.service.TradePlacementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class TradePlacementServiceTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradePlacementService tradePlacementService;

    @Test
    void shouldSaveTradeInDatabase() {
        Trade trade = new Trade(
                null, UUID.randomUUID(),UUID.randomUUID(), UUID.randomUUID(),UUID.randomUUID(),"ticker", 200L
                , 100,
                new Date(), TradeStatus.CREATED
        );

        tradePlacementService.placeTrade(trade);

        Optional<Trade> savedTrade = tradeRepository.findById(trade.getTradeID());

        assertTrue(savedTrade.isPresent());
        assertEquals(trade.getTradeID(), savedTrade.get().getTradeID());
    }
}
