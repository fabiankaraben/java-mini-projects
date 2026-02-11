# Sentiment Analysis API

This is a simple Java backend project that provides a Sentiment Analysis API using a basic word list approach. It analyzes text and returns a sentiment score (POSITIVE, NEGATIVE, or NEUTRAL) along with a confidence level.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure

- `src/main/java`: Source code for the Spring Boot application.
- `src/test/java`: Unit and integration tests.
- `Dockerfile`: Multi-stage Docker build configuration.
- `docker-compose.yml`: Docker Compose configuration.

## How to Run

### Using Maven

1.  **Build the project:**

    ```bash
    mvn clean package
    ```

2.  **Run the application:**

    ```bash
    java -jar target/sentiment-analysis-api-0.0.1-SNAPSHOT.jar
    ```

    The application will start on `http://localhost:8080`.

### Using Docker Compose

1.  **Build and start the container:**

    ```bash
    docker compose up --build
    ```

    The API will be accessible at `http://localhost:8080`.

## API Usage

### Analyze Sentiment

**Endpoint:** `POST /api/sentiment/analyze`

**Request Body:**

```json
{
  "text": "I love this product, it is amazing!"
}
```

**cURL Example:**

```bash
curl -X POST http://localhost:8080/api/sentiment/analyze \
  -H "Content-Type: application/json" \
  -d '{"text": "I love this product, it is amazing!"}'
```

**Response:**

```json
{
  "score": 2,
  "sentiment": "POSITIVE",
  "confidence": 0.3333333333333333
}
```

**Negative Example:**

```bash
curl -X POST http://localhost:8080/api/sentiment/analyze \
  -H "Content-Type: application/json" \
  -d '{"text": "This is terrible and I hate it."}'
```

**Response:**

```json
{
  "score": -2,
  "sentiment": "NEGATIVE",
  "confidence": 0.2857142857142857
}
```

## Running Tests

To run the unit and integration tests, use the following command:

```bash
mvn clean test
```
