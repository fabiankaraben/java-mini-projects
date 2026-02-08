# Currency Converter

A simple backend service in Java that converts currencies using `java.net.http.HttpClient` to fetch rates from a free external API.

## Description

This mini-project demonstrates how to:
- Use Java 11+ `HttpClient` to make asynchronous or synchronous HTTP requests.
- Parse JSON responses using `Jackson`.
- Expose a simple HTTP API using `com.sun.net.httpserver.HttpServer`.
- Test external API integrations using `WireMock`.

## Requirements

- Java 17+
- Maven 3.6+

## How to Run

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Run the application:**
   ```bash
   java -jar target/currency-converter-1.0-SNAPSHOT.jar
   ```
   The server will start on port `8080`.

## API Usage

The application exposes a single endpoint `/convert` which accepts query parameters.

### Convert Currency

**Endpoint:** `GET /convert`

**Parameters:**
- `from`: The base currency code (e.g., USD, EUR)
- `to`: The target currency code (e.g., EUR, GBP)
- `amount`: The amount to convert

**Example Request:**

```bash
curl "http://localhost:8080/convert?from=USD&to=EUR&amount=100"
```

**Example Response:**

```json
{
  "amount": 100.0,
  "from": "USD",
  "to": "EUR",
  "convertedAmount": 85.0
}
```

## Running Tests

This project uses **JUnit 5** and **WireMock** to verify the logic without making actual network requests to the external API.

To run the tests:

```bash
mvn clean test
```

## Project Structure

```
basic/45-currency-converter/
├── pom.xml                 # Maven configuration
├── src
│   ├── main
│   │   java
│   │   └── com/fabiankaraben/currencyconverter
│   │       ├── CurrencyConverter.java   # Service logic
│   │       ├── ExchangeRateResponse.java # DTO for JSON parsing
│   │       └── Main.java                # HTTP Server entry point
│   └── test
│       java
│       └── com/fabiankaraben/currencyconverter
│           └── CurrencyConverterTest.java # Unit/Integration tests with WireMock
└── README.md
```
