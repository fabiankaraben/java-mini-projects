package com.example.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    private final QueueManager queueManager;
    private volatile boolean running = true;
    private final java.util.concurrent.atomic.AtomicInteger processedCount = new java.util.concurrent.atomic.AtomicInteger(0);

    public Worker(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Override
    public void run() {
        logger.info("Worker started, waiting for tasks...");
        while (running) {
            try {
                Task task = queueManager.take();
                process(task);
                processedCount.incrementAndGet();
            } catch (InterruptedException e) {
                logger.info("Worker interrupted, shutting down.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void process(Task task) {
        logger.info("Processing task: {}", task);
        try {
            // Simulate processing time
            Thread.sleep(100); 
            logger.info("Completed task: {}", task.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        this.running = false;
    }

    public int getProcessedCount() {
        return processedCount.get();
    }
}
