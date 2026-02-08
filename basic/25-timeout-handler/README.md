# Timeout Handler

This mini-project demonstrates how to implement a request timeout mechanism in a raw Java HTTP server. It wraps an existing `HttpHandler` and ensures that if the execution takes longer than a specified duration, the processing is cancelled and a `503 Service Unavailable` response is sent.

## Requirements

- Java 17+
- Maven 3.6+

## Project Structure

- **App.java**: The main entry point that starts the HTTP server.
- **TimeoutHandler.java**: The wrapper handler that enforces the timeout.
- **SlowHandler.java**: A sample handler that simulates long-running tasks.

## How to Run

1.  Build the project:
    ```bash
    mvn clean package
    ```

2.  Run the application:
    ```bash
    mvn exec:java -Dexec.mainClass="com.fabiankaraben.timeouthandler.App"
    ```
    Or run the JAR directly if packaged:
    ```bash
    java -cp target/timeout-handler-1.0-SNAPSHOT.jar com.fabiankaraben.timeouthandler.App
    ```

## Usage

The server starts on port 8080.

### 1. Successful Request (Fast Handler)
This endpoint sleeps for 1 second, which is within the 2-second timeout.

```bash
curl -v http://localhost:8080/fast
```
**Expected Output:**
```
< HTTP/1.1 200 OK
...
Process complete
```

### 2. Timed Out Request (Slow Handler)
This endpoint sleeps for 5 seconds, exceeding the 2-second timeout.

```bash
curl -v http://localhost:8080/slow
```
**Expected Output:**
```
< HTTP/1.1 503 Service Unavailable
...
Request timed out
```

## Running Tests

This project includes JUnit 5 tests to verify the timeout behavior. The tests spin up a server on a different port and use an `HttpClient` to verify that long-running requests are correctly terminated with a 503 status.

To run the tests:

```bash
mvn clean test
```
