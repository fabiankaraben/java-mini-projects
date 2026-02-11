package com.fabiankaraben.stocksimulator;

import com.fabiankaraben.stocksimulator.model.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class StockPriceSimulatorApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testStockPriceStream() {
        Flux<List<Stock>> stockStream = webTestClient.get()
                .uri("/api/stocks/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(new ParameterizedTypeReference<List<Stock>>() {})
                .getResponseBody();

        StepVerifier.create(stockStream)
                .assertNext(stocks -> {
                    assertNotNull(stocks);
                    assertFalse(stocks.isEmpty());
                    // Verify we have all symbols
                    assertNotNull(stocks.get(0).symbol());
                    assertNotNull(stocks.get(0).price());
                    assertNotNull(stocks.get(0).timestamp());
                })
                .assertNext(stocks -> {
                    assertNotNull(stocks);
                    assertFalse(stocks.isEmpty());
                })
                .assertNext(stocks -> {
                    assertNotNull(stocks);
                    assertFalse(stocks.isEmpty());
                })
                .thenCancel()
                .verify();
    }
}
