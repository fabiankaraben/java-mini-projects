package com.example.hft.controller;

import com.example.hft.service.TradeEventProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trading")
public class TradingController {

    private final TradeEventProducer tradeEventProducer;

    public TradingController(TradeEventProducer tradeEventProducer) {
        this.tradeEventProducer = tradeEventProducer;
    }

    @PostMapping("/tick")
    public ResponseEntity<String> receiveTick(@RequestParam String symbol, @RequestParam double price, @RequestParam double volume) {
        tradeEventProducer.onData(symbol, price, volume);
        return ResponseEntity.ok("Tick processed");
    }
}
