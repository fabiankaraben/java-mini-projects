# Form Data Handler

This is a basic backend mini-project in Java using `com.sun.net.httpserver`. It handles `POST` requests with `application/x-www-form-urlencoded` data, parses the key-value pairs, and echoes them back in the response.

## Features

- **Form Data Parsing**: Manually parses `application/x-www-form-urlencoded` request bodies.
- **POST Endpoint**: Listens on `/api/form` for POST requests.
- **Echo Response**: Returns the parsed key-value pairs as a plain text response.
- **Validation**: Enforces `Content-Type: application/x-www-form-urlencoded` and returns `415 Unsupported Media Type` if incorrect.
- **Testing**: Includes integration tests using `java.net.http.HttpClient` to verify form submission and parsing.

## Requirements

- Java 17 or higher
- Gradle

## Project Structure

```
basic/04-form-data-handler/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── fabiankaraben/
│   │               └── formdata/
│   │                   ├── SimpleHttpServer.java  # Main application class
│   │                   └── FormDataHandler.java   # Request handler logic
│   └── test/
│       └── java/
│           └── com/
│               └── fabiankaraben/
│                   └── formdata/
│                       └── FormDataHandlerIntegrationTest.java # Integration tests
├── build.gradle
├── settings.gradle
└── README.md
```

## How to Use

1. **Build the project**:
   ```bash
   ./gradlew build
   ```

2. **Run the server**:
   You can run the server using the `run` task provided by the application plugin:
   ```bash
   ./gradlew run
   ```
   
   The server will start on port **8080**.

3. **Test the endpoint**:
   You can use `curl` to send a POST request with form data:
   
   ```bash
   curl -v -X POST http://localhost:8080/api/form \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "name=John Doe&email=john.doe@example.com&message=Hello World"
   ```
   
   Expected output:
   ```
   Received Form Data:
   name: John Doe
   email: john.doe@example.com
   message: Hello World
   ```

   **Error Case**: Sending JSON or incorrect Content-Type
   ```bash
   curl -v -X POST http://localhost:8080/api/form \
        -H "Content-Type: application/json" \
        -d '{"name": "John"}'
   ```
   Expected output: `415 Unsupported Media Type`

## How to Run Tests

Run the integration tests using Gradle:

```bash
./gradlew clean test
```

This will execute `FormDataHandlerIntegrationTest`, which verifies:
- Correct parsing of URL-encoded form data.
- Handling of `405 Method Not Allowed` for GET requests.
- Handling of `415 Unsupported Media Type` for non-form requests.
