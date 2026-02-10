# Real-Time Notifications

This mini-project demonstrates a backend service in Java that pushes real-time notifications to clients using Server-Sent Events (SSE).

## Requirements

- Java 17 or higher
- Gradle 7.x or higher (Wrapper included)

## Features

- **Subscribe to Notifications**: Clients can subscribe to receive real-time updates via SSE.
- **Send Notifications**: An endpoint to trigger notifications to all connected clients.

## Usage

### 1. Start the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`.

### 2. Subscribe to Notifications (Client 1)

Open a terminal and run:

```bash
curl -N http://localhost:8080/api/notifications/subscribe
```

The connection will remain open, waiting for events.

### 3. Send a Notification (Client 2)

Open another terminal and trigger a notification:

```bash
curl -X POST http://localhost:8080/api/notifications/send \
     -H "Content-Type: text/plain" \
     -d "Hello, this is a real-time alert!"
```

You should see the message appear in the first terminal.

## Testing

Integration tests are included to verify the SSE subscription and notification broadcasting.

To run the tests:

```bash
./gradlew clean test
```

Test output will be logged to the console (passed, skipped, failed).
