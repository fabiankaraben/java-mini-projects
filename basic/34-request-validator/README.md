# Request Validator

This is a basic backend in Java that validates request bodies using Bean Validation (Jakarta Validation). It ensures that incoming JSON data meets specific criteria before processing it.

## Requirements

*   Java 17 or higher
*   Gradle

## How to Run

1.  **Start the application:**

    ```bash
    ./gradlew bootRun
    ```

    The application will start on port 8080.

## Usage

You can test the API using `curl`.

### 1. Create a Valid User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "age": 25
  }'
```

**Response (200 OK):**

```json
{
  "message": "User created successfully",
  "username": "john_doe"
}
```

### 2. Validation Error (Invalid Data)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jo",
    "email": "invalid-email",
    "age": 16
  }'
```

**Response (400 Bad Request):**

```json
{
  "username": "Username must be between 3 and 20 characters",
  "email": "Email should be valid",
  "age": "Age must be at least 18"
}
```

## Running Tests

To run the unit tests and check for `ConstraintViolationException` handling:

```bash
./gradlew clean test
```

The test output will show passed, skipped, and failed events.
