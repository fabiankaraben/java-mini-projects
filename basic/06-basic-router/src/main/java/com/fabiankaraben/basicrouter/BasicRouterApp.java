package com.fabiankaraben.basicrouter;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class BasicRouterApp {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        Router router = new Router();

        // Define routes
        router.addRoute("GET", "/users", (exchange) -> {
            String response = "List of users";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });
        
        router.addRoute("POST", "/users", (exchange) -> {
            String response = "User created";
            exchange.sendResponseHeaders(201, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });
        
        router.addRoute("DELETE", "/users", (exchange) -> {
            String response = "User deleted";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        });

        server.createContext("/", router);
        server.setExecutor(null);
        System.out.println("Server started on port " + PORT);
        server.start();
    }
}
