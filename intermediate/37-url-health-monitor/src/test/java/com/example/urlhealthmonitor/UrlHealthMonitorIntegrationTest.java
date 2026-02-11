package com.example.urlhealthmonitor;

import com.example.urlhealthmonitor.model.MonitoredUrl;
import com.example.urlhealthmonitor.model.UrlCheckHistory;
import com.example.urlhealthmonitor.repository.MonitoredUrlRepository;
import com.example.urlhealthmonitor.repository.UrlCheckHistoryRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class UrlHealthMonitorIntegrationTest {

    @Autowired
    private MonitoredUrlRepository urlRepository;

    @Autowired
    private UrlCheckHistoryRepository historyRepository;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {
        historyRepository.deleteAll();
        urlRepository.deleteAll();
    }

    @Test
    void testUrlMonitoring() {
        // Setup WireMock stub
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/health"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withBody("OK")));

        String monitoredUrl = wireMockServer.baseUrl() + "/health";

        // Add URL to monitor
        MonitoredUrl url = new MonitoredUrl(monitoredUrl);
        urlRepository.save(url);

        // Wait for schedule to run (configured to 10s in application.properties, but we might want to trigger it or wait)
        // Since the schedule is 10s, we wait at least that long.
        // Ideally, we can configure the schedule to be faster for tests using @TestPropertySource or DynamicPropertySource
        // But here we rely on the 10s default in application.properties or override it.
        // Let's rely on Awaitility to wait up to 15 seconds.

        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            List<UrlCheckHistory> history = historyRepository.findAll();
            assertThat(history).isNotEmpty();
            UrlCheckHistory check = history.get(0);
            assertThat(check.getStatusCode()).isEqualTo(200);
            assertThat(check.getStatus()).isEqualTo("UP");
        });
        
        // Verify the MonitoredUrl entity was also updated
        MonitoredUrl updatedUrl = urlRepository.findById(url.getId()).orElseThrow();
        assertThat(updatedUrl.getLastStatus()).isEqualTo("UP");
        assertThat(updatedUrl.getLastStatusCode()).isEqualTo(200);
        assertThat(updatedUrl.getLastCheckedAt()).isNotNull();
    }
    
    @Test
    void testUrlDownMonitoring() {
        // Setup WireMock stub for 503
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/down"))
                .willReturn(WireMock.aResponse()
                        .withStatus(503)
                        .withBody("Service Unavailable")));

        String monitoredUrl = wireMockServer.baseUrl() + "/down";

        MonitoredUrl url = new MonitoredUrl(monitoredUrl);
        urlRepository.save(url);

        await().atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            List<UrlCheckHistory> history = historyRepository.findAll();
            assertThat(history).isNotEmpty();
            UrlCheckHistory check = history.get(0);
            assertThat(check.getStatusCode()).isEqualTo(503);
            assertThat(check.getStatus()).isEqualTo("DOWN");
        });
    }
}
