package com.example.multitenant;

import com.example.multitenant.model.Product;
import com.example.multitenant.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class MultiTenantIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testTenantIsolation() throws Exception {
        // Create product for Tenant A
        mockMvc.perform(post("/api/products")
                .header("X-Tenant-ID", "tenant-a")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Product A\", \"price\": 10.0}"))
                .andExpect(status().isOk());

        // Create product for Tenant B
        mockMvc.perform(post("/api/products")
                .header("X-Tenant-ID", "tenant-b")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Product B\", \"price\": 20.0}"))
                .andExpect(status().isOk());

        // Verify Tenant A sees only Product A
        mockMvc.perform(get("/api/products")
                .header("X-Tenant-ID", "tenant-a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Product A"));

        // Verify Tenant B sees only Product B
        mockMvc.perform(get("/api/products")
                .header("X-Tenant-ID", "tenant-b"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Product B"));
    }

    @Test
    void testMissingTenantHeader() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isBadRequest());
    }
}
