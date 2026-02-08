# UUID Generator API

This mini-project is a simple backend in Java that generates unique IDs (UUIDs) on request using `java.util.UUID`.

## Requirements

- Java 17
- Maven 3.6+

## Project Structure

- `src/main/java`: Source code for the UUID Generator API
- `src/test/java`: Integration tests

## How to Run

1.  Navigate to the project directory:
    ```bash
    cd basic/43-uuid-generator-api
    ```

2.  Compile the project:
    ```bash
    mvn clean compile
    ```

3.  Run the application (you might need to create a main class runner or compile to jar, but here is how to run the class directly):
    ```bash
    mvn exec:java -Dexec.mainClass="com.fabiankaraben.uuidapi.UuidServer"
    ```
    The server will start on port `8080`.

## API Usage

### Generate a UUID

**Request:**

```bash
curl -v http://localhost:8080/api/uuid
```

**Response:**

```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000"
}
```

## Running Tests

To run the integration tests:

```bash
mvn clean test
```

This will run the `UuidServerIntegrationTest` which calls the API multiple times to ensure uniqueness and correct UUID format.
