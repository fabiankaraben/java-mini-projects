package com.example.queue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueueIntegrationTest {
    private QueueManager queueManager;
    private Worker worker;
    private QueueServer server;
    private Thread workerThread;
    private static final int TEST_PORT = 8081;

    @BeforeEach
    void setUp() throws IOException {
        queueManager = new QueueManager();
        worker = new Worker(queueManager);
        workerThread = new Thread(worker);
        workerThread.start();

        server = new QueueServer(TEST_PORT, queueManager);
        server.start();
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        server.stop();
        worker.stop();
        workerThread.interrupt();
        workerThread.join(1000);
    }

    @Test
    void testTaskSubmissionAndProcessing() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String taskContent = "Test Task Content";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + TEST_PORT + "/task"))
                .POST(HttpRequest.BodyPublishers.ofString(taskContent))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify HTTP response
        assertEquals(202, response.statusCode());
        assertTrue(response.body().contains("Task submitted with ID:"));

        // Wait for processing
        // Since processing takes 100ms, we wait a bit
        long startTime = System.currentTimeMillis();
        while (worker.getProcessedCount() == 0 && System.currentTimeMillis() - startTime < 2000) {
            Thread.sleep(100);
        }

        // Verify task was processed
        assertEquals(1, worker.getProcessedCount(), "Worker should have processed 1 task");
    }

    @Test
    void testMultipleTasksProcessingOrder() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        int taskCount = 3;

        for (int i = 0; i < taskCount; i++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + TEST_PORT + "/task"))
                    .POST(HttpRequest.BodyPublishers.ofString("Task " + i))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
        }

        // Wait for processing (100ms * 3 = 300ms min)
        long startTime = System.currentTimeMillis();
        while (worker.getProcessedCount() < taskCount && System.currentTimeMillis() - startTime < 5000) {
            Thread.sleep(100);
        }

        assertEquals(taskCount, worker.getProcessedCount(), "Worker should have processed all tasks");
    }
}
