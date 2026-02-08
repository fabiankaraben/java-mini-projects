# CORS Handler

This is a simple Java backend that demonstrates how to add Cross-Origin Resource Sharing (CORS) headers to HTTP responses. It uses the standard `com.sun.net.httpserver.HttpServer` included in the JDK.

## Requirements

- Java 17 or higher
- Maven 3.6+

## Project Structure

```
basic/17-cors-handler/
├── src/
│   ├── main/java/com/example/cors/CorsHandler.java
│   └── test/java/com/example/cors/CorsHandlerTest.java
├── pom.xml
├── .gitignore
└── README.md
```

## How to Run

1. **Compile the project:**
   ```bash
   mvn clean package
   ```

2. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.cors.CorsHandler"
   ```
   The server will start on port `8080`.

## Usage Examples

You can test the CORS headers using `curl`.

### 1. Simple GET Request
Verifies that the server responds and includes the `Access-Control-Allow-Origin` header.

```bash
curl -v -H "Origin: http://example.com" http://localhost:8080/api/data
```

**Expected Headers:**
```
< HTTP/1.1 200 OK
< Access-Control-Allow-Origin: *
< Access-Control-Allow-Methods: GET, POST, OPTIONS
< Access-Control-Allow-Headers: Content-Type, Authorization
< Content-Type: application/json
```

### 2. Preflight OPTIONS Request
Verifies that the server handles the preflight `OPTIONS` request correctly.

```bash
curl -v -X OPTIONS \
  -H "Origin: http://example.com" \
  -H "Access-Control-Request-Method: POST" \
  http://localhost:8080/api/data
```

**Expected Headers:**
```
< HTTP/1.1 204 No Content
< Access-Control-Allow-Origin: *
< Access-Control-Allow-Methods: GET, POST, OPTIONS
< Access-Control-Allow-Headers: Content-Type, Authorization
```

## Running Tests

This project includes integration tests that verify the CORS headers and preflight handling.

To run the tests:
```bash
mvn clean test
```
