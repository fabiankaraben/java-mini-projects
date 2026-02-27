package com.example.jpms.web;

import com.example.jpms.model.Product;
import com.example.jpms.service.ProductService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WebServerTest {

    @Test
    void testWebServerInit() {
        ProductService dummyService = new ProductService() {
            @Override
            public List<Product> getAllProducts() { return Collections.emptyList(); }
            @Override
            public Optional<Product> getProductById(String id) { return Optional.empty(); }
            @Override
            public Product createProduct(Product product) { return product; }
            @Override
            public boolean deleteProduct(String id) { return false; }
        };

        WebServer server = new WebServer(dummyService);
        assertNotNull(server);
    }
}
