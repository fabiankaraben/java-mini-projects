# Rate Limiter with Time

This is a basic backend in Java implementing a simple rate limiter using the Token Bucket algorithm. It limits the number of requests a client can make within a specific time window.

## Requirements

*   Java 11 or higher
*   Gradle (wrapper included)

## Features

*   **Token Bucket Algorithm**: Implements a thread-safe token bucket rate limiter.
*   **HTTP Server**: Uses `com.sun.net.httpserver` to handle incoming requests.
*   **Concurrency Handling**: Designed to handle concurrent requests efficiently.
*   **Rate Limit**: configured to 5 requests per second with a burst capacity of 10.

## How to Run

1.  **Build and Run the Application:**

    ```bash
    ./gradlew run
    ```
    The server will start on port `8080`.

2.  **Test with CURL:**

    You can send requests to the API endpoint:

    ```bash
    curl -i http://localhost:8080/api/resource
    ```

    **Response Headers:**
    *   `200 OK`: Request allowed.
    *   `429 Too Many Requests`: Rate limit exceeded.

    **Simulate High Traffic:**
    To simulate multiple requests rapidly (e.g., in a loop):

    ```bash
    for i in {1..20}; do curl -i http://localhost:8080/api/resource; echo; done
    ```

## Testing

This project includes concurrency tests that simulate rapid requests to verify the rate limiting behavior (429 Too Many Requests).

To run the tests and see the output details:

```bash
./gradlew clean test
```

The build configuration is set to show test events: `passed`, `skipped`, and `failed`.

## Project Structure

*   `src/main/java/com/example/ratelimiter/TokenBucket.java`: The core rate limiter logic.
*   `src/main/java/com/example/ratelimiter/App.java`: The HTTP server and main entry point.
*   `src/test/java/com/example/ratelimiter/RateLimiterTest.java`: Integration tests for concurrency and rate limiting.
