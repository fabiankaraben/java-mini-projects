package com.example.search;

import com.example.search.model.Product;
import com.example.search.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class SearchApplicationTests {

    @Container
    static ElasticsearchContainer elasticsearch = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.15.3")
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false")
            .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m");

    @DynamicPropertySource
    static void elasticsearchProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticsearch::getHttpHostAddress);
    }

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testIndexAndSearch() {
        Product p1 = new Product(UUID.randomUUID().toString(), "Gaming Laptop", "High performance laptop", 1500.00);
        Product p2 = new Product(UUID.randomUUID().toString(), "Office Laptop", "Reliable office laptop", 800.00);
        Product p3 = new Product(UUID.randomUUID().toString(), "Gaming Mouse", "Precision mouse", 50.00);

        productRepository.saveAll(List.of(p1, p2, p3));

        List<Product> gamingProducts = productRepository.findByNameContaining("Gaming");
        assertThat(gamingProducts).hasSize(2);
        assertThat(gamingProducts).extracting(Product::getName).contains("Gaming Laptop", "Gaming Mouse");

        List<Product> officeProducts = productRepository.findByDescriptionContaining("office");
        assertThat(officeProducts).hasSize(1);
        assertThat(officeProducts.get(0).getName()).isEqualTo("Office Laptop");
    }
}
