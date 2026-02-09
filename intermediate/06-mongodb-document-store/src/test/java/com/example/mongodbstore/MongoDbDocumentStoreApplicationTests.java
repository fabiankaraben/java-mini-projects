package com.example.mongodbstore;

import com.example.mongodbstore.model.Product;
import com.example.mongodbstore.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class MongoDbDocumentStoreApplicationTests {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testDocumentInsertionAndRetrieval() {
        // Given
        Product product = new Product("Test Product", "Test Description", 99.99);

        // When
        Product savedProduct = productRepository.save(product);

        // Then
        assertThat(savedProduct.getId()).isNotNull();
        
        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get().getName()).isEqualTo("Test Product");
        assertThat(retrievedProduct.get().getDescription()).isEqualTo("Test Description");
        assertThat(retrievedProduct.get().getPrice()).isEqualTo(99.99);
    }

    @Test
    void testFindAll() {
        // Given
        Product p1 = new Product("P1", "D1", 10.0);
        Product p2 = new Product("P2", "D2", 20.0);
        productRepository.saveAll(List.of(p1, p2));

        // When
        List<Product> products = productRepository.findAll();

        // Then
        assertThat(products).hasSize(2);
    }
}
