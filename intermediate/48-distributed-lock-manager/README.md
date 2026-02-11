# Distributed Lock Manager

This mini-project implements a Distributed Lock Manager using Java and Redis. It ensures exclusive access to a resource across multiple instances or threads.

## Requirements

*   Java 21 or higher
*   Docker and Docker Compose

## Project Structure

*   `src/main/java`: Source code for the Spring Boot application.
*   `src/test/java`: Integration tests demonstrating concurrency control.
*   `Dockerfile`: Docker image definition for the application.
*   `docker-compose.yml`: Compose file to run the application and Redis.

## How to Run

### Using Docker Compose

1.  Build and start the services:
    ```bash
    docker compose up --build
    ```
2.  The application will be available at `http://localhost:8080`.

### Running Tests

To run the integration tests (requires Docker for Testcontainers):

```bash
./gradlew clean test
```

The test output will show `passed`, `skipped`, or `failed` events for each test.

## Usage

### Acquire Lock

Try to acquire a lock for a specific resource ID.

```bash
curl -X POST "http://localhost:8080/api/resource/my-resource/access?ttl=10"
```

**Response (Success):**
```
<lock-token-uuid>
```

**Response (Locked):**
```
Resource is locked
```

### Release Lock

Release the lock using the token received during acquisition.

```bash
curl -X POST "http://localhost:8080/api/resource/my-resource/release?token=<lock-token-uuid>"
```

**Response:**
```
Resource released
```

## How it works

The `DistributedLockService` uses Redis `SET NX` (set if not exists) command with an expiration time. This atomic operation ensures that only one client can set the key and thus acquire the lock. If the operation returns true, the lock is acquired. The lock automatically expires after the TTL to prevent deadlocks if the holding process crashes.
