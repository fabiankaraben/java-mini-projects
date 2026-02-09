# Rate Limiting Service

This is a backend mini-project in Java using **Spring Boot** that implements a token bucket rate limiting service using **Redis**.

## Description

The service provides an API endpoint that is rate-limited. It uses a custom Lua script executed in Redis to atomically manage the token bucket algorithm. This ensures that the rate limits are enforced correctly even in a distributed environment with high concurrency.

- **Algorithm**: Token Bucket
- **Rate Limit**: 10 requests per second
- **Burst Capacity**: 20 requests

## Requirements

- Java 17+
- Docker and Docker Compose (for running the application and Redis)
- Maven (for building locally if needed)

## Project Structure

- `src/main/java`: Spring Boot application code
- `src/main/resources/scripts`: Redis Lua scripts
- `src/test/java`: Integration tests using Testcontainers
- `Dockerfile`: Multi-stage Docker build definition
- `docker-compose.yml`: Docker Compose configuration to run the app and Redis

## How to Run

The easiest way to run the application is using Docker Compose.

1. **Start the application and Redis:**

   ```bash
   docker compose up --build
   ```

2. **Access the API:**

   The application runs on port `8080`.

## Usage Examples

You can test the rate limiting using `curl`.

### 1. Successful Request

```bash
curl -i -H "X-User-Id: user123" http://localhost:8080/api/resource
```

**Response:**
```
HTTP/1.1 200 OK
...
Request allowed
```

### 2. Rate Limited Request

If you exceed the rate limit (more than 20 burst or >10 req/s sustained), you will receive a 429 status code.

```bash
# Run this in a loop or verify manually after spamming requests
curl -i -H "X-User-Id: user123" http://localhost:8080/api/resource
```

**Response:**
```
HTTP/1.1 429 Too Many Requests
...
Too many requests
```

## Testing

The project includes integration tests that use **Testcontainers** to spin up a Redis container and simulate high concurrency.

To run the tests, execute:

```bash
mvn clean test
```

The tests will:
1. Start a Redis container.
2. Spin up the Spring Boot context.
3. Simulate concurrent requests to the API.
4. Verify that requests are allowed up to the limit and rejected afterwards.
