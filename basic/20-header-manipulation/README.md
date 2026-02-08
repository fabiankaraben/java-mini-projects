# Header Manipulation

## Description
This is a simple backend in Java, demonstrating how to read custom headers from HTTP requests and set custom headers in HTTP responses. It serves as a practical example of header manipulation in a Spring Boot application.

## Requirements
- Java 17 or higher
- Gradle (provided via wrapper)

## How to Run
1. Navigate to the project directory:
   ```bash
   cd basic/20-header-manipulation
   ```
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```
   The server will start on port 8080 (default).

## How to Use

### Endpoint: `GET /api/headers`

This endpoint accepts a custom header `X-Custom-Input` and returns it in the response body, along with a processed version in the response header `X-Custom-Output`. It also adds a timestamp header.

#### Example 1: Without custom header (uses default)
```bash
curl -v http://localhost:8080/api/headers
```
**Expected Output (Truncated):**
```
< HTTP/1.1 200 
< X-Custom-Output: Processed: default
< X-Server-Timestamp: 1677...
...
{"receivedCustomInput":"default","message":"Headers processed successfully"}
```

#### Example 2: With custom header
```bash
curl -v -H "X-Custom-Input: MyCustomValue" http://localhost:8080/api/headers
```
**Expected Output (Truncated):**
```
< HTTP/1.1 200 
< X-Custom-Output: Processed: MyCustomValue
< X-Server-Timestamp: 1677...
...
{"receivedCustomInput":"MyCustomValue","message":"Headers processed successfully"}
```

## Running Tests
To run the automated tests, execute the following command:

```bash
./gradlew clean test
```

The test logging is configured to show passed, skipped, and failed events in the console output.
