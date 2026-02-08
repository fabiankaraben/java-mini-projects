package com.example.ratelimiter;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RateLimiterTest {

    private HttpServer server;
    private static final int PORT = 8081; // Use a different port for testing
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = App.startServer(PORT);
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void testRateLimitConcurrency() throws InterruptedException, ExecutionException {
        // The bucket has capacity 10, refill rate 5/sec.
        // We start with 10 tokens.
        
        int numberOfRequests = 20;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);
        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            tasks.add(() -> {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + PORT + "/api/resource"))
                        .GET()
                        .build();
                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    return response.statusCode();
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            });
        }

        // Send all requests "simultaneously"
        List<Future<Integer>> futures = executor.invokeAll(tasks);
        
        List<Integer> statusCodes = new ArrayList<>();
        for (Future<Integer> future : futures) {
            statusCodes.add(future.get());
        }
        executor.shutdown();

        long successCount = statusCodes.stream().filter(c -> c == 200).count();
        long tooManyRequestsCount = statusCodes.stream().filter(c -> c == 429).count();

        System.out.println("Success count: " + successCount);
        System.out.println("429 count: " + tooManyRequestsCount);

        // We expect at least the initial capacity (10) to succeed.
        // Since we fire 20 requests rapidly, some should fail (get 429).
        // Refill is 5/sec, so if the test runs very fast, we mostly consume the initial 10.
        
        assertTrue(successCount >= 10, "Should allow at least 10 requests (burst capacity)");
        assertTrue(tooManyRequestsCount > 0, "Should have some 429 responses when exceeding capacity");
        
        // Ensure total requests processed match
        assertEquals(numberOfRequests, successCount + tooManyRequestsCount);
    }
}
