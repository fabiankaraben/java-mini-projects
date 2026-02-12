package com.fabiankaraben.wasmhost;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WasmServiceTest {

    @Autowired
    private WasmService wasmService;

    @Test
    void testAdd() {
        int result = wasmService.add(5, 7);
        Assertions.assertEquals(12, result, "5 + 7 should equal 12");
    }

    @Test
    void testAddNegativeNumbers() {
        int result = wasmService.add(-10, 5);
        Assertions.assertEquals(-5, result, "-10 + 5 should equal -5");
    }
}
