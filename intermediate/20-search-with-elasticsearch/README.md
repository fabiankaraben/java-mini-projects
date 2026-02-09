# Search with Elasticsearch

This mini-project demonstrates how to implement a search service using Java, Spring Boot, and Elasticsearch.

## Requirements

-   Java 17
-   Docker and Docker Compose
-   Gradle

## Description

The project uses:
-   **Spring Data Elasticsearch** for indexing and searching documents.
-   **Testcontainers** for integration testing with a real Elasticsearch instance running in Docker.
-   **Docker Compose** to run the application and Elasticsearch together.

## How to Run

### Using Docker Compose

To run the entire stack (Application + Elasticsearch):

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Local Development

1.  Start Elasticsearch using Docker Compose:
    ```bash
    docker compose up elasticsearch -d
    ```
2.  Run the application with Gradle:
    ```bash
    ./gradlew bootRun
    ```

## API Usage

### Create a Product

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone",
    "description": "Latest model smartphone with high resolution camera",
    "price": 999.99
  }'
```

### Search Products by Name

```bash
curl "http://localhost:8080/api/products/search?query=Smartphone"
```

### Get All Products

```bash
curl http://localhost:8080/api/products
```

## Testing

To run the integration tests using Testcontainers:

```bash
./gradlew clean test
```

The tests will automatically spin up an Elasticsearch container, index some documents, and perform search queries to verify the results.
