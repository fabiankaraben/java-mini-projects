package com.example.consul;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.consul.ConsulContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ConsulMicroserviceApplicationTests {

    @Container
    static ConsulContainer consulContainer = new ConsulContainer("consul:1.15")
            .withConsulCommand("kv put config/consul-microservice/testKey testValue");

    @DynamicPropertySource
    static void consulProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.consul.host", consulContainer::getHost);
        registry.add("spring.cloud.consul.port", consulContainer::getFirstMappedPort);
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(discoveryClient).isNotNull();
    }

    @Test
    void serviceShouldRegisterWithConsul() throws InterruptedException {
        // Wait for registration
        int attempts = 0;
        List<ServiceInstance> instances = List.of();
        while (attempts < 10 && instances.isEmpty()) {
            Thread.sleep(1000);
            instances = discoveryClient.getInstances("consul-microservice");
            attempts++;
        }

        assertThat(instances).isNotEmpty();
        ServiceInstance instance = instances.get(0);
        assertThat(instance.getServiceId()).isEqualTo("consul-microservice");
    }

    @Test
    void healthCheckShouldBeUp() {
        String health = restTemplate.getForObject("/actuator/health", String.class);
        assertThat(health).contains("UP");
    }
}
