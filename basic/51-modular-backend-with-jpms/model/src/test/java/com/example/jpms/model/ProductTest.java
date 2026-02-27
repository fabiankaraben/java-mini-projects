package com.example.jpms.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product p = new Product("1", "Laptop", 999.99);
        assertEquals("1", p.getId());
        assertEquals("Laptop", p.getName());
        assertEquals(999.99, p.getPrice());
    }

    @Test
    void testProductEquality() {
        Product p1 = new Product("1", "Laptop", 999.99);
        Product p2 = new Product("1", "Laptop", 999.99);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
