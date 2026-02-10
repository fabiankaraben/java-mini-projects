# Reverse Proxy with Custom Logic

This mini-project implements a Reverse Proxy using **Spring Cloud Gateway**. It demonstrates how to create custom routing rules and filters to modify requests and responses.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional, for running the full stack)

## Features

- **Custom Header Filter**: Adds a `X-Custom-Header` with value `Gateway-Processed` to requests matching the service route.
- **Route Configuration**:
  - `/api/service/**` -> Forwards to an upstream service (default: `http://localhost:8081` or `upstream-service` in Docker).
    - Strips the prefix `/api/service` (2 segments).
  - `/echo/**` -> Forwards to `https://postman-echo.com`.
    - Strips the prefix `/echo` (1 segment).
    - Adds a response header `X-Echo-Response: True`.

## Project Structure

- `src/main/java/.../filter/CustomHeaderFilter.java`: Custom `GatewayFilterFactory` that adds a header to the request.
- `src/main/java/.../config/GatewayConfig.java`: Java-based configuration of routes using `RouteLocator`.
- `src/test/java/.../ReverseProxyIntegrationTest.java`: Integration tests using **WireMock** to simulate upstream services.

## How to Run

### Using Maven (Local)

1. Build the project:
   ```bash
   mvn clean package
   ```
2. Run the application:
   ```bash
   java -jar target/reverse-proxy-service-0.0.1-SNAPSHOT.jar
   ```

Note: You will need a service running on port 8081 for the `/api/service` route to work, or you can update `src/main/resources/application.yml`.

### Using Docker Compose

This will start the reverse proxy and a dummy upstream service (`whoami`).

```bash
docker compose up --build
```

The proxy will be available at `http://localhost:8080`.

## Usage Examples (curl)

### 1. Service Route
Forwards to the upstream service.

```bash
curl -v http://localhost:8080/api/service/hello
```

**Expected Output:**
You should see the output from the upstream service (e.g., `whoami` details) and the logs should show the `X-Custom-Header` was added.

### 2. Echo Route
Forwards to Postman Echo.

```bash
curl -v http://localhost:8080/echo/get?foo=bar
```

**Expected Output:**
- Response from Postman Echo (JSON).
- Response header `X-Echo-Response: True`.

## Testing

This project uses **WireMock** for integration testing to verify routing rules and header modifications without needing real upstream services.

Run the tests with:

```bash
mvn clean test
```

The tests verify:
1. Requests to `/api/service/**` are forwarded to the WireMock server with the `X-Custom-Header`.
2. Requests to `/echo/**` are forwarded and the response contains `X-Echo-Response`.
