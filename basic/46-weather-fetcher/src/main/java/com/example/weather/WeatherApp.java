package com.example.weather;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class WeatherApp {

    private static final int PORT = 8080;
    // Open-Meteo API endpoint
    private static final String DEFAULT_API_URL = "https://api.open-meteo.com/v1/forecast";

    public static void main(String[] args) throws IOException {
        WeatherService weatherService = new WeatherService(DEFAULT_API_URL);
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        server.createContext("/weather", new WeatherHandler(weatherService));
        server.setExecutor(null); // creates a default executor
        
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class WeatherHandler implements HttpHandler {
        private final WeatherService weatherService;

        public WeatherHandler(WeatherService weatherService) {
            this.weatherService = weatherService;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            // Parse query params
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = queryToMap(query);
            
            double lat = 52.52; // Default Berlin
            double lon = 13.41;
            
            try {
                if (params.containsKey("lat")) {
                    lat = Double.parseDouble(params.get("lat"));
                }
                if (params.containsKey("lon")) {
                    lon = Double.parseDouble(params.get("lon"));
                }
                
                String weatherData = weatherService.getWeather(lat, lon);
                
                // Set content type to JSON
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                sendResponse(exchange, 200, weatherData);
                
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, "Invalid latitude or longitude format");
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
        
        private Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null) {
                return result;
            }
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
