package com.example.crawler;

import com.example.crawler.model.CrawledPage;
import com.example.crawler.service.CrawlerService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
class DistributedWebCrawlerIntegrationTest {

    @Autowired
    private CrawlerService crawlerService;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(0); // Random port
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());

        // Setup mock pages
        stubFor(get(urlEqualTo("/page1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody("<html><head><title>Page 1</title></head><body><a href='" + wireMockServer.baseUrl() + "/page2'>Link to Page 2</a></body></html>")));

        stubFor(get(urlEqualTo("/page2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody("<html><head><title>Page 2</title></head><body><a href='" + wireMockServer.baseUrl() + "/page3'>Link to Page 3</a></body></html>")));

        stubFor(get(urlEqualTo("/page3"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withBody("<html><head><title>Page 3</title></head><body>No links here</body></html>")));
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
    }

    @Test
    void testCrawling() {
        String seedUrl = wireMockServer.baseUrl() + "/page1";
        
        crawlerService.startCrawl(seedUrl, 10);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            Map<String, CrawledPage> pages = crawlerService.getCrawledPages();
            assertThat(pages).hasSize(3);
            
            assertThat(pages).containsKey(seedUrl);
            assertThat(pages.get(seedUrl).getTitle()).isEqualTo("Page 1");
            assertThat(pages.get(seedUrl).getLinks()).contains(wireMockServer.baseUrl() + "/page2");

            assertThat(pages).containsKey(wireMockServer.baseUrl() + "/page2");
            assertThat(pages.get(wireMockServer.baseUrl() + "/page2").getTitle()).isEqualTo("Page 2");
            
            assertThat(pages).containsKey(wireMockServer.baseUrl() + "/page3");
        });
    }
}
