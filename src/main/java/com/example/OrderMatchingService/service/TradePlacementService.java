package com.example.OrderMatchingService.service;
import com.example.OrderMatchingService.domain.Trade;
import com.example.OrderMatchingService.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class TradePlacementService {

    private final TradeRepository tradeRepository;

    public TradePlacementService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public boolean isValid(Trade trade) {
        return trade.getQuantity() > 0 && trade.getPrice() > 0;
    }

    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    public Optional<Trade> findById(UUID tradeId) {
        return tradeRepository.findById(tradeId);
    }

    public void deleteById(UUID tradeId) {
        tradeRepository.deleteById(tradeId);
    }

    public void placeTrade(Trade trade) {
        if (!isValid(trade)) {
            throw new IllegalArgumentException("Invalid trade");
        }
        save(trade);
    }
}
