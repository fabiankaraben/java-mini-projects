# MongoDB Document Store

This mini-project is a **Spring Boot** application that uses **MongoDB** as a NoSQL document store. It demonstrates how to perform CRUD operations on documents using Spring Data MongoDB.

## Requirements

- Java 17+
- Docker & Docker Compose (for running MongoDB)

## Project Structure

- **Spring Boot**: Backend framework
- **MongoDB**: NoSQL Database
- **Gradle**: Dependency Management
- **Testcontainers**: Integration testing with MongoDB

## How to Run

### Using Docker Compose (Recommended)

1. Build the application:
   ```bash
   ./gradlew build -x test
   ```
2. Start the application and MongoDB:
   ```bash
   docker compose up --build
   ```
   The application will be accessible at `http://localhost:8080`.

### Running Locally (Requires local MongoDB)

If you have MongoDB running locally on port 27017:

```bash
./gradlew bootRun
```

## API Usage

### Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop", "description": "High performance laptop", "price": 1200.00}'
```

### Get All Products

```bash
curl http://localhost:8080/api/products
```

### Get Product by ID

Replace `{id}` with the actual ID returned from creation.

```bash
curl http://localhost:8080/api/products/{id}
```

### Update a Product

```bash
curl -X PUT http://localhost:8080/api/products/{id} \
  -H "Content-Type: application/json" \
  -d '{"name": "Laptop Pro", "description": "Upgraded laptop", "price": 1500.00}'
```

### Delete a Product

```bash
curl -X DELETE http://localhost:8080/api/products/{id}
```

## Testing

This project uses **Testcontainers** to spin up a MongoDB container for integration tests.

To run the tests:

```bash
./gradlew clean test
```

The test output will show passed/failed events.
