package com.example.search.controller;

import com.example.search.model.Product;
import com.example.search.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        return productRepository.save(product);
    }

    @GetMapping
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String query) {
        return productRepository.findByNameContaining(query);
    }
}
