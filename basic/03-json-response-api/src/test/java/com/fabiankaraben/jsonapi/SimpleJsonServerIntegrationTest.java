package com.fabiankaraben.jsonapi;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleJsonServerIntegrationTest {

    private static SimpleJsonServer server;
    private static HttpClient client;
    private static String baseUrl;

    @BeforeAll
    static void setUp() throws IOException {
        server = new SimpleJsonServer();
        server.start();
        int port = server.getPort();
        baseUrl = "http://localhost:" + port + "/api/user";
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testGetUser() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        
        // Check content type
        assertTrue(response.headers().firstValue("Content-Type").orElse("").contains("application/json"));
        
        // Check body content
        String body = response.body();
        assertTrue(body.contains("\"id\":1"));
        assertTrue(body.contains("\"name\":\"John Doe\""));
        assertTrue(body.contains("\"email\":\"john.doe@example.com\""));
    }

    @Test
    void testMethodNotAllowed() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }
}
