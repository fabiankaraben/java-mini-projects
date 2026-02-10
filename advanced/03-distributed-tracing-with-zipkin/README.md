# Distributed Tracing with Zipkin

This mini-project demonstrates a microservices architecture with distributed tracing using **Spring Boot 3**, **Micrometer Tracing**, and **Zipkin**. It consists of two services (`service-a` and `service-b`) where `service-a` calls `service-b`, and the entire trace is visualized in Zipkin.

## Requirements

- **Java 17+**
- **Maven**
- **Docker** & **Docker Compose** (for running Zipkin and the services)

## Project Structure

- **service-a**: The Caller Service. Exposes `/hello`, calls `service-b`.
- **service-b**: The Callee Service. Exposes `/greet`.
- **docker-compose.yml**: Orchestrates `service-a`, `service-b`, and `zipkin`.

## How to Run

### 1. Build the Applications

Navigate to the project root (`advanced/03-distributed-tracing-with-zipkin`) and run:

```bash
mvn clean package -DskipTests
```

### 2. Start Services with Docker Compose

```bash
docker compose up --build
```

This will start:
- **Zipkin** on port `9411`
- **Service A** on port `8081`
- **Service B** on port `8082`

### 3. Usage Examples

Call **Service A**:

```bash
curl http://localhost:8081/hello
```

**Expected Output:**
```
Service A calling -> Hello from Service B!
```

### 4. View Traces in Zipkin

Open your browser and navigate to:

[http://localhost:9411/zipkin/](http://localhost:9411/zipkin/)

- Click "Run Query" to see recent traces.
- You should see a trace spanning `service-a` and `service-b`.
- Click on the trace to see the timeline and latency details.

## Running Tests

The project includes integration tests that verify span reporting using **Testcontainers** (Zipkin) and **WireMock** (mocking Service B).

Run the tests using Maven:

```bash
mvn clean test
```

*Note: You need a running Docker environment for Testcontainers to work.*

## Implementation Details

- **Micrometer Tracing** (Bridge Brave) is used for tracing instrumentation.
- **Zipkin Reporter** sends spans to the Zipkin server.
- `restTemplate` in `ServiceA` is automatically instrumented to propagate trace context (headers like `traceparent`, `b3`) to `ServiceB`.
