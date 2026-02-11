# IP Geolocation Service

This is a backend service in Java using Spring Boot and MaxMind GeoIP2 to return location data (Country, City, Latitude, Longitude) for a given IP address.

## Requirements

- Java 17+
- Gradle
- Docker & Docker Compose (optional, for containerization)
- MaxMind GeoLite2 City Database (`GeoLite2-City.mmdb`)

## Setup

1. **Download the GeoLite2 City Database**:
   - This project requires the `GeoLite2-City.mmdb` file to function in a real environment.
   - Sign up for a free account at [MaxMind](https://www.maxmind.com/en/geolite2/signup).
   - Download the **GeoLite2 City** database (binary .mmdb format).
   - Place the `GeoLite2-City.mmdb` file in `src/main/resources/`.

## Running the Application

### Locally
```bash
./gradlew bootRun
```

### With Docker Compose
```bash
docker compose up --build
```

## Usage

**Endpoint:** `GET /api/geolocation/{ipAddress}`

**Example Request:**
```bash
curl http://localhost:8080/api/geolocation/8.8.8.8
```

**Example Response:**
```json
{
  "ipAddress": "8.8.8.8",
  "country": "United States",
  "city": "Mountain View",
  "latitude": 37.4223,
  "longitude": -122.0848
}
```

## Testing

Integration tests use a mocked `DatabaseReader`, so they do not require the actual `.mmdb` file to pass.

Run the tests using Gradle:
```bash
./gradlew clean test
```
The test output will show events: `passed`, `skipped`, `failed`.
