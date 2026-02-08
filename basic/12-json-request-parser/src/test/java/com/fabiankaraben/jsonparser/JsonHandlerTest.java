package com.fabiankaraben.jsonparser;

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

class JsonHandlerTest {

    private HttpServer server;
    private HttpClient client;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        // Start server on a random available port
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/api/user", new JsonHandler());
        server.setExecutor(null);
        server.start();

        port = server.getAddress().getPort();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void testValidUser() throws IOException, InterruptedException {
        String json = "{\"name\":\"John Doe\",\"age\":30,\"email\":\"john@example.com\"}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("User received: User{name='John Doe', age=30, email='john@example.com'}"));
    }

    @Test
    void testInvalidMethod() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }

    @Test
    void testInvalidJson() throws IOException, InterruptedException {
        String json = "{invalid-json}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("Invalid JSON format"));
    }

    @Test
    void testValidationFailure_EmptyName() throws IOException, InterruptedException {
        String json = "{\"name\":\"\",\"age\":30,\"email\":\"john@example.com\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("Validation Error: Name cannot be empty"));
    }

    @Test
    void testValidationFailure_InvalidAge() throws IOException, InterruptedException {
        String json = "{\"name\":\"John\",\"age\":0,\"email\":\"john@example.com\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("Validation Error: Age must be greater than 0"));
    }
}
