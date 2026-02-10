# Event Sourcing with CQRS

This mini-project demonstrates a microservices backend in Java using the **Axon Framework** to implement the **CQRS (Command Query Responsibility Segregation)** pattern and **Event Sourcing**.

## Description

The project separates read and write operations:
- **Command Side (Write)**: Handled by `AccountAggregate`, which processes commands (Create, Deposit, Withdraw) and produces events.
- **Query Side (Read)**: Handled by `AccountProjection`, which listens to events and updates an `AccountSummary` read model stored in an H2 database.

Events are the source of truth for the system state.

## Requirements

- Java 17 or higher
- Docker and Docker Compose (for running the full application with Axon Server)
- Gradle (provided via wrapper)

## Project Structure

- `src/main/java/com/example/eventsourcing/core/api`: API definition (Commands, Events, Queries)
- `src/main/java/com/example/eventsourcing/command`: Command model (Aggregate)
- `src/main/java/com/example/eventsourcing/query`: Query model (Projections, Entities, Repository)
- `src/main/java/com/example/eventsourcing/gui`: REST Controller

## How to Run

### 1. Build the Application

```bash
./gradlew clean build
```

### 2. Run with Docker Compose

This project requires Axon Server. The easiest way to run the application and Axon Server is using Docker Compose.

```bash
docker compose up --build
```

This will start:
- **Axon Server** (Control Plane: 8024, gRPC: 8124)
- **Event Sourcing Application** (Port: 8080)

### 3. Usage (cURL Examples)

Once the application is running on `http://localhost:8080`, you can interact with it using `curl`.

#### Create a new Account
```bash
curl -X POST -H "Content-Type: application/json" -d "100.00" http://localhost:8080/accounts
# Returns: <ACCOUNT_ID> (e.g., 550e8400-e29b-41d4-a716-446655440000)
```

#### Deposit Money
Replace `<ACCOUNT_ID>` with the ID returned from the creation step.
```bash
curl -X PUT -H "Content-Type: application/json" -d "50.00" http://localhost:8080/accounts/<ACCOUNT_ID>/deposit
```

#### Withdraw Money
```bash
curl -X PUT -H "Content-Type: application/json" -d "30.00" http://localhost:8080/accounts/<ACCOUNT_ID>/withdraw
```

#### Get Account Summary (Query)
```bash
curl -X GET http://localhost:8080/accounts/<ACCOUNT_ID>
# Returns JSON: {"accountId":"<ACCOUNT_ID>", "balance":120.00}
```

## Testing

The project includes unit tests using **Axon Test Fixtures** to verify that aggregates correctly handle commands and publish events.

To run the tests:

```bash
./gradlew clean test
```

Test output will show `passed`, `skipped`, and `failed` events in the console.

## Dependencies

- **Axon Framework**: For Event Sourcing and CQRS
- **Spring Boot**: Application framework
- **Spring Data JPA & H2**: For the query model database
- **JUnit 5 & Axon Test**: For testing
