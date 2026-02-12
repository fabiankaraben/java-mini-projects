# Chaos Engineering Tool

A Java-based Chaos Engineering tool that injects failures (latency, exceptions) into running JVM applications using a Java Agent. This tool allows you to test the resilience of your applications by simulating adverse conditions without changing the application code.

## Features

- **Latency Injection**: Introduce artificial delays to methods to simulate network lag or slow services.
- **Exception Injection**: Throw specific exceptions from methods to simulate errors or crashes.
- **Configuration**: JSON-based configuration to target specific classes and methods.
- **Probability Control**: Define the rate (0.0 to 1.0) at which failures occur.
- **Non-Intrusive**: Works as a Java Agent, requiring no code changes to the target application.

## Requirements

- Java 17 or higher
- Docker and Docker Compose (optional, for containerized execution)
- Gradle (wrapper provided)

## Project Structure

- `src/main/java/com/example/chaos/agent`: Contains the Java Agent logic (`ChaosAgent`, `ChaosAdvice`).
- `src/main/java/com/example/chaos/config`: Configuration loading logic.
- `src/main/java/com/example/chaos/dummy`: A dummy service used for testing and demonstration.
- `src/main/java/com/example/chaos/demo`: A main application wrapper for the dummy service.
- `src/test/java`: Integration tests verifying the agent's behavior.

## Configuration

The agent is configured via a JSON file (default: `chaos-config.json`).

### Example Configuration

```json
{
  "targets": [
    {
      "className": "com.example.chaos.dummy.DummyService",
      "methodName": "sayHello",
      "failureType": "LATENCY",
      "latencyMs": 2000,
      "rate": 1.0
    },
    {
      "className": "com.example.chaos.dummy.DummyService",
      "methodName": "processData",
      "failureType": "EXCEPTION",
      "exceptionClass": "java.lang.RuntimeException",
      "rate": 0.5
    }
  ]
}
```

- **className**: The fully qualified name of the class to instrument.
- **methodName**: The name of the method to target.
- **failureType**: `LATENCY` or `EXCEPTION`.
- **latencyMs**: Duration of delay in milliseconds (only for `LATENCY`).
- **exceptionClass**: Fully qualified name of the exception to throw (only for `EXCEPTION`).
- **rate**: Probability of the failure occurring (0.0 to 1.0).

## How to Run

### Using Docker Compose (Recommended)

This project includes a `docker-compose.yml` that builds the agent and runs a dummy application with the agent attached.

1.  **Build and Run**:
    ```bash
    docker compose up --build
    ```

2.  **Observe Output**:
    You will see logs from the `chaos-app` container.
    - `sayHello` method will be delayed by 2 seconds (configured in `chaos-config.json`).
    - `processData` method will throw a `RuntimeException`.

    ```text
    chaos-app-1  | [ChaosAgent] Injecting failure: LATENCY into com.example.chaos.dummy.DummyService#sayHello
    chaos-app-1  | Hello World
    chaos-app-1  | [ChaosAgent] Injecting failure: EXCEPTION into com.example.chaos.dummy.DummyService#processData
    chaos-app-1  | Caught exception: Chaos Monkey injected exception!
    ```

### Running Locally with Gradle

1.  **Build the Project**:
    ```bash
    ./gradlew clean build
    ```

2.  **Run with Agent**:
    You need to package the jar and then run your application with the `-javaagent` flag.
    ```bash
    # Build the JAR
    ./gradlew jar

    # Run the Demo App with the Agent
    java -javaagent:build/libs/chaos-engineering-tool-1.0.0.jar \
         -cp build/libs/chaos-engineering-tool-1.0.0.jar \
         com.example.chaos.demo.DummyApp
    ```
    *Note: Ensure `chaos-config.json` is in the current working directory or set `CHAOS_CONFIG_FILE` environment variable.*

## Testing

The project includes integration tests that verify the agent's functionality by dynamically attaching it to the JVM.

Run the tests using Gradle:

```bash
./gradlew clean test
```

The test output will show the passed/failed status of the tests:

```text
com.example.chaos.ChaosIntegrationTest > testLatencyInjection() PASSED
com.example.chaos.ChaosIntegrationTest > testExceptionInjection() PASSED
```

## Dependencies

- **ByteBuddy**: Used for bytecode instrumentation and class transformation.
- **Jackson**: Used for parsing the JSON configuration.
- **JUnit 5**: Used for testing.
