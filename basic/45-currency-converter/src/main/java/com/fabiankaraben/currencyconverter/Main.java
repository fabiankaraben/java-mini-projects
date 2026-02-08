package com.fabiankaraben.currencyconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final int PORT = 8080;
    // Using a free API that supports base currency
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest"; 

    public static void main(String[] args) throws IOException {
        CurrencyConverter converter = new CurrencyConverter(API_URL);
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        server.createContext("/convert", new ConvertHandler(converter));
        server.setExecutor(null); // creates a default executor
        
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class ConvertHandler implements HttpHandler {
        private final CurrencyConverter converter;
        private final ObjectMapper objectMapper = new ObjectMapper();

        public ConvertHandler(CurrencyConverter converter) {
            this.converter = converter;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);

            String from = params.get("from");
            String to = params.get("to");
            String amountStr = params.get("amount");

            if (from == null || to == null || amountStr == null) {
                sendResponse(exchange, 400, "Missing parameters: from, to, amount");
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                double result = converter.convert(from.toUpperCase(), to.toUpperCase(), amount);
                
                Map<String, Object> response = new HashMap<>();
                response.put("from", from);
                response.put("to", to);
                response.put("amount", amount);
                response.put("convertedAmount", result);

                String jsonResponse = objectMapper.writeValueAsString(response);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                sendResponse(exchange, 200, jsonResponse);
                
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "Invalid amount format");
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, 400, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] bytes = response.getBytes();
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private Map<String, String> parseQuery(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null) return result;
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }
}
