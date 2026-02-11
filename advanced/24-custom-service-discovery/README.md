# Custom Service Discovery

A lightweight Service Discovery server implemented in Java using Spring Boot. This project demonstrates the core concepts of service registry, heartbeats, and failure detection (eviction).

## Features

- **Service Registration**: Services can register themselves with the discovery server.
- **Heartbeats**: Services must send periodic heartbeats to maintain their registration.
- **Failure Detection**: The registry automatically evicts instances that fail to send heartbeats within a configured threshold (30 seconds).
- **REST API**: Simple API for registration, heartbeats, and querying instances.

## Requirements

- Java 17+
- Docker & Docker Compose (optional, for containerized run)

## Project Structure

- `src/main/java/com/example/discovery`: Source code.
  - `ServiceRegistry.java`: Core logic for managing service instances.
  - `RegistryCleaner.java`: Scheduled task to remove stale instances.
  - `RegistryController.java`: REST endpoints.
- `src/test/java/com/example/discovery`: Integration tests.

## How to Run

### Local Execution

1. Make sure you have the Gradle wrapper or Gradle installed.
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```
   The server will start on port 8080.

### Docker Execution

1. Build and run using Docker Compose:
   ```bash
   docker compose up --build
   ```
2. The application will be accessible at `http://localhost:8080`.

## API Usage

### 1. Register a Service

```bash
curl -X POST http://localhost:8080/api/registry/register \
     -H "Content-Type: application/json" \
     -d '{
           "serviceId": "my-service",
           "host": "localhost",
           "port": 9000
         }'
```

### 2. Send Heartbeat

```bash
curl -X POST "http://localhost:8080/api/registry/heartbeat?serviceId=my-service&host=localhost&port=9000"
```

### 3. List All Instances

```bash
curl http://localhost:8080/api/registry/instances
```

### 4. List Instances for Specific Service

```bash
curl http://localhost:8080/api/registry/instances/my-service
```

### 5. Unregister a Service

```bash
curl -X DELETE "http://localhost:8080/api/registry/unregister?serviceId=my-service&host=localhost&port=9000"
```

## Running Tests

This project uses Gradle for build and testing. The tests verify the registration flow, heartbeat updates, and automatic eviction of stale instances.

To run the tests:

```bash
./gradlew clean test
```

Expected output will show `passed`, `skipped`, and `failed` events for each test.
