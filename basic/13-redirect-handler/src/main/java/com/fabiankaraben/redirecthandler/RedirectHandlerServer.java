package com.fabiankaraben.redirecthandler;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RedirectHandlerServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // Create the server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Create a context for the path "/redirect" that uses our RedirectHandler
        // Redirecting to example.com with 302 Found status
        server.createContext("/redirect", new RedirectHandler("https://www.example.com", 302));
        
        // Also adding a 301 Moved Permanently example
        server.createContext("/moved", new RedirectHandler("https://www.example.com/new-location", 301));

        server.setExecutor(null); // Use the default executor
        server.start();

        System.out.println("Server started on port " + PORT);
        System.out.println("Try: http://localhost:" + PORT + "/redirect (302)");
        System.out.println("Try: http://localhost:" + PORT + "/moved (301)");
    }
}
