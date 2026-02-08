package com.fabiankaraben.inmemorydatastore;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        DataStore dataStore = new DataStore();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        server.createContext("/items", new StoreHandler(dataStore));
        server.setExecutor(null); // creates a default executor
        
        System.out.println("Server started on port " + PORT);
        server.start();
    }
}
