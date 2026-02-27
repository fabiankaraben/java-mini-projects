package com.example.jpms.web;

import com.example.jpms.model.Product;
import com.example.jpms.service.ProductService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WebServer {

    private final ProductService productService;
    private HttpServer server;

    public WebServer(ProductService productService) {
        this.productService = productService;
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/products", new ProductHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    private class ProductHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            exchange.getResponseHeaders().add("Content-Type", "application/json");

            try {
                if ("GET".equals(method)) {
                    if (path.equals("/products") || path.equals("/products/")) {
                        handleGetAll(exchange);
                    } else {
                        handleGetOne(exchange, path);
                    }
                } else if ("POST".equals(method)) {
                    handlePost(exchange);
                } else if ("DELETE".equals(method)) {
                    handleDelete(exchange, path);
                } else {
                    sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
            }
        }

        private void handleGetAll(HttpExchange exchange) throws IOException {
            List<Product> products = productService.getAllProducts();
            String json = "[" + products.stream().map(this::toJson).collect(Collectors.joining(",")) + "]";
            sendResponse(exchange, 200, json);
        }

        private void handleGetOne(HttpExchange exchange, String path) throws IOException {
            String id = path.substring("/products/".length());
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                sendResponse(exchange, 200, toJson(product.get()));
            } else {
                sendResponse(exchange, 404, "{\"error\": \"Not found\"}");
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            Product parsed = fromJson(body);
            Product created = productService.createProduct(parsed);
            sendResponse(exchange, 201, toJson(created));
        }

        private void handleDelete(HttpExchange exchange, String path) throws IOException {
            String id = path.substring("/products/".length());
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                sendResponse(exchange, 204, "");
            } else {
                sendResponse(exchange, 404, "{\"error\": \"Not found\"}");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            // In HttpExchange, 204 requires length -1 instead of 0
            if (statusCode == 204) {
                exchange.sendResponseHeaders(statusCode, -1);
            } else {
                exchange.sendResponseHeaders(statusCode, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            }
        }

        private String toJson(Product p) {
            return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"price\":%s}",
                    p.getId() != null ? p.getId() : "",
                    p.getName() != null ? p.getName() : "",
                    p.getPrice());
        }

        private Product fromJson(String json) {
            // Very simplistic JSON parsing for mini-project.
            // Assuming format {"name":"...","price":...}
            String name = "";
            double price = 0.0;
            String id = null;

            json = json.replaceAll("[{}\"]", "").trim();
            String[] parts = json.split(",");
            for (String part : parts) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    String k = kv[0].trim();
                    String v = kv[1].trim();
                    if ("name".equals(k)) {
                        name = v;
                    } else if ("price".equals(k)) {
                        price = Double.parseDouble(v);
                    } else if ("id".equals(k)) {
                        id = v;
                    }
                }
            }
            return new Product(id, name, price);
        }
    }
}
