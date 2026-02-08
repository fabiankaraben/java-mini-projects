package com.example.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        QueueManager queueManager = new QueueManager();
        
        // Start Worker
        Worker worker = new Worker(queueManager);
        Thread workerThread = new Thread(worker, "Worker-Thread");
        workerThread.start();

        // Start HTTP Server
        QueueServer server = new QueueServer(PORT, queueManager);
        try {
            server.start();
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down...");
                server.stop();
                worker.stop();
                try {
                    workerThread.join(1000);
                } catch (InterruptedException e) {
                    logger.error("Error waiting for worker to stop", e);
                }
            }));
            
            // Keep main thread alive
            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            logger.error("Application error", e);
        }
    }
}
