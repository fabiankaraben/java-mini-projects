# Prometheus Metrics Mini-Project

This project demonstrates a simple Java backend using **Spring Boot** and **Micrometer** to expose application metrics in a format compatible with **Prometheus**.

## Description

The application provides a few sample endpoints (`/hello`, `/random`) that simulate work. It uses the `micrometer-registry-prometheus` to collect metrics and verify them. The metrics are exposed at the `/metrics` endpoint.

## Requirements

- Java 17 or higher
- Maven

## How to Run

1. Navigate to the project directory:
   ```bash
   cd basic/29-prometheus-metrics
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   # OR if you have maven installed
   mvn spring-boot:run
   ```
   (Note: If `mvnw` is not generated, use `mvn spring-boot:run` assuming Maven is installed)

3. Access the endpoints:
   ```bash
   curl http://localhost:8080/hello
   curl http://localhost:8080/random
   ```

4. View the Prometheus metrics:
   ```bash
   curl http://localhost:8080/metrics
   ```
   You should see output similar to:
   ```text
   # HELP hello_request_seconds Time taken to return hello
   # TYPE hello_request_seconds summary
   hello_request_seconds_count 2.0
   hello_request_seconds_sum 0.012
   ...
   ```

## Testing

To run the integration tests which verify that metrics are correctly generated and exposed:

```bash
mvn clean test
```

The tests perform the following:
1. Hit the `/hello` and `/random` endpoints.
2. Fetch the `/metrics` endpoint.
3. Assert that the custom metrics (e.g., `hello_request_seconds_count`) and JVM metrics are present.
