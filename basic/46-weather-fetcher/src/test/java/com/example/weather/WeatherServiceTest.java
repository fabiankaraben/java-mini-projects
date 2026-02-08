package com.example.weather;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherServiceTest {

    private WireMockServer wireMockServer;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // Start WireMock on a random port or fixed port. 
        // Using 0 for dynamic port, but we need to know it to pass to service.
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        // Initialize service with WireMock's base URL
        weatherService = new WeatherService("http://localhost:" + wireMockServer.port() + "/v1/forecast");
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetWeatherReturnsJson() throws IOException, InterruptedException {
        // Mock response body
        String jsonResponse = "{\"latitude\":52.52,\"longitude\":13.41,\"generationtime_ms\":2.2,\"utc_offset_seconds\":0,\"timezone\":\"GMT\",\"timezone_abbreviation\":\"GMT\",\"elevation\":38.0,\"current_weather\":{\"temperature\":15.5,\"windspeed\":10.0,\"winddirection\":180,\"weathercode\":3,\"time\":\"2023-08-01T12:00\"}}";

        // Stub the external API
        wireMockServer.stubFor(get(urlMatching("/v1/forecast.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
                        .withStatus(200)));

        // Call the service
        String result = weatherService.getWeather(52.52, 13.41);

        // Verify the result
        assertEquals(jsonResponse, result, "The service should return the raw JSON from the provider");
        
        // Verify WireMock received the request with correct parameters
        wireMockServer.verify(getRequestedFor(urlMatching("/v1/forecast\\?latitude=52\\.52&longitude=13\\.41&current_weather=true")));
    }
    
    @Test
    void testGetWeatherHandlesError() {
        wireMockServer.stubFor(get(urlMatching("/v1/forecast.*"))
                .willReturn(aResponse()
                        .withStatus(500)));
                        
        try {
            weatherService.getWeather(52.52, 13.41);
        } catch (IOException | InterruptedException e) {
            assertTrue(e.getMessage().contains("HTTP 500"));
        }
    }
}
