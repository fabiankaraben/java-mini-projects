package com.example.jpms.service.impl;

import com.example.jpms.model.Product;
import com.example.jpms.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProductServiceImpl implements ProductService {
    private final ConcurrentMap<String, Product> products = new ConcurrentHashMap<>();

    public ProductServiceImpl() {
        // Add some initial data
        Product p1 = new Product("1", "Laptop", 1200.0);
        Product p2 = new Product("2", "Mouse", 25.0);
        products.put(p1.getId(), p1);
        products.put(p2.getId(), p2);
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Product createProduct(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public boolean deleteProduct(String id) {
        return products.remove(id) != null;
    }
}
