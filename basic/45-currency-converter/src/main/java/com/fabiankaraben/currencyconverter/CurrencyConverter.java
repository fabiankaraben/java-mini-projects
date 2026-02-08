package com.fabiankaraben.currencyconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class CurrencyConverter {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl;

    public CurrencyConverter(String apiUrl) {
        this.apiUrl = apiUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    // For testing purposes to inject a mock HttpClient if needed, 
    // though WireMock is preferred for integration testing.
    public CurrencyConverter(String apiUrl, HttpClient httpClient) {
        this.apiUrl = apiUrl;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public double convert(String from, String to, double amount) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "/" + from))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch exchange rates. Status code: " + response.statusCode());
        }

        ExchangeRateResponse exchangeRateResponse = objectMapper.readValue(response.body(), ExchangeRateResponse.class);
        Map<String, Double> rates = exchangeRateResponse.getRates();

        if (rates == null || !rates.containsKey(to)) {
            throw new IllegalArgumentException("Currency code not found: " + to);
        }

        double rate = rates.get(to);
        return amount * rate;
    }
}
