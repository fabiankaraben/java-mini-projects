# Panic/Exception Recovery Mini-Project

This project demonstrates a simple Java backend that is resilient to unchecked exceptions (RuntimeExceptions) occurring within request handlers. It implements a mechanism to catch these exceptions, log them, and return a graceful `500 Internal Server Error` response instead of crashing the server or leaving the connection hanging.

## Requirements

*   Java 17 or later
*   Maven 3.6 or later

## Project Structure

*   `src/main/java/com/example/exceptionrecovery/App.java`: Main application class. Sets up the HTTP server and handlers. Includes a `SafeHandler` wrapper that provides the exception recovery logic.
*   `src/test/java/com/example/exceptionrecovery/IntegrationTest.java`: Integration tests that verify the server's behavior under normal and error conditions.

## How to Run

1.  **Build the project:**
    ```bash
    mvn clean package
    ```

2.  **Run the application:**
    ```bash
    java -jar target/exception-recovery-1.0-SNAPSHOT.jar
    ```

    The server will start on port `8080`.

## Usage Examples (curl)

### 1. Happy Path
Request the `/hello` endpoint, which functions normally.

```bash
curl -i http://localhost:8080/hello
```

**Expected Output:**
```
HTTP/1.1 200 OK
...
Hello, World!
```

### 2. Triggering a Panic (Exception)
Request the `/poison` endpoint, which intentionally throws a `RuntimeException`.

```bash
curl -i http://localhost:8080/poison
```

**Expected Output:**
```
HTTP/1.1 500 Internal Server Error
...
Internal Server Error: This is a poison pill!
```

**Server Logs:**
The server console will verify that the exception was caught:
```
Recovered from unchecked exception: This is a poison pill!
...stack trace...
```

### 3. Verifying Recovery
After hitting the poison endpoint, try the hello endpoint again to prove the server is still running.

```bash
curl -i http://localhost:8080/hello
```

## Running Tests

This project includes integration tests that programmatically start the server, hit the poison endpoint to ensure it returns 500, and then verify the server is still responsive.

Run the tests using Maven:

```bash
mvn clean test
```
