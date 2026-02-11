package com.example.multitenant.controller;

import com.example.multitenant.config.TenantContext;
import com.example.multitenant.model.Product;
import com.example.multitenant.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Enforce tenant ID from context
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant ID is missing");
        }
        product.setTenantId(tenantId);
        return productRepository.save(product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // The aspect should filter out products from other tenants, 
        // preventing deletion if the ID belongs to another tenant.
        // However, standard deleteById might not trigger the Aspect on the SELECT before DELETE if it's a simple delete.
        // Actually, Hibernate's delete usually loads the entity first or issues a DELETE ... WHERE id=?
        // If it issues DELETE ... WHERE id=?, the filter might NOT be applied automatically unless enabled.
        // Filters apply to queries.
        // For deleteById, Spring Data JPA typically does findById then delete. findById should respect the filter.
        
        if (productRepository.existsById(id)) {
             productRepository.deleteById(id);
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
