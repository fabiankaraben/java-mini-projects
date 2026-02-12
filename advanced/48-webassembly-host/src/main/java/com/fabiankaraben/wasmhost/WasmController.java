package com.fabiankaraben.wasmhost;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/wasm")
public class WasmController {

    private final WasmService wasmService;

    public WasmController(WasmService wasmService) {
        this.wasmService = wasmService;
    }

    @GetMapping("/add")
    public Map<String, Object> add(@RequestParam int a, @RequestParam int b) {
        int result = wasmService.add(a, b);
        return Map.of(
                "operation", "add",
                "a", a,
                "b", b,
                "result", result
        );
    }
}
