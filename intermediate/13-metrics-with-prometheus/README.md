# Metrics with Prometheus

This is a backend in Java using **Spring Boot** and **Micrometer**, exposing custom metrics.
It includes a Docker Compose setup to run the application alongside a Prometheus instance scraped from the application's actuator endpoint.

## Requirements

- Java 17 or later
- Maven
- Docker and Docker Compose (optional, for running the full stack)

## Project Structure

- `src/main/java`: Spring Boot application and controller.
- `src/main/resources`: Configuration files.
- `src/test/java`: Integration tests.
- `Dockerfile`: Docker image definition for the application.
- `docker-compose.yml`: Services definition (App + Prometheus).
- `prometheus.yml`: Prometheus configuration file.

## How to Run

### Using Maven (Local)

1.  **Build and Run**:
    ```bash
    ./mvnw spring-boot:run
    ```
    (Or install first: `./mvnw clean install` and run `java -jar target/metrics-with-prometheus-0.0.1-SNAPSHOT.jar`)

2.  **Access the endpoint**:
    ```bash
    curl http://localhost:8080/hello
    ```

3.  **Check Metrics**:
    ```bash
    curl http://localhost:8080/actuator/prometheus
    ```

### Using Docker Compose

1.  **Build and Start**:
    ```bash
    docker compose up --build
    ```

2.  **Access the application**:
    - App: `http://localhost:8080`
    - Prometheus: `http://localhost:9090`

3.  **Generate some traffic**:
    Run the following command a few times to generate metrics:
    ```bash
    curl http://localhost:8080/hello
    ```

4.  **View Metrics in Prometheus**:
    - Open `http://localhost:9090` in your browser.
    - Search for `custom_api_requests_total` or `custom_api_delay_seconds`.

## Testing

To run the integration tests, use the following command. The project uses Maven, but if you were using Gradle, you would use `./gradlew clean test`.

```bash
./mvnw clean test
```

The tests verify:
- The context loads successfully.
- The `/actuator/prometheus` endpoint is exposed and returns valid content.
- Accessing the `/hello` endpoint updates the custom metrics.

## API Endpoints

-   `GET /hello`: Returns a greeting and records custom metrics (counter and timer).
-   `GET /actuator/prometheus`: Exposes metrics in Prometheus format.

## Custom Metrics

-   `custom.api.requests`: Counter tracking the number of requests to `/hello`.
-   `custom.api.delay`: Timer tracking the execution time of the `/hello` endpoint (simulated delay).
