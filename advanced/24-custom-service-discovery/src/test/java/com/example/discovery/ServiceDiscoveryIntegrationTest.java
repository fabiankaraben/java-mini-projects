package com.example.discovery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceDiscoveryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServiceRegistry serviceRegistry;

    @BeforeEach
    void setUp() {
        serviceRegistry.clear();
    }

    @Test
    void testServiceRegistrationAndDiscovery() {
        // 1. Register Service A
        ServiceInstance serviceA = new ServiceInstance("service-a", "localhost", 8081);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/registry/register", serviceA, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 2. Register Service B
        ServiceInstance serviceB = new ServiceInstance("service-b", "localhost", 8082);
        restTemplate.postForEntity("/api/registry/register", serviceB, String.class);

        // 3. Discover all instances
        ResponseEntity<List<ServiceInstance>> allInstancesResponse = restTemplate.exchange(
                "/api/registry/instances",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(allInstancesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allInstancesResponse.getBody()).hasSize(2);

        // 4. Discover instances for Service A
        ResponseEntity<List<ServiceInstance>> serviceAInstancesResponse = restTemplate.exchange(
                "/api/registry/instances/service-a",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        assertThat(serviceAInstancesResponse.getBody()).hasSize(1);
        assertThat(serviceAInstancesResponse.getBody().get(0).getServiceId()).isEqualTo("service-a");
    }

    @Test
    void testHeartbeatAndExpiration() throws InterruptedException {
        // 1. Register a service
        ServiceInstance serviceC = new ServiceInstance("service-c", "localhost", 9090);
        restTemplate.postForEntity("/api/registry/register", serviceC, String.class);

        // 2. Verify it exists
        List<ServiceInstance> instances = serviceRegistry.getInstances("service-c");
        assertThat(instances).isNotEmpty();
        ServiceInstance registeredInstance = instances.get(0);

        // 3. Simulate time passing by manually setting the last heartbeat to the past (40 seconds ago)
        // Default threshold is 30 seconds
        registeredInstance.setLastHeartbeat(Instant.now().minusSeconds(40));

        // 4. Trigger cleanup manually to avoid waiting for the scheduled task
        serviceRegistry.removeStaleInstances();

        // 5. Verify it is gone
        assertThat(serviceRegistry.getInstances("service-c")).isEmpty();

        // 6. Register again
        restTemplate.postForEntity("/api/registry/register", serviceC, String.class);
        assertThat(serviceRegistry.getInstances("service-c")).isNotEmpty();

        // 7. Send heartbeat
        restTemplate.postForEntity(
                "/api/registry/heartbeat?serviceId=service-c&host=localhost&port=9090",
                null,
                String.class
        );

        // 8. Verify heartbeat updated (we can check if the time is recent)
        instances = serviceRegistry.getInstances("service-c");
        assertThat(instances).isNotEmpty();
        assertThat(instances.get(0).getLastHeartbeat()).isAfter(Instant.now().minusSeconds(5));
    }
}
