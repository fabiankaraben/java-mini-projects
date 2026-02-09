# Redis Cache Layer

This mini-project demonstrates a backend in Java using **Spring Boot** that utilizes **Redis** for caching responses to improve performance.

## 📋 Requirements

-   Java 17 or higher
-   Docker (for running the application via Docker Compose and for integration tests)
-   Gradle (wrapper included)

## 🚀 How to Run

### Using Docker Compose (Recommended)

To run the entire application (Spring Boot app + Redis) in Docker:

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Local Development

If you want to run the application locally, you need a running Redis instance on `localhost:6379`. You can start one using Docker:

```bash
docker run -d -p 6379:6379 redis:7.2-alpine
```

Then run the application with Gradle:

```bash
./gradlew bootRun
```

## 🛠 Usage

The application exposes a simple endpoint that simulates a slow service (3 seconds delay). The first request will be slow (cache miss), and subsequent requests with the same key will be fast (cache hit).

### Curl Examples

1.  **First Request (Cache Miss)**
    Time taken: ~3 seconds

    ```bash
    curl http://localhost:8080/data/mykey
    ```

    Response:
    ```
    Data for mykey (Time: 3005ms)
    ```

2.  **Second Request (Cache Hit)**
    Time taken: < 10ms

    ```bash
    curl http://localhost:8080/data/mykey
    ```

    Response:
    ```
    Data for mykey (Time: 5ms)
    ```

## 🧪 Testing

This project uses **Testcontainers** to run integration tests with a real Redis instance in a Docker container. It verifies cache hits and misses by checking execution time.

To run the tests:

```bash
./gradlew clean test
```

The build is configured to show test execution events (`passed`, `skipped`, `failed`) in the console output.

## 📂 Project Structure

-   `src/main/java`: Source code for the Spring Boot application.
-   `src/test/java`: Integration tests using Testcontainers.
-   `docker-compose.yml`: Docker Compose configuration for running the app and Redis.
-   `Dockerfile`: Docker configuration for the Spring Boot application.
