package com.fabiankaraben.inmemorydatastore;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreIntegrationTest {

    private static HttpServer server;
    private static DataStore dataStore;
    private static final int PORT = 8081; // Use a different port for testing
    private static final String BASE_URL = "http://localhost:" + PORT + "/items";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @BeforeAll
    static void startServer() throws IOException {
        dataStore = new DataStore();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/items", new StoreHandler(dataStore));
        server.setExecutor(null);
        server.start();
    }

    @AfterAll
    static void stopServer() {
        server.stop(0);
    }

    @BeforeEach
    void clearData() {
        dataStore.clear();
    }

    @Test
    void testCreateAndRetrieveItem() throws IOException, InterruptedException {
        // Create Item
        Item item = new Item("100", "Integration Test");
        String json = gson.toJson(item);
        
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, postResponse.statusCode());

        // Retrieve Item
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/100"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        
        Item retrieved = gson.fromJson(getResponse.body(), Item.class);
        assertEquals("Integration Test", retrieved.getContent());
    }

    @Test
    void testUpdateItem() throws IOException, InterruptedException {
        // Create
        Item item = new Item("101", "Original");
        dataStore.save(item);

        // Update
        Item update = new Item("101", "Updated");
        String json = gson.toJson(update);

        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/101"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, putResponse.statusCode());

        // Verify
        assertEquals("Updated", dataStore.findById("101").get().getContent());
    }

    @Test
    void testDeleteItem() throws IOException, InterruptedException {
        dataStore.save(new Item("102", "To Delete"));

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/102"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());
        
        assertEquals(0, dataStore.findAll().size());
    }
    
    @Test
    void testGetAllItems() throws IOException, InterruptedException {
        dataStore.save(new Item("201", "A"));
        dataStore.save(new Item("202", "B"));

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        
        Item[] items = gson.fromJson(response.body(), Item[].class);
        assertEquals(2, items.length);
    }
}
