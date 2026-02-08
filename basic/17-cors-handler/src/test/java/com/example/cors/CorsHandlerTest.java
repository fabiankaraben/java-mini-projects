package com.example.cors;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CorsHandlerTest {

    private HttpServer server;
    private static final int PORT = 8081; // Use a different port for testing

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/data", new CorsHandler.MyCorsHandler());
        server.setExecutor(null);
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void testOptionsPreflightRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/data"))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://example.com")
                .header("Access-Control-Request-Method", "GET")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        assertEquals("*", response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));
        assertTrue(response.headers().firstValue("Access-Control-Allow-Methods").orElse("").contains("GET"));
    }

    @Test
    void testGetRequestWithCorsHeaders() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/api/data"))
                .GET()
                .header("Origin", "http://example.com")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("*", response.headers().firstValue("Access-Control-Allow-Origin").orElse(""));
        assertTrue(response.body().contains("CORS-enabled response"));
    }
}
