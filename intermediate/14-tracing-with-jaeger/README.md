# Tracing with Jaeger/OpenTelemetry

This is a mini-project demonstrating a Java backend using **Spring Boot** and **OpenTelemetry**, configured to send traces to **Jaeger**.

## Requirements

- Java 17 or higher
- Docker and Docker Compose (for running Jaeger)
- Gradle (provided via wrapper, but requires Java)

## Project Structure

- `src/main/java`: Spring Boot application code.
- `src/test/java`: Integration tests using Testcontainers.
- `docker-compose.yml`: Definition for running Jaeger and the application.

## How to Run

### Using Docker Compose (Recommended)

This starts both the Jaeger backend and the Spring Boot application.

1.  Build the application JAR:
    ```bash
    ./gradlew clean build -x test
    ```
2.  Start the services:
    ```bash
    docker compose up --build
    ```
3.  Access the application:
    ```bash
    curl "http://localhost:8080/hello?name=Fabian"
    ```
4.  View traces in Jaeger UI:
    Open [http://localhost:16686](http://localhost:16686) in your browser.

### Running Locally with Dockerized Jaeger

If you want to run the app in your IDE or terminal but use Jaeger in Docker:

1.  Start Jaeger:
    ```bash
    docker run -d --name jaeger \
      -e COLLECTOR_OTLP_ENABLED=true \
      -p 16686:16686 \
      -p 4317:4317 \
      -p 4318:4318 \
      jaegertracing/all-in-one:1.60
    ```
2.  Run the application:
    ```bash
    ./gradlew bootRun
    ```
3.  Make requests and view traces as above.

## Testing

The project includes integration tests that use **Testcontainers** to spin up a Jaeger instance and verify the application connects correctly.

To run the tests:

```bash
./gradlew clean test
```

You should see output indicating tests `passed`.

## Endpoints

-   `GET /hello?name={name}`: Returns a greeting.
-   `GET /actuator/health`: Application health status.

## Configuration

The application is configured in `src/main/resources/application.yml`.
OpenTelemetry export is set to `http://localhost:4317` (gRPC) by default.
