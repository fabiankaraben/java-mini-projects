package com.fabiankaraben.websocket.echo;

import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class EchoEndpoint {

    @OnMessage
    public String onMessage(String message, Session session) {
        System.out.println("Received: " + message);
        return message;
    }
}
