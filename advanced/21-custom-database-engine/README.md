# Custom Database Engine

A simple append-only log database engine implemented in Java using Spring Boot. This project demonstrates the core concepts of a log-structured storage engine where writes are appended to the end of a file, and an in-memory index is maintained for fast lookups.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **Append-Only Storage**: All writes are appended to a log file (`data/db.log`), ensuring durability and sequential write performance.
- **In-Memory Index**: A `ConcurrentHashMap` maintains an index of keys to values (offsets/values) for O(1) read performance.
- **Crash Recovery**: On startup, the engine replays the log file to rebuild the in-memory index.
- **REST API**: Simple HTTP interface to `SET` and `GET` key-value pairs.

## Project Structure

- `src/main/java`: Source code.
  - `engine/StorageEngine.java`: The core storage logic handling file I/O and indexing.
  - `controller/DbController.java`: REST controller exposing the API.
- `src/test/java`: Unit and integration tests.

## How to Run

### Locally (Maven)

1.  **Build and Run**:
    ```bash
    mvn spring-boot:run
    ```
    The application will start on port `8080`.

2.  **Run Tests**:
    ```bash
    mvn clean test
    ```

### Using Docker

1.  **Build and Start**:
    ```bash
    docker compose up --build
    ```

2.  **Stop**:
    ```bash
    docker compose down
    ```

## Usage

Use `curl` to interact with the database.

### Set a Value
```bash
curl -X POST http://localhost:8080/api/db/mykey -H "Content-Type: text/plain" -d "Hello World"
```
**Response**: `Key set successfully`

### Get a Value
```bash
curl http://localhost:8080/api/db/mykey
```
**Response**: `Hello World`

### Update a Value
The engine supports updates by appending a new entry for the same key. The latest entry takes precedence.
```bash
curl -X POST http://localhost:8080/api/db/mykey -d "Updated Value"
curl http://localhost:8080/api/db/mykey
```
**Response**: `Updated Value`

## Testing

The project includes:
- **Unit Tests**: Verify the `StorageEngine` logic, including persistence, updates, and input validation.
- **Integration Tests**: Verify the REST API and handle concurrent read/write operations to ensure thread safety.

Run tests with:
```bash
mvn clean test
```
