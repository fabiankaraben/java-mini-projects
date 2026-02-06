# Hello World HTTP Server

This is a simple backend mini-project in Java using `com.sun.net.httpserver`. It serves a single GET endpoint that returns a "Hello World" message, demonstrating basic HTTP server implementation, error handling, and logging without external frameworks like Spring or Jakarta EE.

## Features

- **Simple HTTP Server**: Uses Java's built-in `com.sun.net.httpserver`.
- **GET Endpoint**: Responds with "Hello World" on the root path `/`.
- **Error Handling**: Returns `405 Method Not Allowed` for non-GET requests.
- **Logging**: Basic logging of incoming requests and server status.
- **Testing**: Includes unit tests with JUnit 5 and Mockito, and integration tests using `java.net.http.HttpClient`.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

```
basic/01-hello-world-http-server/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── fabiankaraben/
│   │               └── http/
│   │                   ├── SimpleHttpServer.java  # Main application class
│   │                   └── HelloHandler.java      # Request handler logic
│   └── test/
│       └── java/
│           └── com/
│               └── fabiankaraben/
│                   └── http/
│                       ├── HelloHandlerTest.java              # Unit tests
│                       └── SimpleHttpServerIntegrationTest.java # Integration tests
└── pom.xml
```

## How to Use

1. **Build the project**:
   ```bash
   mvn clean package
   ```

2. **Run the server**:
   You can run the server using the `exec-maven-plugin` (if configured) or by running the main class directly. Since no specific exec plugin is configured, you can run the compiled class:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.fabiankaraben.http.SimpleHttpServer"
   ```
   Or simply run the main method in `src/main/java/com/fabiankaraben/http/SimpleHttpServer.java` from your IDE.

   The server will start on port **8080**.

3. **Test the endpoint**:
   Open your browser or use `curl`:
   ```bash
   curl -v http://localhost:8080/
   ```
   Expected output:
   ```
   Hello World
   ```

## How to Run Tests

Run the unit and integration tests using Maven:

```bash
mvn test
```

This will execute:
- `HelloHandlerTest`: Unit tests verifying logic with mocks.
- `SimpleHttpServerIntegrationTest`: Integration tests spinning up the server and making real HTTP requests.
