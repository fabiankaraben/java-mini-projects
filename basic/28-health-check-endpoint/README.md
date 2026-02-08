# Health Check Endpoint

This mini-project demonstrates a basic backend in Java that provides a `/health` endpoint returning a 200 OK status. It serves as a fundamental example of setting up a simple HTTP server and handling a specific route to verify service availability.

## Requirements

- Java 11 or higher
- Gradle (provided via wrapper)

## How to Use

### Running the Application

1. Open a terminal and navigate to the project directory:
   ```bash
   cd basic/28-health-check-endpoint
   ```

2. Run the application using Gradle:
   ```bash
   ./gradlew run
   ```
   The server will start on port `8080`.

3. Verify the endpoint using `curl`:
   ```bash
   curl -v http://localhost:8080/health
   ```
   
   Expected Output:
   ```
   < HTTP/1.1 200 OK
   < Content-type: application/json
   < Content-length: 16
   < 
   {"status": "OK"}
   ```

### Running Tests

To run the integration tests, execute the following command:

```bash
./gradlew clean test
```

The test suite includes a simple integration test that verifies:
- The `/health` endpoint returns a `200 OK` status code.
- The response body matches the expected JSON: `{"status": "OK"}`.
