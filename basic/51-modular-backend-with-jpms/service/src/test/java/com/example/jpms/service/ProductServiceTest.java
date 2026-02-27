package com.example.jpms.service;

import com.example.jpms.model.Product;
import com.example.jpms.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById() {
        Optional<Product> p = productService.getProductById("1");
        assertTrue(p.isPresent());
        assertEquals("Laptop", p.get().getName());
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product(null, "Keyboard", 50.0);
        Product created = productService.createProduct(newProduct);
        assertNotNull(created.getId());
        assertEquals("Keyboard", created.getName());
        assertEquals(3, productService.getAllProducts().size());
    }

    @Test
    void testDeleteProduct() {
        boolean deleted = productService.deleteProduct("1");
        assertTrue(deleted);
        assertEquals(1, productService.getAllProducts().size());
    }
}
