# Custom Load Balancer

A high-performance TCP/HTTP Load Balancer implemented in Java using Netty. It supports the Round-Robin algorithm to distribute traffic across multiple backend servers.

## Features
- **Netty-based**: Uses non-blocking I/O for high throughput.
- **Round-Robin Strategy**: Evenly distributes incoming connections/requests to backends.
- **TCP Proxying**: Forwards raw TCP traffic (works for HTTP, etc.).
- **Dockerized**: Easy to deploy with Docker Compose.

## Requirements
- Java 17+
- Gradle 8.x (wrapper included)
- Docker & Docker Compose (optional, for running the full stack)

## Project Structure
- `src/main/java`: Source code.
  - `LoadBalancerApplication`: Main entry point.
  - `FrontendHandler` & `BackendHandler`: Netty handlers for proxying traffic.
  - `strategy/`: Load balancing algorithms (Round Robin).
- `src/test/java`: Integration tests using embedded Netty servers.

## How to Run

### Using Gradle (Local)
1. **Build and Run**:
   ```bash
   ./gradlew run
   ```
   By default, it listens on port 8080 and forwards to `localhost:8081` and `localhost:8082`. You will need to have services running on those ports.

2. **Run Tests**:
   ```bash
   ./gradlew clean test
   ```
   This will run integration tests that spin up mock backend servers and verify that the load balancer correctly distributes requests.

### Using Docker Compose
This is the recommended way to see it in action, as it sets up the load balancer and two sample backend services.

1. **Build and Start**:
   ```bash
   docker compose up --build
   ```

2. **Test with Curl**:
   Open a terminal and send multiple requests to the load balancer (exposed on port 8080):
   ```bash
   for i in {1..10}; do curl http://localhost:8080; echo; done
   ```

   **Expected Output**:
   You should see responses alternating between the two backends:
   ```
   Hello from Backend 1
   Hello from Backend 2
   Hello from Backend 1
   Hello from Backend 2
   ...
   ```

## Configuration
You can configure the load balancer using environment variables:
- `LB_PORT`: The port the load balancer listens on (default: 8080).
- `BACKEND_SERVERS`: Comma-separated list of backend servers in `host:port` format (e.g., `backend1:80,backend2:80`).
