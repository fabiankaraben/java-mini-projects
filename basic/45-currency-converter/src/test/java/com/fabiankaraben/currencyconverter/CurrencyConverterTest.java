package com.fabiankaraben.currencyconverter;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyConverterTest {

    private WireMockServer wireMockServer;
    private CurrencyConverter currencyConverter;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer(0); // Random port
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        String baseUrl = "http://localhost:" + wireMockServer.port();
        currencyConverter = new CurrencyConverter(baseUrl);
    }

    @AfterEach
    public void teardown() {
        wireMockServer.stop();
    }

    @Test
    public void testConvertSuccess() throws IOException, InterruptedException {
        String jsonResponse = "{"
                + "\"base\": \"USD\","
                + "\"date\": \"2023-10-01\","
                + "\"rates\": {"
                + "  \"EUR\": 0.85,"
                + "  \"GBP\": 0.73"
                + "}"
                + "}";

        stubFor(get(urlEqualTo("/USD"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
                        .withStatus(200)));

        double result = currencyConverter.convert("USD", "EUR", 100.0);
        assertEquals(85.0, result, 0.001);
    }

    @Test
    public void testConvertCurrencyNotFound() {
        String jsonResponse = "{"
                + "\"base\": \"USD\","
                + "\"rates\": {"
                + "  \"EUR\": 0.85"
                + "}"
                + "}";

        stubFor(get(urlEqualTo("/USD"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
                        .withStatus(200)));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyConverter.convert("USD", "JPY", 100.0);
        });

        assertTrue(exception.getMessage().contains("Currency code not found"));
    }

    @Test
    public void testApiFailure() {
        stubFor(get(urlEqualTo("/USD"))
                .willReturn(aResponse()
                        .withStatus(500)));

        Exception exception = assertThrows(IOException.class, () -> {
            currencyConverter.convert("USD", "EUR", 100.0);
        });

        assertTrue(exception.getMessage().contains("Failed to fetch exchange rates"));
    }
}
