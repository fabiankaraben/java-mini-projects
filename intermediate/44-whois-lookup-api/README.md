# Whois Lookup API

This project is a Java Spring Boot application that provides a REST API to query WHOIS information for domain names using **Apache Commons Net**.

## Requirements

- Java 17+
- Gradle (wrapper included/recommended)
- Docker & Docker Compose (optional, for containerized execution)

## Features

- Query WHOIS information for a specific domain.
- Support for specifying a custom WHOIS server and port (useful for specific TLDs or testing).
- JSON response with parsed details (raw result included).

## Project Structure

```
src/
├── main/
│   ├── java/com/example/whois/
│   │   ├── WhoisApplication.java      # Main entry point
│   │   ├── WhoisController.java       # REST Controller
│   │   └── WhoisService.java          # Service using Apache Commons Net
│   └── resources/
│       └── application.properties     # Configuration
└── test/
    └── java/com/example/whois/
        └── WhoisIntegrationTest.java  # Integration tests with Mock Server
```

## How to Run

### Using Gradle

1. **Build and Run:**
   ```bash
   ./gradlew bootRun
   ```
   The application will start on port `8080`.

### Using Docker Compose

1. **Build and Start:**
   ```bash
   docker compose up --build
   ```

## API Usage

### Lookup Domain Info

**Endpoint:** `GET /api/whois`

**Parameters:**
- `domain` (required): The domain name to lookup (e.g., `example.com`).
- `server` (optional): Specific WHOIS server to query.
- `port` (optional): specific port to query (default 43).

**Example Request:**

```bash
curl "http://localhost:8080/api/whois?domain=google.com"
```

**Example Response:**

```json
{
  "result": "Domain Name: google.com\nRegistry Domain ID: 2138514_DOMAIN_COM-VRSN\n...",
  "domain": "google.com",
  "whois_server": "default"
}
```

**Custom Server Example:**

```bash
curl "http://localhost:8080/api/whois?domain=google.com&server=whois.verisign-grs.com"
```

## Testing

This project includes integration tests that spin up a Mock WHOIS server (using `ServerSocket`) to verify the application logic without relying on external network calls or rate limits.

To run the tests:

```bash
./gradlew clean test
```

The test output will show `passed`, `skipped`, or `failed` events in the console.
