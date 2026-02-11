# Webhook Delivery System

This is a backend mini-project in Java that accepts events and delivers payloads to registered callback URLs. It demonstrates a simple webhook delivery mechanism using Spring Boot and Spring WebFlux (WebClient).

## Requirements

- Java 17+
- Docker (optional, for running the application in a container)

## Project Structure

- `src/main/java`: Source code
- `src/test/java`: Integration tests using WireMock
- `Dockerfile`: Docker build configuration
- `docker-compose.yml`: Docker Compose configuration

## How to Run

### Using Gradle (Local)

1.  **Build and Run**:
    ```bash
    ./gradlew bootRun
    ```

### Using Docker

1.  **Run with Docker Compose**:
    ```bash
    docker compose up --build
    ```
    The application will be accessible at `http://localhost:8080`.

## API Usage

### 1. Register a Webhook

Register a callback URL for a specific event type.

**Request:**

```bash
curl -X POST http://localhost:8080/api/webhooks/register \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://webhook.site/your-unique-id",
    "eventType": "ORDER_CREATED"
  }'
```

**Response:**

```json
{
  "id": "uuid-string",
  "url": "https://webhook.site/your-unique-id",
  "eventType": "ORDER_CREATED"
}
```

### 2. Trigger an Event

Trigger an event which will be delivered to all registered subscribers for that event type.

**Request:**

```bash
curl -X POST http://localhost:8080/api/webhooks/trigger \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "ORDER_CREATED",
    "payload": {
      "orderId": "12345",
      "amount": 99.99,
      "currency": "USD"
    }
  }'
```

**Response:**

`202 Accepted`

## Testing

This project includes integration tests that use **WireMock** to simulate a webhook receiver. The tests verify that when an event is triggered, the system correctly calls the registered webhook URL with the expected payload.

To run the tests, execute the following command:

```bash
./gradlew clean test
```

The test output will show events `passed`, `skipped`, and `failed`.
