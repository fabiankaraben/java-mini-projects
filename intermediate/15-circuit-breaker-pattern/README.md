# Circuit Breaker Pattern

This mini-project demonstrates the **Circuit Breaker Pattern** using **Spring Boot** and **Resilience4j**. It simulates interactions with an external API and handles failures gracefully by providing fallback responses when the external service is unavailable or failing.

## Requirements

- Java 17+
- Maven

## Features

- **Spring Boot**: Web application framework.
- **Resilience4j**: Fault tolerance library implementing the Circuit Breaker pattern.
- **WireMock**: Used in tests to simulate external API behavior (success, failure, delays).

## How it Works

The application exposes an endpoint `/api/data`. This endpoint calls an external service (configured as `http://localhost:8081/api/external`).
- **Closed State**: The application forwards requests to the external service.
- **Open State**: If the failure rate exceeds the threshold (configured as 50%), the Circuit Breaker opens, and requests are immediately blocked and redirected to a fallback method.
- **Half-Open State**: After a wait duration, the Circuit Breaker allows a limited number of test requests to check if the external service has recovered.

## Configuration (`application.yml`)

The Circuit Breaker is configured with the following parameters:
- `slidingWindowSize`: 5 (Number of calls to record)
- `failureRateThreshold`: 50% (Open circuit if 50% of calls fail)
- `waitDurationInOpenState`: 5s (Time to wait before trying again)
- `permittedNumberOfCallsInHalfOpenState`: 3

## Usage

### Running the Application

1. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

2. Access the endpoint:
   ```bash
   curl http://localhost:8080/api/data
   ```

   *Note: Since there is no actual service running on port 8081 during normal execution, this will trigger the fallback response.*

   **Expected Output:**
   ```
   Fallback response: External service is currently unavailable.
   ```

## Testing

The project includes integration tests using **WireMock** to simulate the external service and verify the Circuit Breaker behavior.

### Run Tests

```bash
mvn clean test
```

The tests perform the following steps:
1. Simulate a successful call to the external API.
2. Simulate failures (500 Internal Server Error) to reach the failure rate threshold.
3. Verify that the Circuit Breaker opens and returns the fallback response.
4. Verify that subsequent calls are blocked while the circuit is open.
