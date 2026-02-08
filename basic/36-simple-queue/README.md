# Simple Queue with BlockingQueue

This is a basic backend application in Java that implements a simple task queue system using `BlockingQueue`. It processes tasks in a FIFO (First-In-First-Out) order via an HTTP API.

## Project Structure

- **QueueManager**: Manages the `BlockingQueue` and handles task submission and retrieval.
- **Worker**: Runs in a separate thread, consuming tasks from the queue and processing them.
- **QueueServer**: An HTTP server that accepts POST requests to submit tasks.
- **Main**: The entry point that initializes the components and starts the application.

## Requirements

- Java 11 or higher
- Gradle

## How to Run

1. **Build and Run the Application**:
   ```bash
   ./gradlew run
   ```
   The server will start on port 8080.

## How to Use

You can submit tasks to the queue using `curl`.

### Submit a Task

**Endpoint**: `POST /task`

**Example**:
```bash
curl -X POST -d "Process this data" http://localhost:8080/task
```

**Response**:
```
Task submitted with ID: <UUID>
```

The worker will pick up the task and process it (simulated by a 100ms delay and logging). Check the application logs to see the processing status.

## Testing

The project includes integration tests that verify the task submission and processing flow.

To run the tests, use the following command:

```bash
./gradlew clean test
```

The build configuration is set to show detailed test events ("passed", "skipped", "failed") in the console output.

## Implementation Details

- **Concurrency**: Uses `java.util.concurrent.BlockingQueue` (specifically `LinkedBlockingQueue`) for thread-safe queue management.
- **HTTP Server**: Uses `com.sun.net.httpserver.HttpServer` for handling HTTP requests without external frameworks like Spring or Javalin.
- **Logging**: Uses SLF4J with Logback for logging task processing events.
