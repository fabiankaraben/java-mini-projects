package com.example.errorhandling.controller;

import com.example.errorhandling.exception.InvalidInputException;
import com.example.errorhandling.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/{id}")
    public String getProduct(@PathVariable String id) {
        if ("error".equals(id)) {
            throw new RuntimeException("Simulated unexpected error");
        }
        if (Integer.parseInt(id) < 0) {
            throw new InvalidInputException("Product ID cannot be negative");
        }
        if ("999".equals(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        return "Product " + id;
    }
}
