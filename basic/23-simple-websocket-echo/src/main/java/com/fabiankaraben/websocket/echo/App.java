package com.fabiankaraben.websocket.echo;

import org.glassfish.tyrus.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) {
        Server server = new Server("localhost", 8025, "/ws", null, EchoEndpoint.class);

        try {
            server.start();
            System.out.println("WebSocket server started at ws://localhost:8025/ws/echo");
            System.out.println("Press any key to stop the server...");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
