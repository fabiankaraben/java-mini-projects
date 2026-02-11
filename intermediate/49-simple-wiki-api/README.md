# Simple Wiki API

This is a backend implementation of a Simple Wiki API using Java and Spring Boot. It supports creating, updating, and retrieving wiki pages, with a focus on version control. Every update to a page creates a new version, preserving the history of changes.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional, for running the application in a container)

## Project Structure

- **Language**: Java 17
- **Framework**: Spring Boot 3.4.1
- **Build Tool**: Maven
- **Database**: H2 (In-memory)
- **Dependencies**: Spring Data JPA, Spring Web, H2 Database

## Features

- **Create Page**: Create a new wiki page with a unique slug.
- **Update Page**: Update an existing page. Each update creates a new version.
- **Get Page**: Retrieve metadata of a page.
- **Get Latest Content**: Retrieve the content of the latest version of a page.
- **Get History**: Retrieve the version history of a page.
- **Get Version**: Retrieve the content of a specific version.

## How to Run

### Using Maven

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

### Using Docker Compose

```bash
docker compose up --build
```

## Usage Examples (curl)

### 1. Create a Page

```bash
curl -X POST http://localhost:8080/api/pages \
  -H "Content-Type: application/json" \
  -d '{
    "slug": "hello-world",
    "title": "Hello World",
    "content": "This is the first version of the Hello World page."
  }'
```

### 2. Update the Page (Create Version 2)

```bash
curl -X PUT http://localhost:8080/api/pages/hello-world \
  -H "Content-Type: application/json" \
  -d '{
    "content": "This is the updated content (Version 2)."
  }'
```

### 3. Get Latest Content

```bash
curl http://localhost:8080/api/pages/hello-world/content
```

### 4. Get Version History

```bash
curl http://localhost:8080/api/pages/hello-world/history
```

### 5. Get Specific Version

```bash
curl http://localhost:8080/api/pages/hello-world/versions/1
```

## Testing

The project includes integration tests that verify the versioning logic and history integrity.

To run the tests:

```bash
./mvnw clean test
```

The tests cover:
- Creating a page.
- Updating it multiple times.
- Verifying that the latest content matches the last update.
- Verifying that the history contains all versions in the correct order.
- Verifying retrieval of specific past versions.
