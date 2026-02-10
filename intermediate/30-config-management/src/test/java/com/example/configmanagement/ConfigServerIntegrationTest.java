package com.example.configmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigServerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldReturnDefaultConfiguration() {
        ResponseEntity<Environment> entity = restTemplate.getForEntity(
                "http://localhost:" + port + "/client-service/default", Environment.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().getPropertySources()).isNotEmpty();
        
        // Check if the property source contains the expected property
        boolean hasProperty = entity.getBody().getPropertySources().stream()
                .anyMatch(ps -> ps.getSource().containsKey("example.property") && 
                               "This is the default configuration".equals(ps.getSource().get("example.property")));
        
        assertThat(hasProperty).isTrue();
    }

    @Test
    void shouldReturnDevConfiguration() {
        ResponseEntity<Environment> entity = restTemplate.getForEntity(
                "http://localhost:" + port + "/client-service/dev", Environment.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        
        // Check if the property source contains the expected dev property
        boolean hasProperty = entity.getBody().getPropertySources().stream()
                .anyMatch(ps -> ps.getSource().containsKey("example.property") && 
                               "This is the dev configuration".equals(ps.getSource().get("example.property")));
        
        assertThat(hasProperty).isTrue();
    }
}
