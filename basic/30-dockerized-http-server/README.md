# Dockerized HTTP Server

This is a basic backend in Java, packaged in a Docker image for easy deployment. It demonstrates how to containerize a Java application using Docker and how to test it using Testcontainers.

## Requirements

- Java 17 or higher
- Docker
- Docker Compose

## Project Structure

- `src/main/java`: Source code for the simple HTTP server.
- `src/test/java`: Integration tests using Testcontainers.
- `Dockerfile`: Instructions for building the Docker image.
- `docker-compose.yml`: Configuration for running the application with Docker Compose.
- `build.gradle`: Project dependency and build configuration.

## How to Run

### Using Docker Compose

The easiest way to run the application is using Docker Compose. This handles building the image and running the container.

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```

2.  The server will start on port `8080`.

3.  To stop the server:
    ```bash
    docker compose down
    ```

### Using Gradle Locally

You can also run the application locally without Docker for development:

```bash
./gradlew run
```

## How to Use

Once the server is running (either via Docker or Gradle), you can test it using `curl`:

```bash
curl -v http://localhost:8080/
```

Expected output:
```
Hello from Dockerized Java Server!
```

## Testing

This project uses **Testcontainers** to build and run the Docker image during testing, ensuring that the containerized application works as expected.

To run the tests:

```bash
./gradlew clean test
```

The test output will show events for "passed", "skipped", and "failed" tests.
