package com.example.hft.benchmark;

import com.example.hft.model.TradeEvent;
import com.example.hft.model.TradeEventFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class DisruptorLatencyTest {

    private static final int EVENT_COUNT = 1_000_000;
    private final long[] latencies = new long[EVENT_COUNT];

    @Test
    public void testLatency() throws InterruptedException {
        TradeEventFactory factory = new TradeEventFactory();
        int bufferSize = 1024 * 64; 

        Disruptor<TradeEvent> disruptor = new Disruptor<>(
                factory,
                bufferSize,
                DaemonThreadFactory.INSTANCE,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        CountDownLatch latch = new CountDownLatch(EVENT_COUNT);
        
        EventHandler<TradeEvent> latencyHandler = (event, sequence, endOfBatch) -> {
            long endTime = System.nanoTime();
            long startTime = event.getTimestamp();
            int index = (int) sequence;
            if (index < EVENT_COUNT) {
                latencies[index] = endTime - startTime;
            }
            latch.countDown();
        };

        disruptor.handleEventsWith(latencyHandler);
        disruptor.start();

        RingBuffer<TradeEvent> ringBuffer = disruptor.getRingBuffer();

        System.out.println("Starting latency test with " + EVENT_COUNT + " events...");
        
        long startLoop = System.nanoTime();
        for (int i = 0; i < EVENT_COUNT; i++) {
            long sequence = ringBuffer.next();
            try {
                TradeEvent event = ringBuffer.get(sequence);
                event.set("TEST", 100.0, 10, System.nanoTime());
            } finally {
                ringBuffer.publish(sequence);
            }
        }
        
        latch.await();
        long endLoop = System.nanoTime();

        calculateAndPrintStats();
        
        disruptor.shutdown();
        
        double throughput = EVENT_COUNT / ((endLoop - startLoop) / 1_000_000_000.0);
        System.out.printf("Throughput: %.0f ops/sec%n", throughput);
    }

    private void calculateAndPrintStats() {
        Arrays.sort(latencies);

        long p50 = latencies[(int) (EVENT_COUNT * 0.50)];
        long p99 = latencies[(int) (EVENT_COUNT * 0.99)];
        long p999 = latencies[(int) (EVENT_COUNT * 0.999)];
        long max = latencies[EVENT_COUNT - 1];
        long min = latencies[0];
        long avg = Arrays.stream(latencies).sum() / EVENT_COUNT;

        System.out.println("Latency Results (microseconds):");
        System.out.println("Min: " + TimeUnit_toMicros(min));
        System.out.println("Avg: " + TimeUnit_toMicros(avg));
        System.out.println("P50: " + TimeUnit_toMicros(p50));
        System.out.println("P99: " + TimeUnit_toMicros(p99));
        System.out.println("P99.9: " + TimeUnit_toMicros(p999));
        System.out.println("Max: " + TimeUnit_toMicros(max));
    }

    private double TimeUnit_toMicros(long nanos) {
        return nanos / 1000.0;
    }
}
