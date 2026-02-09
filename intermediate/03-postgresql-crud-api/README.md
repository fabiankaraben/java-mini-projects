# PostgreSQL CRUD API

This mini-project is a simple REST API implemented in Java using Spring Boot, which performs CRUD operations on a `User` entity. It uses PostgreSQL for persistent storage and Testcontainers for integration testing.

## Requirements

- Java 17+
- Docker & Docker Compose
- Maven (optional, `mvnw` wrapper is not included but you can use installed `mvn`)

## Project Structure

- **Spring Boot**: Framework for the application.
- **Spring Data JPA**: For database interactions.
- **PostgreSQL**: Relational database.
- **Testcontainers**: For running integration tests against a real PostgreSQL instance in Docker.

## How to Run

### Using Docker Compose (Recommended)

To run the entire application (Database + API) using Docker Compose:

1.  Build the application JAR:
    ```bash
    mvn clean package -DskipTests
    ```
2.  Start the services:
    ```bash
    docker compose up --build
    ```

The API will be available at `http://localhost:8080`.

To stop the services:
```bash
docker compose down
```

### Running Tests

To run the integration tests:

```bash
mvn clean test
```

## API Usage

Here are some example `curl` commands to interact with the API.

### 1. Create a User

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'
```

### 2. Get All Users

```bash
curl http://localhost:8080/users
```

### 3. Get User by ID

```bash
curl http://localhost:8080/users/1
```

### 4. Update a User

```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "John Updated", "email": "john.updated@example.com"}'
```

### 5. Delete a User

```bash
curl -X DELETE http://localhost:8080/users/1
```
