package com.example.jpms.app;

import com.example.jpms.service.ProductService;
import com.example.jpms.web.WebServer;

import java.util.ServiceLoader;

public class Application {
    public static void main(String[] args) {
        System.out.println("Starting Modular Backend...");

        // Use ServiceLoader to discover ProductService implementation
        ServiceLoader<ProductService> loader = ServiceLoader.load(ProductService.class);
        ProductService productService = loader.findFirst()
                .orElseThrow(() -> new RuntimeException("No ProductService implementation found! Ensure the service module provides it."));

        System.out.println("Discovered ProductService implementation: " + productService.getClass().getName());

        WebServer server = new WebServer(productService);
        try {
            int port = 8080;
            server.start(port);
            System.out.println("Server running on http://localhost:" + port);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start server");
            System.exit(1);
        }
    }
}
