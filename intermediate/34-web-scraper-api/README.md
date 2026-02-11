# Web Scraper API

This is a backend mini-project in Java using Spring Boot and **Jsoup** to extract specific data (title, meta tags, headers) from a provided URL.

## Requirements

- Java 17+
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure

- `src/main/java`: Source code
- `src/test/java`: Unit tests
- `Dockerfile`: Docker build configuration
- `docker-compose.yml`: Docker Compose configuration

## How to Run

### Local Execution (Gradle)

1.  Clone the repository and navigate to the project directory:
    ```bash
    cd intermediate/34-web-scraper-api
    ```

2.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

3.  The API will be available at `http://localhost:8080`.

### Docker Execution

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```

2.  The API will be available at `http://localhost:8080`.

## API Usage

### Scrape a URL

**Endpoint:** `GET /api/scrape`

**Parameters:**
- `url` (required): The URL to scrape.

**Example Request:**

```bash
curl "http://localhost:8080/api/scrape?url=https://example.com"
```

**Example Response:**

```json
{
  "title": "Example Domain",
  "metaTags": {
    "viewport": "width=device-width, initial-scale=1"
  },
  "headers": {
    "h1": [
      "Example Domain"
    ]
  }
}
```

## Testing

This project includes unit tests with mocked HTML content to verify the extraction logic.

To run the tests:

```bash
./gradlew clean test
```

The test output will show passed, skipped, and failed events.
