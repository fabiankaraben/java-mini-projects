package com.fabiankaraben.xmlapi;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class XmlResponseApi {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/user", new UserHandler());
        server.setExecutor(null);
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class UserHandler implements HttpHandler {
        private final XmlMapper xmlMapper = new XmlMapper();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                User user = new User("1", "John Doe", "john.doe@example.com");
                
                String response = xmlMapper.writeValueAsString(user);
                
                exchange.getResponseHeaders().set("Content-Type", "application/xml");
                exchange.sendResponseHeaders(200, response.length());
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }
}
