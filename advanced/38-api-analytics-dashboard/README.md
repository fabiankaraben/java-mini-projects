# API Analytics Dashboard

A Java-based backend service that aggregates API usage metrics (latency, error rates) into time-series data. It provides endpoints to ingest metrics and retrieve aggregated statistics including P95 latency.

## Requirements

-   Java 17 or higher
-   Docker and Docker Compose (optional, for containerized run)
-   Gradle (wrapper included)

## Features

-   **Ingestion**: Receives API metric events (endpoint, method, latency, error status).
-   **Aggregation**: Calculates request counts, error rates, average latency, and 95th percentile (P95) latency per endpoint and method.
-   **Concurrency**: Uses thread-safe data structures (`CopyOnWriteArrayList`, `ConcurrentHashMap` logic) to handle concurrent ingestion.
-   **Testing**: Includes integration tests and load simulation.

## Getting Started

### Running Locally

1.  **Build and Run**:
    ```bash
    ./gradlew bootRun
    ```
    The application will start on port `8080`.

### Running with Docker

1.  **Build and Start**:
    ```bash
    docker compose up --build
    ```
    The application will start on port `8080`.

## API Usage

### 1. Ingest Metric
Send a metric to the system.

```bash
curl -X POST http://localhost:8080/api/analytics/ingest \
  -H "Content-Type: application/json" \
  -d '{
    "endpoint": "/api/users",
    "method": "GET",
    "latencyMs": 120,
    "isError": false
  }'
```

### 2. Get Aggregated Stats
Retrieve aggregated statistics for all endpoints.

```bash
curl http://localhost:8080/api/analytics/stats
```

**Response Example:**
```json
[
  {
    "endpoint": "/api/users",
    "method": "GET",
    "requestCount": 1,
    "errorRate": 0.0,
    "p95Latency": 120.0,
    "averageLatency": 120.0
  }
]
```

## Testing

The project includes integration tests that verify aggregation logic (P95 calculation) and simulate load.

To run the tests:

```bash
./gradlew clean test
```

Test output will show passed/skipped/failed events.

## Project Structure

-   `src/main/java/com/example/analytics/AnalyticsService.java`: Core logic for storage and aggregation.
-   `src/main/java/com/example/analytics/AnalyticsController.java`: REST API endpoints.
-   `src/main/java/com/example/analytics/Metric.java`: Domain model for a single data point.
-   `src/main/java/com/example/analytics/AggregatedData.java`: Domain model for result stats.
-   `src/test/java/com/example/analytics/AnalyticsIntegrationTest.java`: Integration and load tests.
