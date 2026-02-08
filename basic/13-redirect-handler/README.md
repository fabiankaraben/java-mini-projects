# Redirect Handler

This is a simple backend in Java that handles GET requests by redirecting them to another URL using HTTP 301 (Moved Permanently) or 302 (Found) status codes.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

- `src/main/java`: Source code for the Redirect Handler.
- `src/test/java`: Integration tests using JUnit 5 and `java.net.http.HttpClient`.

## How to Run

1. **Compile the project**:
   ```bash
   mvn clean compile
   ```

2. **Run the server**:
   You can run the `RedirectHandlerServer` class directly from your IDE or compile it into a jar.
   
   Using Maven to run (if configured) or simply compiling and running the class:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.fabiankaraben.redirecthandler.RedirectHandlerServer"
   ```
   
   The server will start on port `8080`.

## How to Use

Once the server is running, you can test the redirects using `curl`.

### 302 Found (Temporary Redirect)

Endpoint: `/redirect` -> Redirects to `https://www.example.com`

```bash
curl -v http://localhost:8080/redirect
```

**Expected Output (Headers):**
```
< HTTP/1.1 302 Found
< Location: https://www.example.com
< Content-length: 0
```

### 301 Moved Permanently

Endpoint: `/moved` -> Redirects to `https://www.example.com/new-location`

```bash
curl -v http://localhost:8080/moved
```

**Expected Output (Headers):**
```
< HTTP/1.1 301 Moved Permanently
< Location: https://www.example.com/new-location
< Content-length: 0
```

## Running Tests

To run the integration tests, use the following command:

```bash
mvn clean test
```

The tests verify that:
1. The server responds with the correct status code (301 or 302).
2. The `Location` header is correctly set to the target URL.
