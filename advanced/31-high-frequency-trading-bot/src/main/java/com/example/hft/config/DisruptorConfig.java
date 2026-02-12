package com.example.hft.config;

import com.example.hft.model.TradeEvent;
import com.example.hft.model.TradeEventFactory;
import com.example.hft.service.TradeEventHandler;
import com.example.hft.service.TradeEventProducer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DisruptorConfig {

    @Bean
    public Disruptor<TradeEvent> disruptor(TradeEventHandler tradeEventHandler) {
        int bufferSize = 1024; // Must be power of 2
        TradeEventFactory factory = new TradeEventFactory();

        Disruptor<TradeEvent> disruptor = new Disruptor<>(
                factory,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        disruptor.handleEventsWith(tradeEventHandler);
        disruptor.start();
        return disruptor;
    }

    @Bean
    public RingBuffer<TradeEvent> ringBuffer(Disruptor<TradeEvent> disruptor) {
        return disruptor.getRingBuffer();
    }

    @Bean
    public TradeEventProducer tradeEventProducer(RingBuffer<TradeEvent> ringBuffer) {
        return new TradeEventProducer(ringBuffer);
    }
}
