# Weather Fetcher

This is a simple backend mini-project in Java that acts as a proxy to fetch weather data from a public API (Open-Meteo). It uses `java.net.http.HttpClient` for making requests and exposes a simple HTTP server.

## Requirements

- Java 17 or higher
- Gradle (provided via wrapper, or installed)

## How it works

The application starts a simple HTTP server on port 8080.
It exposes a `/weather` endpoint which proxies requests to the Open-Meteo API.

## How to use

1. **Build and Run the application:**

   ```bash
   ./gradlew run
   ```

2. **Fetch Weather Data:**
   You can query the weather for a specific latitude and longitude.

   **Default (Berlin):**
   ```bash
   curl http://localhost:8080/weather
   ```

   **Custom Coordinates (e.g., London):**
   ```bash
   curl "http://localhost:8080/weather?lat=51.5074&lon=-0.1278"
   ```

## Testing

This project uses **WireMock** to simulate the external Weather API, ensuring tests are deterministic and do not rely on the live internet connection or external service availability.

To run the tests and see the detailed output (passed/skipped/failed events):

```bash
./gradlew clean test
```

## Project Structure

- `src/main/java/com/example/weather/WeatherService.java`: Logic to fetch data from the external API.
- `src/main/java/com/example/weather/WeatherApp.java`: Main entry point and HTTP server.
- `src/test/java/com/example/weather/WeatherServiceTest.java`: Integration tests using WireMock.
