# Reactive Microservices

This mini-project demonstrates a reactive microservices backend using **Spring WebFlux** and **RSocket**. It showcases different RSocket interaction models (Request-Response, Fire-and-Forget, Request-Stream, Channel) and how to consume them. It also includes a REST adapter (`WebRestController`) to easily test the RSocket endpoints using HTTP tools like `curl`.

## Requirements

- Java 17+
- Docker (optional, for containerization)
- Maven

## Features

- **Spring WebFlux**: Reactive web stack.
- **RSocket**: Binary protocol for reactive streams with backpressure.
- **Interaction Models**:
  - Request-Response
  - Fire-and-Forget
  - Request-Stream
  - Channel (Bi-directional)
- **Integration Tests**: Using `StepVerifier` to verify reactive flows.

## How to Run

### Using Maven

```bash
mvn clean spring-boot:run
```

The application will start on port `8080` (HTTP) and `7000` (RSocket).

### Using Docker

```bash
docker compose up --build
```

## Usage

Since RSocket uses a binary protocol, we've provided a REST controller that proxies requests to the RSocket server internally.

### 1. Request-Response
Sends a message and expects a single response.

```bash
curl -X POST http://localhost:8080/api/request-response \
  -H "Content-Type: application/json" \
  -d '{"sender": "User", "content": "Hello World"}'
```

### 2. Fire-and-Forget
Sends a message without waiting for a response (returns immediately).

```bash
curl -X POST http://localhost:8080/api/fire-and-forget \
  -H "Content-Type: application/json" \
  -d '{"sender": "User", "content": "Ignore me"}'
```

### 3. Request-Stream
Sends a message and receives a stream of responses (Server-Sent Events).

```bash
curl -X POST http://localhost:8080/api/request-stream \
  -H "Content-Type: application/json" \
  -d '{"sender": "User", "content": "Start Streaming"}'
```

### 4. Channel
Bi-directional streaming (simulated via REST by sending a stream of inputs).

```bash
curl -X POST http://localhost:8080/api/channel \
  -H "Content-Type: application/json" \
  -d '{"sender": "User", "content": "Channel Init"}'
```

## Testing

To run the integration tests using `StepVerifier`:

```bash
mvn clean test
```
