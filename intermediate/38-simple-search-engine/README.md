# Simple Search Engine

This is a backend mini-project in Java that implements a basic **Inverted Index** to search text documents. It provides a REST API to index documents and perform keyword-based searches.

## Project Structure

- **Language**: Java 25 (Code compatible with Java 17)
- **Framework**: Spring Boot 3.4.1
- **Build Tool**: Gradle
- **Containerization**: Docker & Docker Compose

## Requirements

- Java 17 or higher (Java 25 is used in the environment)
- Gradle (Wrapper included)
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **In-Memory Inverted Index**: Maps words to document IDs for fast retrieval.
- **REST API**:
  - `POST /api/search/index`: Index a new document.
  - `GET /api/search?query={terms}`: Search for documents containing the terms.
- **Boolean Search**: Supports implicit AND search (documents must contain all query terms).

## How to Run

### Using Gradle

1. **Build and Run**:
   ```bash
   ./gradlew bootRun
   ```
   The application will start on `http://localhost:8080`.

2. **Run Tests**:
   ```bash
   ./gradlew clean test
   ```
   Tests are configured to show detailed logging (passed, skipped, failed).

### Using Docker

1. **Build and Start**:
   ```bash
   docker compose up --build
   ```

2. **Stop**:
   ```bash
   docker compose down
   ```

## Usage Examples (curl)

### 1. Index Documents

Index a document about Java:
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"id": "1", "content": "Java is a high-level, class-based, object-oriented programming language."}' \
  http://localhost:8080/api/search/index
```

Index a document about Spring Boot:
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"id": "2", "content": "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications."}' \
  http://localhost:8080/api/search/index
```

Index a document about Python:
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"id": "3", "content": "Python is a high-level, general-purpose programming language."}' \
  http://localhost:8080/api/search/index
```

### 2. Search Documents

Search for "Java":
```bash
curl "http://localhost:8080/api/search?query=Java"
# Output: ["1"]
```

Search for "programming language" (implicit AND):
```bash
curl "http://localhost:8080/api/search?query=programming%20language"
# Output: ["1", "3"]
```

Search for "Spring" or "Boot":
```bash
curl "http://localhost:8080/api/search?query=Spring%20Boot"
# Output: ["2"]
```

## Implementation Details

The core of the search engine is the `InvertedIndexService`. It maintains a `ConcurrentHashMap` where keys are unique tokens (words) and values are sets of document IDs.

- **Tokenization**: Splits text by whitespace.
- **Normalization**: Converts to lowercase and removes non-alphanumeric characters.
- **Search Logic**: intersection of document sets for all terms in the query (AND logic).
