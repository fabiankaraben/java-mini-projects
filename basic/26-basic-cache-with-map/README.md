# Basic Cache with Map

This is a basic backend in Java, implementing an in-memory cache for expensive computations. It demonstrates how to use `ConcurrentHashMap` to cache results of expensive operations, avoiding redundant computations for the same input.

## Requirements

- Java 17 or higher
- Gradle (wrapper included)

## Project Structure

- `src/main/java/com/example/cache/ComputationService.java`: Interface for computation services.
- `src/main/java/com/example/cache/ExpensiveComputationService.java`: Simulates a slow computation (2 seconds delay).
- `src/main/java/com/example/cache/CachedComputationService.java`: A decorator that caches results using a Map.
- `src/main/java/com/example/cache/App.java`: Simple HTTP server exposing the service.
- `src/test/java/com/example/cache/CachedComputationServiceTest.java`: Unit tests verifying cache behavior.

## How to Run

1. **Start the application:**

   ```bash
   ./gradlew run
   ```

   The server will start on port 8080.

## How to Use

You can interact with the service using `curl`.

### 1. Perform a Computation

**Request:**

```bash
curl "http://localhost:8080/compute?input=hello"
```

**Response (First time - takes ~2 seconds):**

```
Result: Processed: hello
Time taken: 2005 ms
```

**Response (Second time - instant):**

```
Result: Processed: hello
Time taken: 0 ms
```

### 2. Check Cache Statistics

**Request:**

```bash
curl http://localhost:8080/stats
```

**Response:**

```
Cache Size: 1
```

## Running Tests

To run the unit tests and see the cache hit/miss verification:

```bash
./gradlew clean test
```

The test output will show passing tests for cache hits, misses, and cache clearing. We have configured the test logging to show passed, skipped, and failed events.
