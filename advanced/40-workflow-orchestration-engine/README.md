# Workflow Orchestration Engine

A simple backend service for defining and executing Directed Acyclic Graphs (DAGs) of tasks.

## Requirements
- Java 17+
- Docker & Docker Compose (optional, for containerized execution)

## Features
- **DAG Execution**: Supports complex dependencies (linear, parallel, branching).
- **Cycle Detection**: Prevents submission of cyclic graphs.
- **Asynchronous Execution**: Workflows run in the background.
- **REST API**: Submit workflows and check status.

## Project Structure
- `src/main/java`: Source code
- `src/test/java`: Unit tests
- `Dockerfile` & `docker-compose.yml`: Containerization

## running Tests
To run the unit tests:
```bash
./gradlew clean test
```
The test output will show `passed`, `skipped`, and `failed` events.

## Running the Application
### Locally
```bash
./gradlew bootRun
```

### With Docker Compose
```bash
docker compose up --build
```

## API Usage

### 1. Submit a Linear Workflow
A simple sequence: A -> B -> C

```bash
curl -X POST http://localhost:8080/api/workflows \
  -H "Content-Type: application/json" \
  -d '{
    "tasks": [
      { "id": "task1", "type": "LOG", "payload": "Step 1" },
      { "id": "task2", "type": "LOG", "payload": "Step 2", "dependencies": ["task1"] },
      { "id": "task3", "type": "LOG", "payload": "Step 3", "dependencies": ["task2"] }
    ]
  }'
```

### 2. Submit a Parallel Workflow
A starts, then B and C run in parallel, then D waits for both.

```bash
curl -X POST http://localhost:8080/api/workflows \
  -H "Content-Type: application/json" \
  -d '{
    "tasks": [
      { "id": "A", "type": "LOG", "payload": "Start" },
      { "id": "B", "type": "LOG", "payload": "Left Branch", "dependencies": ["A"] },
      { "id": "C", "type": "LOG", "payload": "Right Branch", "dependencies": ["A"] },
      { "id": "D", "type": "LOG", "payload": "Join", "dependencies": ["B", "C"] }
    ]
  }'
```

### 3. Check Workflow Status
Replace `{id}` with the ID returned from the submission.

```bash
curl http://localhost:8080/api/workflows/{id}
```
