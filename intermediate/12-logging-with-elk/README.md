# Logging with SLF4J/Logback and ELK Stack

This mini-project demonstrates how to configure a Spring Boot application to send structured logs to an ELK (Elasticsearch, Logstash, Kibana) stack using SLF4J and Logback.

## Project Structure

- **Spring Boot Application**: Generates logs at various levels (INFO, DEBUG, ERROR, etc.).
- **Logback**: Configured to send logs to Logstash via TCP using `logstash-logback-encoder`.
- **Logstash**: Receives logs from the application and forwards them to Elasticsearch.
- **Elasticsearch**: Stores and indexes the logs.
- **Kibana**: Visualizes the logs.

## Requirements

- Java 17 or higher
- Docker and Docker Compose

## How to Run

### 1. Start the ELK Stack

The project includes a `docker-compose.yml` file to spin up Elasticsearch, Logstash, and Kibana.

```bash
docker compose up -d
```

Wait for a few minutes for the services to start. You can check the status with `docker compose ps`.

### 2. Run the Application

You can run the application using the Gradle wrapper (after generating it) or your local Gradle installation.

```bash
./gradlew bootRun
```

Or run it inside Docker (optional):
The provided `docker-compose.yml` also includes the `app` service.

```bash
docker compose up -d --build
```

## Usage

Once the application is running (default port 8080), you can generate logs using the following endpoints:

### Generate Standard Logs

```bash
# Default INFO log
curl "http://localhost:8080/log"

# Generate WARN log
curl "http://localhost:8080/log?level=WARN&message=Something%20might%20be%20wrong"

# Generate ERROR log
curl "http://localhost:8080/log?level=ERROR&message=Critical%20failure"

# Generate DEBUG log (needs DEBUG level enabled in logback or application.properties)
curl "http://localhost:8080/log?level=DEBUG&message=Debugging%20info"
```

### Generate Exception Log

This endpoint throws an exception which is caught and logged as an ERROR with stack trace.

```bash
curl "http://localhost:8080/exception"
```

## Viewing Logs in Kibana

1. Open Kibana in your browser: [http://localhost:5601](http://localhost:5601)
2. Go to **Management > Stack Management > Kibana > Index Patterns**.
3. Create a new index pattern. You should see an index matching `springboot-logs-*`.
4. Define the pattern as `springboot-logs-*` and select `@timestamp` as the time field.
5. Go to **Discover** to view and search your logs.

## Testing

The project includes integration tests that verify log generation using a custom Logback Appender to capture logs in memory.

To run the tests:

```bash
./gradlew clean test
```

The test output will show the status of each test (PASSED, SKIPPED, FAILED).

## Stop Services

To stop and remove the containers:

```bash
docker compose down
```
