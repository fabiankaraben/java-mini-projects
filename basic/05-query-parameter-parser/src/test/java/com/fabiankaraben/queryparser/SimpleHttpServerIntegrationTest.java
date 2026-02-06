package com.fabiankaraben.queryparser;

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

class SimpleHttpServerIntegrationTest {

    private static SimpleHttpServer server;
    private static HttpClient client;
    private static String baseUrl;

    @BeforeAll
    static void setUp() throws IOException {
        server = new SimpleHttpServer();
        server.start();
        int port = server.getPort();
        baseUrl = "http://localhost:" + port + "/api/parse";
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testParseQueryParameters() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?name=Alice&role=admin"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").contains("application/json"));
        
        String body = response.body();
        assertTrue(body.contains("\"name\":\"Alice\""));
        assertTrue(body.contains("\"role\":\"admin\""));
    }

    @Test
    void testParseUrlEncodedParameters() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?message=Hello%20World&symbol=%24"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String body = response.body();
        assertTrue(body.contains("\"message\":\"Hello World\""));
        assertTrue(body.contains("\"symbol\":\"$\""));
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
