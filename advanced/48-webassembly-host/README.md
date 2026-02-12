# WebAssembly Host

This is a backend in Java using **Chicory** to execute WASM modules server-side. It exposes a REST API to invoke a simple WebAssembly function (addition).

## Requirements

- Java 17+
- Docker & Docker Compose (optional, for containerized run)

## How to Run

### Using Gradle (Local)

1.  Build and run the application:
    ```bash
    ./gradlew bootRun
    ```

### Using Docker Compose

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```

## Usage

The application loads a pre-compiled WASM module (`add.wasm`) that exports an `add` function.

### Add Two Numbers

**Request:**
```bash
curl "http://localhost:8080/api/wasm/add?a=10&b=20"
```

**Response:**
```json
{
  "operation": "add",
  "a": 10,
  "b": 20,
  "result": 30
}
```

## Running Tests

To run the unit tests (which verify the WASM execution):

```bash
./gradlew clean test
```

Expected output should show `passed`, `skipped`, and `failed` events.
