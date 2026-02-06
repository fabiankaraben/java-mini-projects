# JSON Response API

This is a simple backend mini-project in Java that provides a GET endpoint returning a JSON object. It demonstrates how to serialize Java objects to JSON using the **Jackson** library and serve them via `com.sun.net.httpserver`.

## Features

- **JSON Endpoint**: Serves a JSON representation of a User object at `/api/user`.
- **JSON Serialization**: Uses `Jackson` (fasterxml) to convert Java objects to JSON strings.
- **HTTP Server**: Uses Java's built-in `com.sun.net.httpserver`.
- **Testing**:
  - Unit tests for JSON serialization/deserialization.
  - Integration tests using `java.net.http.HttpClient` to verify the API response.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

```
basic/03-json-response-api/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── fabiankaraben/
│   │               └── jsonapi/
│   │                   ├── SimpleJsonServer.java  # Main application class
│   │                   ├── JsonHandler.java       # Handles HTTP requests
│   │                   └── User.java              # Model class
│   └── test/
│       └── java/
│           └── com/
│               └── fabiankaraben/
│                   └── jsonapi/
│                       ├── UserTest.java                      # JSON serialization unit tests
│                       └── SimpleJsonServerIntegrationTest.java # API integration tests
└── pom.xml
```

## How to Use

1. **Build the project**:
   ```bash
   mvn clean package
   ```

2. **Run the server**:
   You can run the server by executing the compiled main class:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.fabiankaraben.jsonapi.SimpleJsonServer"
   ```
   Or run the main method in `src/main/java/com/fabiankaraben/jsonapi/SimpleJsonServer.java` from your IDE.

   The server will start on port **8080**.

3. **Test the endpoint**:
   Open your browser or use `curl`:
   ```bash
   curl -v http://localhost:8080/api/user
   ```
   Expected output:
   ```json
   {"id":1,"name":"John Doe","email":"john.doe@example.com"}
   ```

## How to Run Tests

Run the unit and integration tests using Maven:

```bash
mvn test
```
