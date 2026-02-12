# Code Execution Sandbox

This mini-project demonstrates a secure code execution sandbox using the **Docker Java API**. It allows running untrusted user code (Python by default) in isolated, ephemeral Docker containers with resource limits (memory, CPU) and network restrictions.

## Requirements

- Java 17+
- Docker & Docker Compose (Docker daemon must be running)
- Gradle

## Project Structure

- `src/main/java`: Spring Boot application source code.
- `src/test/java`: Integration tests.
- `Dockerfile`: Multi-stage build for the application.
- `docker-compose.yml`: Configuration to run the app with access to the host Docker socket.

## How it works

1.  Receives code via REST API.
2.  Spins up a `python:3.9-slim` container using `docker-java`.
3.  Mounts/Passes code to the container (via base64 decoding in the command to avoid file mounting complexity for this simple example).
4.  Applies limits:
    -   **Network**: `none` (No internet access)
    -   **Memory**: 50MB
    -   **CPU Quota**: 50%
    -   **Timeout**: 5 seconds
5.  Captures `stdout`, `stderr`, and exit code.
6.  Cleans up the container.

## How to Run

### Using Docker Compose (Recommended)

This ensures the application has access to the Docker socket.

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Running Tests

To run the integration tests (which verify timeout logic and malicious code isolation), run:

```bash
./gradlew clean test
```

*Note: Tests require the Docker daemon to be running on your machine.*

## API Usage

### Execute Code

**Endpoint**: `POST /api/sandbox/execute`

**Request Body**:
```json
{
  "code": "print('Hello World')",
  "language": "python"
}
```

**Response**:
```json
{
  "stdout": "Hello World\n",
  "stderr": "",
  "exitCode": 0,
  "timeout": false
}
```

### Examples

**1. valid Code**
```bash
curl -X POST http://localhost:8080/api/sandbox/execute \
  -H "Content-Type: application/json" \
  -d '{"code": "print(2 + 2)"}'
```

**2. Infinite Loop (Timeout)**
```bash
curl -X POST http://localhost:8080/api/sandbox/execute \
  -H "Content-Type: application/json" \
  -d '{"code": "while True: pass"}'
```

**3. Malicious Code (Network Access)**
```bash
curl -X POST http://localhost:8080/api/sandbox/execute \
  -H "Content-Type: application/json" \
  -d '{"code": "import urllib.request; print(urllib.request.urlopen(\"http://google.com\").read())"}'
```
