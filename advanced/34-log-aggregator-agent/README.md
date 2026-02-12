# Log Aggregator Agent

A Java-based log shipping agent that tails a local log file and forwards the lines to a central server via HTTP (similar to Filebeat).

## Requirements

- Java 17+
- Gradle (wrapper provided)
- Docker & Docker Compose (optional, for running the full stack)

## Features

- **File Tailing**: Real-time monitoring of a specified log file using Apache Commons IO `Tailer`.
- **Log Forwarding**: HTTP POST requests to a central log server.
- **Resilience**: Continues monitoring even if the file is rotated or temporarily unavailable.

## Project Structure

- `src/main/java`: Source code for the agent.
- `src/test/java`: Integration tests using WireMock and Awaitility.
- `Dockerfile` & `docker-compose.yml`: Containerization setup.

## Configuration

The application is configured via `application.properties` or environment variables:

| Property | Environment Variable | Description | Default |
|----------|----------------------|-------------|---------|
| `log.aggregator.file.path` | `LOG_AGGREGATOR_FILE_PATH` | Path to the log file to tail | `/tmp/monitored-app.log` |
| `log.aggregator.server.url` | `LOG_AGGREGATOR_SERVER_URL` | URL of the central log server | `http://localhost:8081/logs` |

## How to Run

### Local Execution

1.  Build the project:
    ```bash
    ./gradlew clean build
    ```

2.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

3.  Simulate logs:
    Open a new terminal and append logs to the monitored file (default: `/tmp/monitored-app.log`):
    ```bash
    echo "INFO: New log entry" >> /tmp/monitored-app.log
    ```

### Docker Execution

This project includes a `docker-compose.yml` that runs the agent alongside a MockServer to act as the central log receiver.

1.  Start the services:
    ```bash
    docker compose up --build
    ```

2.  Check Agent Status:
    ```bash
    curl http://localhost:8080/status
    # Output: {"agent":"Log Aggregator Agent","status":"UP","timestamp":...}
    ```

3.  Simulate logs:
    The `logs` directory in the project root is mounted to `/tmp/logs` in the container.
    ```bash
    mkdir -p logs
    echo "INFO: Docker log entry" >> logs/app.log
    ```

4.  Verify logs are sent:
    You can check the MockServer dashboard (if exposed) or simply look at the `log-aggregator` container logs to see if it processed the line (debug logging might need to be enabled).

## Testing

The project includes integration tests that verify the file tailing and HTTP forwarding logic using WireMock.

Run the tests with:

```bash
./gradlew clean test
```

Expected output:
```
> Task :test

LogAggregatorIntegrationTest > shouldTailFileAndSendLogsToServer() PASSED

BUILD SUCCESSFUL
```
