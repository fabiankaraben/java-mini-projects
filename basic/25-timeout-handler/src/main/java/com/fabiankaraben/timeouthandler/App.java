package com.fabiankaraben.timeouthandler;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;

public class App {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Define a timeout of 2 seconds
        Duration timeout = Duration.ofSeconds(2);

        // Handler that sleeps for 5 seconds (will timeout)
        server.createContext("/slow", new TimeoutHandler(new SlowHandler(5000), timeout));
        
        // Handler that sleeps for 1 second (will pass)
        server.createContext("/fast", new TimeoutHandler(new SlowHandler(1000), timeout));

        server.setExecutor(null); // creates a default executor
        server.start();
        
        System.out.println("Server started on port " + PORT);
    }
}
