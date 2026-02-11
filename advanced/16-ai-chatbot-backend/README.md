# AI Chatbot Backend

This mini-project demonstrates how to build an AI Chatbot backend in Java using **LangChain4j** and **Spring Boot**. It integrates with Large Language Models (LLMs) like OpenAI to provide chat capabilities.

## Requirements

- Java 17 or higher
- Docker and Docker Compose (optional, for containerized execution)
- OpenAI API Key (optional, defaults to `demo` which works with LangChain4j's demo model, or use a real key)

## Project Structure

- `src/main/java`: Source code for the Spring Boot application.
- `src/test/java`: Integration tests using WireMock.
- `Dockerfile`: Multi-stage Docker build file.
- `docker-compose.yml`: Docker Compose configuration.

## Getting Started

### 1. Build and Run Tests

The project uses Gradle for dependency management and testing. Integration tests use **WireMock** to simulate the OpenAI API, ensuring that tests run without needing a real API key or internet connection.

```bash
./gradlew clean test
```

You should see output indicating that tests passed, including `events "passed", "skipped", "failed"` logging.

### 2. Run the Application

You can run the application locally using Gradle:

```bash
./gradlew bootRun
```

Or using Docker Compose:

```bash
docker compose up --build
```

To use a real OpenAI API Key:

```bash
export OPENAI_API_KEY=your-api-key
./gradlew bootRun
```

Or with Docker Compose:

```bash
OPENAI_API_KEY=your-api-key docker compose up --build
```

### 3. Usage

Once the application is running (default port 8080), you can interact with the chatbot using `curl`.

#### Send a Message

```bash
curl -X POST http://localhost:8080/api/chat \
     -H "Content-Type: text/plain" \
     -d "Hello, how are you?"
```

**Response:**

```text
I am an AI language model, so I don't have feelings, but I'm here to help you! How can I assist you today?
```

## Technologies Used

- **Java 17+**: Programming language.
- **Spring Boot 3.4.1**: Application framework.
- **LangChain4j 0.36.2**: Java framework for LLM integration.
- **Gradle**: Build tool.
- **WireMock**: Tool for mocking HTTP APIs in integration tests.
