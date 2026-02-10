# Config Management

This is a backend mini-project in Java using **Spring Cloud Config**, demonstrating how to load configuration from a central source.
It uses the `native` profile to load configuration files from the local classpath, suitable for testing and simple deployments.

## Requirements

- Java 17 or higher
- Gradle 7.x+ (Wrapper included)

## Features

- **Spring Cloud Config Server**: Centralized configuration management.
- **Native Profile**: Loads configuration from `src/main/resources/config`.
- **Profiles**: Supports different profiles (e.g., `default`, `dev`).

## How to Use

### Running the Server

1.  Build the application:
    ```bash
    ./gradlew build
    ```
2.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

The server will start on port `8888`.

### Fetching Configuration (Curl Examples)

To fetch the configuration for the `client-service` application with the `default` profile:

```bash
curl http://localhost:8888/client-service/default
```

To fetch the configuration for the `client-service` application with the `dev` profile:

```bash
curl http://localhost:8888/client-service/dev
```

You should see a JSON response containing the property sources and values.

## Testing

To run the integration tests:

```bash
./gradlew clean test
```

The tests verify that the application starts up correctly and serves the configuration properties from the native backend.
