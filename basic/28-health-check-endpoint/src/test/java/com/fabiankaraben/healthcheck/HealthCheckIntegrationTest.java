package com.fabiankaraben.healthcheck;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthCheckIntegrationTest {

    private HealthCheckApp app;

    @BeforeEach
    void setUp() throws IOException {
        app = new HealthCheckApp();
        app.start();
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }

    @Test
    void testHealthEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/health"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("{\"status\": \"OK\"}", response.body());
    }
}
