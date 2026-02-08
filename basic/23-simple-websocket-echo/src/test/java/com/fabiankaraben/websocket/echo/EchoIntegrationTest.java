package com.fabiankaraben.websocket.echo;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.glassfish.tyrus.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EchoIntegrationTest {

    private static Server server;

    @BeforeAll
    public static void startServer() throws Exception {
        server = new Server("localhost", 8025, "/ws", null, EchoEndpoint.class);
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testEcho() throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:8025/ws/echo";
        final CountDownLatch latch = new CountDownLatch(1);
        final String messageToSend = "Hello WebSocket";
        final StringBuilder receivedMessage = new StringBuilder();

        Client client = new Client(latch, receivedMessage);
        Session session = container.connectToServer(client, URI.create(uri));

        session.getBasicRemote().sendText(messageToSend);
        
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "Timeout waiting for message echo");
        assertEquals(messageToSend, receivedMessage.toString());
        
        session.close();
    }

    @ClientEndpoint
    public static class Client {
        private final CountDownLatch latch;
        private final StringBuilder receivedMessage;

        public Client(CountDownLatch latch, StringBuilder receivedMessage) {
            this.latch = latch;
            this.receivedMessage = receivedMessage;
        }

        @OnMessage
        public void onMessage(String message) {
            System.out.println("Client received: " + message);
            receivedMessage.append(message);
            latch.countDown();
        }
    }
}
