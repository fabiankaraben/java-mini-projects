package com.example.hft.service;

import com.example.hft.model.TradeEvent;
import com.lmax.disruptor.RingBuffer;
import org.springframework.stereotype.Component;

@Component
public class TradeEventProducer {

    private final RingBuffer<TradeEvent> ringBuffer;

    public TradeEventProducer(RingBuffer<TradeEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String symbol, double price, double volume) {
        long sequence = ringBuffer.next();
        try {
            TradeEvent event = ringBuffer.get(sequence);
            event.set(symbol, price, volume, System.nanoTime());
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
