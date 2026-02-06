# Query Parameter Parser

This is a basic backend mini-project in Java using `com.sun.net.httpserver`. It parses query parameters from a GET request URL and returns them as a JSON object.

## Features

- **Query Parsing**: Manually parses query strings, handling URL decoding (e.g., `%20` -> ` `).
- **JSON Response**: Returns the parsed parameters as a JSON object using the **Jackson** library.
- **Testing**:
  - Unit tests for the parsing logic, covering various edge cases (empty, null, encoded values).
  - Integration tests verifying the API endpoint `/api/parse`.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

```
basic/05-query-parameter-parser/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── fabiankaraben/
│   │               └── queryparser/
│   │                   ├── SimpleHttpServer.java  # Main application class
│   │                   ├── QueryHandler.java      # HTTP Handler
│   │                   └── QueryParser.java       # Parsing logic
│   └── test/
│       └── java/
│           └── com/
│               └── fabiankaraben/
│                   └── queryparser/
│                       ├── QueryParserTest.java                # Unit tests
│                       └── SimpleHttpServerIntegrationTest.java # Integration tests
├── pom.xml
└── README.md
```

## How to Use

1. **Build the project**:
   ```bash
   mvn clean package
   ```

2. **Run the server**:
   You can run the server using the `exec:java` goal:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.fabiankaraben.queryparser.SimpleHttpServer"
   ```
   
   The server will start on port **8080**.

3. **Test the endpoint**:
   Use `curl` to send GET requests with query parameters:

   **Basic Example**:
   ```bash
   curl -v "http://localhost:8080/api/parse?name=John&age=30"
   ```
   Expected JSON Output:
   ```json
   {"name":"John","age":"30"}
   ```

   **URL Encoded Example**:
   ```bash
   curl -v "http://localhost:8080/api/parse?message=Hello%20World&symbol=%24"
   ```
   Expected JSON Output:
   ```json
   {"message":"Hello World","symbol":"$"}
   ```

## How to Run Tests

Run the unit and integration tests using Maven:

```bash
mvn test
```
