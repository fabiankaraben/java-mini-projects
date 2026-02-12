# Distributed Rate Limiter

This project is a **Distributed Rate Limiter** backend service implemented in Java using **Spring Boot** and **Redis Lua Scripts**. It implements the **Sliding Window Log** algorithm to provide precise and atomic rate limiting across multiple application instances.

## 🚀 Features

-   **Distributed State**: Uses Redis as a shared data store, ensuring rate limits are enforced globally across all running instances.
-   **Sliding Window Log**: Implements a sliding window algorithm using Redis `ZSET` (Sorted Sets) for accurate time-based limiting.
-   **Atomic Operations**: Uses Lua scripts to ensure read-modify-write operations on Redis are atomic, preventing race conditions.
-   **Configurable Limits**: Clients can request specific limits and window sizes (for demonstration purposes).

## 📋 Requirements

-   Java 17+
-   Docker & Docker Compose (for running Redis and the application)

## 🛠️ Tech Stack

-   **Language**: Java 17
-   **Framework**: Spring Boot 3.4.1
-   **Database**: Redis (Alpine)
-   **Build Tool**: Gradle
-   **Testing**: JUnit 5, Testcontainers

## 📦 How to Run

The easiest way to run the application is using Docker Compose, which sets up both the application and the Redis container.

1.  **Build and Start:**
    ```bash
    docker compose up --build
    ```
    The application will be available at `http://localhost:8080`.

2.  **Stop:**
    ```bash
    docker compose down
    ```

## 🧪 Testing

### Running Automated Tests

To run the integration tests (which include a distributed load simulation using Testcontainers):

```bash
./gradlew clean test
```

*Note: You need a Docker environment available for Testcontainers to work.*

### Manual Testing with Curl

You can test the rate limiter API using `curl`. The endpoint is `/api/resource`.

**Parameters:**
-   `limit`: Max requests allowed (default: 5)
-   `windowMs`: Time window in milliseconds (default: 10000)
-   Header `X-Client-Id`: Unique identifier for the client (default: "default")

**Example 1: Allowed Requests**
Send 5 requests in quick succession. They should all return 200 OK.
```bash
for i in {1..5}; do curl -i -H "X-Client-Id: user1" "http://localhost:8080/api/resource?limit=5&windowMs=10000"; echo; done
```

**Example 2: Rate Limited Request**
Send a 6th request within the window. It should return 429 Too Many Requests.
```bash
curl -i -H "X-Client-Id: user1" "http://localhost:8080/api/resource?limit=5&windowMs=10000"
```

**Output:**
```
HTTP/1.1 429 Too Many Requests
Content-Type: text/plain;charset=UTF-8
Content-Length: 36

Rate limit exceeded. Try again later.
```

## 📂 Project Structure

-   `src/main/resources/scripts/rate_limiter.lua`: The Lua script implementing the Sliding Window Log logic in Redis.
-   `src/main/java/com/example/ratelimiter/service/RateLimiterService.java`: Service that executes the Lua script.
-   `src/test/java/com/example/ratelimiter/RateLimiterIntegrationTest.java`: Integration tests verifying the logic under concurrent load.
