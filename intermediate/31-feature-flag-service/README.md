# Feature Flag Service

A simple Java backend implementation of a Feature Flag Service using custom logic (in-memory `ConcurrentHashMap`). This service allows managing feature toggles via REST APIs and demonstrates how feature flags can alter application behavior at runtime without redeploying.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized run)

## Project Structure

- `src/main/java/com/example/featureflag/FeatureFlagService.java`: Logic for managing feature flags.
- `src/main/java/com/example/featureflag/FeatureFlagController.java`: REST API to manage flags.
- `src/main/java/com/example/featureflag/DemoController.java`: A demo endpoint that changes behavior based on a feature flag.

## How to Run

### Using Maven

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### Using Docker Compose

```bash
docker compose up --build
```

## How to Use (curl examples)

### 1. List all feature flags

```bash
curl -X GET http://localhost:8080/api/features
```

Response:
```json
{
  "new-feature": false,
  "beta-ui": true
}
```

### 2. Check a specific feature flag

```bash
curl -X GET http://localhost:8080/api/features/new-feature
```

Response: `false`

### 3. Enable a feature flag

```bash
curl -X POST http://localhost:8080/api/features/new-feature
```

### 4. Verify behavior change in Demo endpoint

**Scenario:** We have a feature flag `new-message-feature`.

**Step 1:** Check status (default disabled or non-existent implies false)
```bash
curl -X GET http://localhost:8080/api/features/new-message-feature
```
Response: `false`

**Step 2:** Call the demo endpoint
```bash
curl -X GET http://localhost:8080/api/demo/message
```
Response: `Hello from the OLD feature.`

**Step 3:** Enable the feature
```bash
curl -X POST http://localhost:8080/api/features/new-message-feature
```

**Step 4:** Call the demo endpoint again
```bash
curl -X GET http://localhost:8080/api/demo/message
```
Response: `Hello from the NEW feature!`

### 5. Disable a feature flag

```bash
curl -X DELETE http://localhost:8080/api/features/new-message-feature
```

## Running Tests

This project includes integration tests that verify the feature flag logic and the controller endpoints.

To run the tests:

```bash
mvn clean test
```

The tests cover:
- Default flags initialization.
- Enabling and disabling flags via API.
- Verifying the `DemoController` behavior change when a flag is toggled.
