# Simple Logger Middleware

This is a simple backend mini-project in Java that demonstrates how to implement a custom middleware using `com.sun.net.httpserver` and `java.util.logging`. The middleware intercepts HTTP requests to log the request method, path, and the duration it took to process the request.

## Requirements

- Java 11 or later
- Maven 3.6 or later

## Project Structure

- `src/main/java`: Source code for the HTTP server and middleware.
- `src/test/java`: Unit tests using JUnit 5.

## How to Run

1. **Compile the project:**
   ```bash
   mvn clean package
   ```

2. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.middleware.Main"
   ```
   Alternatively, you can run the generated JAR file:
   ```bash
   java -cp target/simple-logger-middleware-1.0-SNAPSHOT.jar com.example.middleware.Main
   ```

   The server will start on port `8080`.

## How to Use

Once the server is running, you can send HTTP requests using `curl`.

### Example Request

```bash
curl -v http://localhost:8080/hello
```

**Response:**
```
Hello, Middleware!
```

**Server Log Output:**
```
INFO: GET /hello took 5ms
```
*(Note: Duration will vary)*

## How to Run Tests

The project includes unit tests that use a custom `java.util.logging.Handler` to capture and verify log messages.

To run the tests:

```bash
mvn clean test
```
