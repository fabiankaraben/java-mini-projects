package com.example.searchengine;

import com.example.searchengine.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchEngineApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testIndexAndSearchFlow() {
        String baseUrl = "http://localhost:" + port + "/api/search";

        // Index documents
        Document doc1 = new Document("doc1", "Spring Boot is awesome");
        Document doc2 = new Document("doc2", "Spring Cloud is cool");

        ResponseEntity<String> response1 = restTemplate.postForEntity(baseUrl + "/index", doc1, String.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<String> response2 = restTemplate.postForEntity(baseUrl + "/index", doc2, String.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Search for "Spring"
        ResponseEntity<Set> searchResponse = restTemplate.getForEntity(baseUrl + "?query=Spring", Set.class);
        assertThat(searchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchResponse.getBody()).contains("doc1", "doc2");

        // Search for "Boot"
        searchResponse = restTemplate.getForEntity(baseUrl + "?query=Boot", Set.class);
        assertThat(searchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(searchResponse.getBody()).contains("doc1");
        assertThat(searchResponse.getBody()).doesNotContain("doc2");
    }
}
