package com.fabiankaraben.miniprojects.servicea;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ServiceAIntegrationTest {

    @Container
    static GenericContainer<?> zipkin = new GenericContainer<>(DockerImageName.parse("openzipkin/zipkin:latest"))
            .withExposedPorts(9411);

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("management.zipkin.tracing.endpoint", 
                () -> "http://" + zipkin.getHost() + ":" + zipkin.getMappedPort(9411) + "/api/v2/spans");
        registry.add("service.b.url", () -> wireMock.baseUrl());
    }

    @Test
    void testTracePropagationAndReporting() {
        // Mock Service B response
        wireMock.stubFor(get(urlEqualTo("/greet"))
                .willReturn(aResponse()
                        .withBody("Hello from Mocked Service B")
                        .withStatus(200)));

        // Call Service A
        String response = restTemplate.getForObject("http://localhost:" + port + "/hello", String.class);

        // Verify response
        assertThat(response).contains("Service A calling -> Hello from Mocked Service B");

        // Verify Trace Context Propagation to Service B
        wireMock.verify(getRequestedFor(urlEqualTo("/greet"))
                .withHeader("traceparent", matching(".+"))
                .withHeader("b3", matching(".+"))); // Brave/Spring Cloud Sleuth often sends b3 headers too

        // Verify Span Reporting to Zipkin
        // We poll the Zipkin API to check if spans were received
        String zipkinApiUrl = "http://" + zipkin.getHost() + ":" + zipkin.getMappedPort(9411) + "/api/v2/traces";
        TestRestTemplate zipkinClient = new TestRestTemplate();

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            // Zipkin returns a list of traces, where each trace is a list of spans
            String traces = zipkinClient.getForObject(zipkinApiUrl, String.class);
            assertThat(traces).contains("service-a"); // Check that service name appears in traces
        });
    }
}
