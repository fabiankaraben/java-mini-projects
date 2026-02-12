package com.example.hft.service;

import com.example.hft.model.TradeEvent;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TradeEventHandler implements EventHandler<TradeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(TradeEventHandler.class);

    @Override
    public void onEvent(TradeEvent event, long sequence, boolean endOfBatch) {
        // Business logic: Generate an order based on the trade tick
        // Simulating processing logic
        if (event.getPrice() > 100.0) {
            event.setOrderId(UUID.randomUUID().toString());
            // In a real app, this would send an order to an exchange
            logger.info("Order generated: {} for symbol: {} at price: {}", event.getOrderId(), event.getSymbol(), event.getPrice());
        }
    }
}
