package com.fabiankaraben.timeouthandler;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeoutHandlerTest {

    private HttpServer server;
    private static final int PORT = 8081; // Use a different port for testing

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Define a timeout of 1 second for the server handler
        Duration serverTimeout = Duration.ofSeconds(1);
        
        // Handler that sleeps for 2 seconds (should timeout)
        server.createContext("/slow", new TimeoutHandler(new SlowHandler(2000), serverTimeout));
        
        // Handler that sleeps for 500ms (should pass)
        server.createContext("/fast", new TimeoutHandler(new SlowHandler(500), serverTimeout));
        
        server.setExecutor(null);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void testTimeoutResponse() throws IOException, InterruptedException {
        // Client with a higher timeout than the server's handler timeout
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/slow"))
                .timeout(Duration.ofSeconds(5)) // Client waits longer
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Expect 503 Service Unavailable because the server handler timed out
        assertEquals(503, response.statusCode());
        assertEquals("Request timed out", response.body());
    }

    @Test
    void testSuccessResponse() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/fast"))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Expect 200 OK
        assertEquals(200, response.statusCode());
        assertEquals("Process complete", response.body());
    }
}
