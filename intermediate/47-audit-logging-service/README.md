# Audit Logging Service

This is a backend mini-project in Java that demonstrates **Aspect Oriented Programming (AOP)** to automatically log method calls and changes in a service.

## Requirements

- **Java 17+**
- **Maven**
- **Docker** and **Docker Compose** (for running the application and database)

## Features

- **Spring AOP**: Intercepts method calls annotated with `@Auditable`.
- **Automatic Logging**: Captures method name, arguments, return value, execution time, and timestamp.
- **Database Storage**: Stores audit logs in a PostgreSQL database.
- **REST API**: Exposes endpoints to trigger actions and view logs.

## Project Structure

- `src/main/java/com/example/audit/AuditAspect.java`: The AOP aspect that intercepts method calls.
- `src/main/java/com/example/audit/Auditable.java`: Custom annotation to mark methods for auditing.
- `src/main/java/com/example/audit/BusinessService.java`: Service with methods demonstrating audited and non-audited behavior.
- `src/main/java/com/example/audit/AuditLog.java`: Entity representing the audit log record.

## How to Run

### Using Docker Compose (Recommended)

Since this project requires a PostgreSQL database, the easiest way to run it is using Docker Compose.

1.  Build and start the services:
    ```bash
    docker compose up --build
    ```

2.  The application will be available at `http://localhost:8080`.

### Running Locally (Requires local PostgreSQL)

If you prefer to run locally without Docker Compose for the app itself (but still need a DB):
1.  Start a PostgreSQL database (e.g., via Docker):
    ```bash
    docker run --name audit-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=auditdb -p 5432:5432 -d postgres:15-alpine
    ```
2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

## API Usage

### 1. Trigger an Audited Action

**Endpoint:** `POST /api/audit/action`

```bash
curl -X POST "http://localhost:8080/api/audit/action?user=alice&action=login"
```

**Response:**
```text
Action 'login' performed by alice
```

### 2. Trigger Another Audited Action

**Endpoint:** `POST /api/audit/settings`

```bash
curl -X POST "http://localhost:8080/api/audit/settings?key=theme&value=dark"
```

**Response:**
```text
Settings updated: theme=dark
```

### 3. View Audit Logs

**Endpoint:** `GET /api/audit/logs`

```bash
curl http://localhost:8080/api/audit/logs
```

**Response (JSON):**
```json
[
  {
    "id": 1,
    "methodName": "performCriticalAction",
    "arguments": "[alice, login]",
    "returnValue": "Action 'login' performed by alice",
    "timestamp": "2023-10-27T10:00:00.123456",
    "executionTimeMs": 105
  },
  {
    "id": 2,
    "methodName": "updateSettings",
    "arguments": "[theme, dark]",
    "returnValue": "Settings updated: theme=dark",
    "timestamp": "2023-10-27T10:01:00.654321",
    "executionTimeMs": 2
  }
]
```

## Testing

This project includes integration tests that use **Testcontainers** to spin up a PostgreSQL database within the test environment.

To run the tests:

```bash
mvn clean test
```

**Note:** You must have Docker running for the tests to pass, as they rely on Testcontainers to provide the PostgreSQL instance.
