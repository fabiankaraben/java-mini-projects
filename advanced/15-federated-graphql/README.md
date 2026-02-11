# Federated GraphQL

This mini-project demonstrates a microservices backend in Java using **Apollo Federation** with Spring GraphQL. It consists of two subgraphs (`author-service` and `book-service`) and uses **Apollo Router** as the federation gateway to stitch them into a single supergraph.

## Project Structure

- **author-service**: A Spring Boot subgraph service managing Author data.
- **book-service**: A Spring Boot subgraph service managing Book data. It extends the `Author` type to add a list of books.
- **integration-tests**: Integration tests verifying the federation logic using Testcontainers.
- **docker-compose.yml**: Orchestration for running the subgraphs, Apollo Rover (for schema composition), and Apollo Router (gateway).

## Requirements

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (for running the full stack and integration tests)

## How to Run

### 1. Build the Project

Build the Java services using Maven:

```bash
mvn clean package
```

### 2. Run with Docker Compose

This project uses Docker Compose to spin up the subgraphs, compose the schema using Apollo Rover, and start the Apollo Router.

```bash
docker compose up --build
```

Once running:
- **Apollo Router (Gateway)**: `http://localhost:4000/`
- **Author Service**: `http://localhost:8081/graphql`
- **Book Service**: `http://localhost:8082/graphql`

### 3. Query the Supergraph

You can query the Apollo Router at `http://localhost:4000/`.

**Example Query (CURL):**

Retrieve books and their authors (spanning both services):

```bash
curl -X POST -H "Content-Type: application/json" \
     --data '{ "query": "{ books { id title author { name } } }" }' \
     http://localhost:4000/
```

**Response:**

```json
{
  "data": {
    "books": [
      {
        "id": "book-1",
        "title": "Effective Java",
        "author": { "name": "Joshua Bloch" }
      },
      ...
    ]
  }
}
```

**Example: Get Author and their Books:**

```bash
curl -X POST -H "Content-Type: application/json" \
     --data '{ "query": "{ authorById(id: \"author-1\") { name books { title } } }" }' \
     http://localhost:4000/
```

## Testing

The project includes integration tests that use **Testcontainers** to spin up the subgraphs, run Apollo Rover to compose the supergraph schema on the fly, and start Apollo Router to verify federation.

To run the tests:

```bash
mvn clean test
```

*Note: Docker must be running for the integration tests to work.*

## Implementation Details

- **Spring Boot 3.4.1**
- **Spring GraphQL**
- **Apollo Federation JVM Support** (`com.apollographql.federation:federation-graphql-java-support`)
- **Apollo Router** (Gateway)
- **Apollo Rover** (Schema Composition)
