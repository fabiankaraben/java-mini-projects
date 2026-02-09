package com.fabiankaraben.sitemap;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SitemapApp {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/sitemap.xml", new SitemapHandler());
        server.createContext("/", new RootHandler());

        server.setExecutor(null);
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Welcome to the Sitemap Generator! Visit /sitemap.xml to see the sitemap.";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class SitemapHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            SitemapGenerator generator = new SitemapGenerator("http://localhost:8080");
            generator.addPage("/");
            generator.addPage("/about");
            generator.addPage("/contact");
            generator.addPage("/products");

            String response = generator.generateXml();
            exchange.getResponseHeaders().set("Content-Type", "application/xml");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
