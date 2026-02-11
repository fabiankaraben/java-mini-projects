package com.fabiankaraben.stocksimulator.service;

import com.fabiankaraben.stocksimulator.model.Stock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class StockService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<String, BigDecimal> stockPrices = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private static final String[] SYMBOLS = {"AAPL", "GOOGL", "AMZN", "MSFT", "TSLA"};

    public StockService() {
        // Initialize with base prices
        for (String symbol : SYMBOLS) {
            stockPrices.put(symbol, BigDecimal.valueOf(100 + random.nextDouble() * 500));
        }
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Infinite timeout
        
        Runnable removeEmitter = () -> emitters.remove(emitter);
        
        emitter.onCompletion(removeEmitter);
        emitter.onTimeout(removeEmitter);
        emitter.onError((e) -> removeEmitter.run());

        emitters.add(emitter);
        return emitter;
    }

    @Scheduled(fixedRate = 1000)
    public void sendStockUpdates() {
        List<Stock> updates = new ArrayList<>();
        
        // Update prices
        for (String symbol : SYMBOLS) {
            BigDecimal currentPrice = stockPrices.get(symbol);
            // Change by -2% to +2%
            double changeFactor = 0.98 + (random.nextDouble() * 0.04);
            BigDecimal newPrice = currentPrice.multiply(BigDecimal.valueOf(changeFactor))
                    .setScale(2, RoundingMode.HALF_UP);
            stockPrices.put(symbol, newPrice);
            updates.add(new Stock(symbol, newPrice, Instant.now()));
        }

        // Send to all emitters
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("stock-update")
                        .data(updates));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }
        emitters.removeAll(deadEmitters);
    }
}
