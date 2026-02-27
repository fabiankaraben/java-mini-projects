package com.example.jpms.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationTest {

    private static Process appProcess;
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    @BeforeAll
    static void startApp() throws Exception {
        String modulePath = new File("../app/target/modules").getAbsolutePath();
        
        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-p", modulePath,
            "-m", "com.example.jpms.app/com.example.jpms.app.Application"
        );
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        
        appProcess = pb.start();

        // Wait a bit for the server to start
        Thread.sleep(2000);
        if (!appProcess.isAlive()) {
            throw new RuntimeException("Application process failed to start! Check logs. Module path used: " + modulePath);
        }
    }

    @AfterAll
    static void stopApp() {
        if (appProcess != null && appProcess.isAlive()) {
            appProcess.destroyForcibly();
        }
    }

    @Test
    void testGetProducts() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products"))
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Laptop"));
    }

    @Test
    void testCreateProduct() throws Exception {
        String jsonPayload = "{\"name\":\"Monitor\",\"price\":300.0}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/products"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("Monitor"));
    }
}
