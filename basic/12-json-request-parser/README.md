# JSON Request Parser

🔹 This is a basic backend in Java, parsing JSON from POST body and validating basic fields.

## Requirements

- Java 17+
- Gradle

## How to Run

1.  **Build and Run the Server:**

    ```bash
    ./gradlew run
    ```

    The server will start on port `8080`.

## API Usage

### Create User

**Endpoint:** `POST /api/user`

**Valid Request:**

```bash
curl -X POST http://localhost:8080/api/user \
     -H "Content-Type: application/json" \
     -d '{"name": "Alice", "age": 25, "email": "alice@example.com"}'
```

**Response (200 OK):**

```text
User received: User{name='Alice', age=25, email='alice@example.com'}
```

### Error Scenarios

**1. Invalid JSON:**

```bash
curl -v -X POST http://localhost:8080/api/user \
     -H "Content-Type: application/json" \
     -d '{invalid-json}'
```

**Response (400 Bad Request):** `Invalid JSON format`

**2. Validation Error (Missing Name):**

```bash
curl -v -X POST http://localhost:8080/api/user \
     -H "Content-Type: application/json" \
     -d '{"name": "", "age": 25, "email": "alice@example.com"}'
```

**Response (400 Bad Request):** `Validation Error: Name cannot be empty`

**3. Validation Error (Invalid Age):**

```bash
curl -v -X POST http://localhost:8080/api/user \
     -H "Content-Type: application/json" \
     -d '{"name": "Bob", "age": -5, "email": "bob@example.com"}'
```

**Response (400 Bad Request):** `Validation Error: Age must be greater than 0`

## Testing

This project uses **JUnit 5** for testing.

To run the tests:

```bash
./gradlew clean test
```
