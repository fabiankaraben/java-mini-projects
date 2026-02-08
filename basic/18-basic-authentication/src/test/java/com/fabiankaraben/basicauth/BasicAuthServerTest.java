package com.fabiankaraben.basicauth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicAuthServerTest {

    private BasicAuthServer server;
    private HttpClient client;
    private static final String BASE_URL = "http://localhost:8080/protected";

    @BeforeEach
    void setUp() throws IOException {
        server = new BasicAuthServer();
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
        // Give the server a moment to fully release the port
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testMissingAuthHeader() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(401, response.statusCode());
    }

    @Test
    void testInvalidCredentials() throws IOException, InterruptedException {
        String invalidAuth = "Basic " + Base64.getEncoder().encodeToString("user:wrongpass".getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Authorization", invalidAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(401, response.statusCode());
    }

    @Test
    void testValidCredentials() throws IOException, InterruptedException {
        String validAuth = "Basic " + Base64.getEncoder().encodeToString("admin:secret".getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Authorization", validAuth)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals("Authentication successful! Welcome to the protected area.", response.body());
    }
}
