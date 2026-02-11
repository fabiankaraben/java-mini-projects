# Distributed Counter

This mini-project is a **Distributed Counter** service built with **Spring Boot** and **Redis**. It utilizes Redis **HyperLogLog** data structures to provide memory-efficient, approximate counting of unique elements (cardinality) at scale. This is ideal for metrics like unique page views, daily active users, etc.

## Requirements

- **Java 17+** (Project is configured for Java 25 environment targeting Java 17)
- **Gradle**
- **Docker** and **Docker Compose** (for running Redis)

## Features

- **Add Element**: Add a unique element to a counter.
- **Get Count**: Retrieve the approximate cardinality of a counter.
- **Merge Counters**: Merge multiple counters into one (e.g., merge daily counters into a weekly counter).
- **Concurrent Load Testing**: Includes integration tests that simulate high concurrency.

## Getting Started

### Running with Docker Compose

The easiest way to run the application is using Docker Compose, which starts both the application and the Redis container.

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Running Locally

If you want to run the application locally (e.g., for development), you need a running Redis instance.

1. Start Redis:
   ```bash
   docker run -d -p 6379:6379 redis:7-alpine
   ```
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## API Usage

### 1. Add Element to Counter

Increments the unique count for a given key.

```bash
curl -X POST "http://localhost:8080/api/counter/page_views/add?element=user123"
```

### 2. Get Count

Retrieves the approximate count of unique elements.

```bash
curl -X GET "http://localhost:8080/api/counter/page_views/count"
```

Response:
```json
{
  "count": 1
}
```

### 3. Merge Counters

Merges multiple source keys into a destination key.

```bash
# Add to day1
curl -X POST "http://localhost:8080/api/counter/day1/add?element=userA"
# Add to day2
curl -X POST "http://localhost:8080/api/counter/day2/add?element=userB"

# Merge day1 and day2 into week1
curl -X POST "http://localhost:8080/api/counter/merge?destination=week1&sources=day1,day2"

# Get count for week1
curl -X GET "http://localhost:8080/api/counter/week1/count"
```

## Testing

The project includes integration tests that use **Testcontainers** to spin up a Redis instance and **ExecutorService** to simulate concurrent load.

To run the tests:

```bash
./gradlew clean test
```

The test output will show the events: `passed`, `skipped`, `failed`.

### Test Logic
The `DistributedCounterIntegrationTest` simulates 100 concurrent threads, each adding 100 unique elements (total 10,000 unique items). It then asserts that the HyperLogLog count is within a 5% error margin of the actual count, demonstrating the approximate nature and eventual consistency of the system.
