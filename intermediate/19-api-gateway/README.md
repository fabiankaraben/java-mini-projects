# API Gateway

This project is a mini-project demonstrating a **Spring Cloud Gateway** implementation with **Redis-based Rate Limiting**.

It acts as an entry point for backend services, routing requests and enforcing rate limits to protect downstream services from being overwhelmed.

## Requirements

- Java 17+
- Maven
- Docker and Docker Compose

## Features

- **API Gateway**: Routes requests to backend services.
- **Rate Limiting**: Uses Redis to implement the Token Bucket algorithm. Limits requests based on the client's IP address.
- **Integration Testing**: Uses **WireMock** to simulate backend services and **Testcontainers** for a real Redis instance during tests.

## Configuration

The gateway is configured to route requests matching `/api/**` to a backend service.

**Rate Limit Rules (Default):**
- Replenish Rate: 1 request/second
- Burst Capacity: 2 requests
- Key Resolver: IP Address

## How to Run

### Using Docker Compose (Recommended)

Since this project requires Redis, the easiest way to run it is using Docker Compose. We also spin up a WireMock instance to act as a mock backend service.

1. Build and start the services:
   ```bash
   docker compose up --build
   ```

2. The Gateway will be available at `http://localhost:8080`.
   The Mock Backend is accessible internally by the Gateway.

### Testing the Gateway

You can use `curl` to send requests.

**1. Successful Request:**

The mock backend is configured to respond to any request (via WireMock default behavior or mapped mappings).

```bash
# First create a mapping in the mock backend (optional, if you want specific responses)
# By default, WireMock might return 404 if not configured, but let's assume we hit an endpoint.
# You can add mappings to the ./wiremock/mappings directory if you want persistent mocks.
# For now, let's try to hit the gateway.
```

If you just run it, the WireMock container starts empty. You can register a stub on the mock backend (exposed on port 8081 on host) or rely on 404s which still prove routing works.

To verify rate limiting, send multiple requests quickly:

```bash
# Request 1 (Success)
curl -i http://localhost:8080/api/test

# Request 2 (Success - Burst)
curl -i http://localhost:8080/api/test

# Request 3 (Success - Burst)
curl -i http://localhost:8080/api/test

# Request 4 (Should be 429 Too Many Requests)
curl -i http://localhost:8080/api/test
```

*Note: You might receive 404 Not Found from the backend if no mapping exists, but the headers will show if it went through the gateway. If Rate Limiting hits, you will see `HTTP/1.1 429 Too Many Requests`.*

## Running Tests

Integration tests use **Testcontainers** (for Redis) and **WireMock** (for backend).

Run the tests using Maven:

```bash
mvn clean test
```

The tests verify:
1. Requests are correctly routed to the backend.
2. Rate limiting headers are present and limits are enforced.
