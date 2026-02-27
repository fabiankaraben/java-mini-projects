package com.example.jpms.service;

import com.example.jpms.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(String id);
    Product createProduct(Product product);
    boolean deleteProduct(String id);
}
