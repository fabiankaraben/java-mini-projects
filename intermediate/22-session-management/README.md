# Session Management

This mini-project demonstrates how to implement distributed session management in a Java Spring Boot application using **Spring Session** and **Redis**. It ensures that user session data is stored in an external Redis store rather than the application's memory, allowing sessions to persist across application restarts and enabling horizontal scaling.

## Requirements

- Java 17+
- Docker & Docker Compose (for running Redis)
- Gradle (provided via wrapper, but wrapper generation is required if not present)

## Getting Started

### 1. Build the Project

```bash
./gradlew clean build
```

### 2. Run with Docker Compose

This will start both the Spring Boot application and the Redis container.

```bash
docker compose up --build
```

The application will be accessible at `http://localhost:8080`.

### 3. Usage Examples

You can use `curl` to interact with the application and verify session persistence.

**Step 1: Set a session attribute**

```bash
# -c cookies.txt saves the received cookies to a file
curl -v -c cookies.txt "http://localhost:8080/set?key=username&value=fabian"
```

Output should verify the save: `Saved username=fabian`

**Step 2: Get the session attribute**

```bash
# -b cookies.txt sends the cookies from the file
curl -v -b cookies.txt "http://localhost:8080/get?key=username"
```

Output should be: `fabian`

**Step 3: Verify Persistence**

1. Stop the application (Ctrl+C in the docker compose terminal).
2. Start the application again: `docker compose up` (Redis volume might be ephemeral depending on config, but if Redis is not recreated or if volume is mapped, it persists. In this setup, restarting the `app` service while keeping `redis` running proves session externalization).
   - To strictly test "app restart", you can `docker compose restart app`.
3. Run the "Get" curl command again. It should still return `fabian`.

## Testing

This project includes integration tests using **Testcontainers**. The tests spin up a Redis container and verify that session data is preserved even when the Spring ApplicationContext is reloaded (simulating an app restart).

To run the tests:

```bash
./gradlew clean test
```

The build is configured to log test events (`passed`, `skipped`, `failed`) to the console.
