package com.fabiankaraben.reverseproxy;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReverseProxyIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.upstream.service-url", () -> wireMockServer.baseUrl());
        registry.add("app.upstream.echo-url", () -> wireMockServer.baseUrl());
    }

    @Test
    public void testServiceRoute_ShouldRouteToUpstreamAndAddHeader() {
        // Setup WireMock stub
        wireMockServer.stubFor(get(urlEqualTo("/resource"))
                .withHeader("X-Custom-Header", equalTo("Gateway-Processed"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Service Response")));

        // Call Gateway
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/service/resource", String.class);

        // Verify Response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Service Response");

        // Verify Request to Upstream contained the header
        wireMockServer.verify(getRequestedFor(urlEqualTo("/resource"))
                .withHeader("X-Custom-Header", equalTo("Gateway-Processed")));
    }

    @Test
    public void testEchoRoute_ShouldRouteToUpstreamAndAddResponseHeader() {
        // Setup WireMock stub
        wireMockServer.stubFor(get(urlEqualTo("/echo/test"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("Echo Response")));

        // Call Gateway
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/echo/echo/test", String.class);

        // Verify Response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Echo Response");
        assertThat(response.getHeaders().getFirst("X-Echo-Response")).isEqualTo("True");
        
        // Verify Request to Upstream
        wireMockServer.verify(getRequestedFor(urlEqualTo("/echo/test")));
    }
}
