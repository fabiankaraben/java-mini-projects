
# URL Health Monitor

A Java Spring Boot application that periodically checks a list of URLs and records their status (UP/DOWN, response time, status code).

## Requirements

- Java 17+
- Maven
- Docker (optional, for containerization)

## Features

- **Scheduled Monitoring**: Automatically checks URLs at a configurable interval (default 10s for demo).
- **History Tracking**: Stores check results (timestamp, status, response time) in an H2 in-memory database.
- **REST API**:
  - `POST /api/urls`: Add a URL to monitor.
  - `GET /api/urls`: List all monitored URLs.
  - `GET /api/urls/{id}/history`: Get check history for a specific URL.

## How to Run

### Using Maven

1. **Build and Run**:
   ```bash
   mvn clean spring-boot:run
   ```

2. **Access the application**:
   The application runs on `http://localhost:8080`.

### Using Docker

1. **Build and Run with Docker Compose**:
   ```bash
   docker compose up --build
   ```

2. **Stop**:
   ```bash
   docker compose down
   ```

## Usage Examples

### 1. Add a URL to monitor
```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"url": "https://www.google.com"}'
```

### 2. List all monitored URLs
```bash
curl http://localhost:8080/api/urls
```

### 3. View History for a URL
(Replace `1` with the actual ID from the list)
```bash
curl http://localhost:8080/api/urls/1/history
```

## Testing

Run the integration tests to verify the scheduling and monitoring logic:

```bash
mvn clean test
```

The tests use **WireMock** to simulate external endpoints and **Awaitility** to wait for the asynchronous scheduled tasks to execute.

## Project Structure

- `src/main/java`: Source code
  - `model`: JPA Entities (`MonitoredUrl`, `UrlCheckHistory`)
  - `repository`: Data access
  - `service`: Business logic and `@Scheduled` task
  - `controller`: REST endpoints
- `src/test/java`: Integration tests
