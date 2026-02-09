# WebSocket Chat Server

This is a backend mini-project built with Java and Spring Boot that implements a real-time chat server using WebSockets and Redis Pub/Sub.

## Overview

The application allows users to send and receive messages in real-time. It uses Spring WebSocket (STOMP over WebSocket) for real-time communication between the client and the server. Redis is used as a message broker to broadcast messages to all connected clients, enabling scalability across multiple server instances (though only one is configured here).

## Requirements

-   Java 17 or higher
-   Docker (for running Redis and integration tests)
-   Maven

## Features

-   **WebSocket Support**: Real-time bidirectional communication.
-   **STOMP Protocol**: Simple Text Oriented Messaging Protocol for message handling.
-   **Redis Pub/Sub**: Uses Redis to publish messages to a channel and subscribe to it, ensuring messages are distributed.
-   **Integration Testing**: Validates the full flow using Testcontainers.

## Setup & Running

### 1. Start the Infrastructure (Redis)

The application requires Redis. You can start it using Docker Compose:

```bash
docker compose up -d
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on port `8080`.

## Usage

This server is designed to be consumed by a STOMP client (e.g., a frontend using `sockjs-client` and `stompjs`).

**WebSocket Endpoint**: `ws://localhost:8080/ws`
**Subscription Destination**: `/topic/public`
**Send Destination**: `/app/chat.sendMessage`

### Example cURL (Simulated)

WebSockets are persistent connections, so you can't easily use standard `curl` to send a message. However, since we are using STOMP, clients usually send a CONNECT frame, then SUBSCRIBE, then SEND.

To test manually without a frontend, you can use a CLI tool like `wscat` or a dedicated WebSocket client.

**Connecting with `wscat` (if installed):**
```bash
wscat -c ws://localhost:8080/ws
```
*Note: This endpoint uses SockJS fallback by default, so direct raw WebSocket connection might need the specific websocket transport url like `ws://localhost:8080/ws/websocket`.*

## Testing

The project includes integration tests that use **Testcontainers** to spin up a Redis instance automatically.

To run the tests:

```bash
./mvnw clean test
```

The tests will:
1.  Start a Redis container.
2.  Start the Spring Boot application on a random port.
3.  Connect a WebSocket STOMP client.
4.  Subscribe to `/topic/public`.
5.  Send a message to `/app/chat.sendMessage`.
6.  Verify that the message is received back via the subscription.
