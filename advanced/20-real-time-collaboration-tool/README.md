# Real-Time Collaboration Tool

This mini-project is a backend in Java using **Operational Transformation (OT)** over WebSockets to enable real-time collaborative text editing. It ensures eventual consistency between multiple clients editing the same document concurrently.

## Requirements

- Java 17+
- Gradle (wrapper included)
- Docker (optional, for containerized run)

## Features

- **WebSocket Server**: Handles real-time communication with clients.
- **Operational Transformation (OT)**: Transforms concurrent operations (INSERT, DELETE) to maintain consistency.
- **Simulation Tests**: Verifies consistency under concurrent editing scenarios.

## Project Structure

- `src/main/java/com/example/collaboration/model`: Data models (Document, Operation).
- `src/main/java/com/example/collaboration/service`: OT logic implementation.
- `src/main/java/com/example/collaboration/controller`: WebSocket and REST endpoints.
- `src/test/java/com/example/collaboration`: Simulation tests.

## How to Run

### Locally

1.  Navigate to the project directory:
    ```bash
    cd advanced/20-real-time-collaboration-tool
    ```
2.  Run the application using Gradle:
    ```bash
    ./gradlew bootRun
    ```
    The server will start on `http://localhost:8080`.

### With Docker

1.  Build and run using Docker Compose:
    ```bash
    docker compose up --build
    ```

## How to Use

The application exposes a WebSocket endpoint at `/ws` and uses STOMP messaging.

- **Connect**: `ws://localhost:8080/ws`
- **Subscribe**: `/topic/updates` (to receive transformed operations from other clients)
- **Send**: `/app/edit` (to send an operation)

### API Endpoints

- **Get Current Document Content**:
    ```bash
    curl http://localhost:8080/api/document
    ```

### Example Client Logic (Conceptual)

1. Connect to WebSocket.
2. Fetch initial document state via REST.
3. On local edit:
   - Apply locally.
   - Send `Operation` to `/app/edit`.
4. On receiving message from `/topic/updates`:
   - Transform incoming operation against pending local operations (if any).
   - Apply transformed operation to local document.

## Running Tests

To run the simulation tests and verify OT logic:

```bash
./gradlew clean test
```

The output will show the test execution details (passed/skipped/failed).
