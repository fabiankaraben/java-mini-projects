# Stock Price Simulator

This project is a **Stock Price Simulator** backend in Java that streams random stock price updates via Server-Sent Events (SSE). It demonstrates how to build real-time streaming applications using Spring Boot.

## Requirements

- Java 17 or higher
- Maven 3.6+
- Docker (optional, for containerized execution)

## Features

- **Real-time Updates**: Streams stock price updates every second.
- **Server-Sent Events (SSE)**: Uses the standard SSE protocol for unidirectional data streaming.
- **Randomized Simulation**: Simulates realistic stock price fluctuations.
- **Docker Support**: Includes Dockerfile and Docker Compose for easy deployment.

## How to Use

### Running Locally

1.  Build the project:
    ```bash
    mvn clean install
    ```
2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

### Running with Docker

This project includes a `Dockerfile` and `docker-compose.yml` for easy containerization.

1.  Run the application using Docker Compose:
    ```bash
    docker compose up --build
    ```
    This command will build the image and start the container on port `8080`.

2.  To stop the application:
    ```bash
    docker compose down
    ```

### Consuming the Stream

You can consume the stock price stream using `curl` or any SSE-compatible client.

**Endpoint:** `GET /api/stocks/stream`

**Example:**
```bash
curl -N http://localhost:8080/api/stocks/stream
```

**Expected Output:**
```
data:[{"symbol":"AAPL","price":150.23,"timestamp":"2023-10-27T10:00:01.123Z"},{"symbol":"GOOGL","price":2800.50,"timestamp":"..."},...]

data:[{"symbol":"AAPL","price":150.45,"timestamp":"2023-10-27T10:00:02.123Z"},{"symbol":"GOOGL","price":2801.10,"timestamp":"..."},...]
```

## Testing

This project contains integration tests to verify the SSE endpoint and data format.

To run the tests, execute:
```bash
mvn clean test
```
The tests use `WebTestClient` and `StepVerifier` to connect to the stream and validate the incoming events.
