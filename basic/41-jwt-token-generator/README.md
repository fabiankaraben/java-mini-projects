# JWT Token Generator

This is a simple backend mini-project in Java that demonstrates how to generate and validate JWT (JSON Web Tokens) using the `jjwt` library.

## Requirements

*   Java 17 or higher
*   Maven

## How to use

### Build and Run

1.  Navigate to the project directory:
    ```bash
    cd basic/41-jwt-token-generator
    ```

2.  Run the application using Maven:
    ```bash
    mvn clean compile exec:java
    ```

The server will start on port `8080`.

### API Endpoints

#### 1. Generate Token

*   **URL**: `/generate`
*   **Method**: `POST`
*   **Body**: JSON object containing `subject` and any other claims.

**Example Request:**

```bash
curl -X POST http://localhost:8080/generate \
     -H "Content-Type: application/json" \
     -d '{
           "subject": "user123",
           "role": "admin",
           "email": "user@example.com"
         }'
```

**Example Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### 2. Validate Token

*   **URL**: `/validate`
*   **Method**: `POST`
*   **Body**: JSON object containing the `token`.

**Example Request:**

```bash
curl -X POST http://localhost:8080/validate \
     -H "Content-Type: application/json" \
     -d '{
           "token": "YOUR_JWT_TOKEN_HERE"
         }'
```

**Example Response (Valid):**

```json
{
  "valid": true,
  "subject": "user123",
  "claims": {
    "sub": "user123",
    "role": "admin",
    "email": "user@example.com",
    "iat": 1700000000,
    "exp": 1700003600
  }
}
```

**Example Response (Invalid):**

```json
{
  "valid": false,
  "error": "JWT expired at 2023-..."
}
```

## Testing

This project includes unit tests that generate a token and immediately validate/decode it to check claims.

To run the tests:

```bash
mvn clean test
```

## Implementation Details

*   **Library**: [JJWT](https://github.com/jwtk/jjwt)
*   **Server**: Standard Java `com.sun.net.httpserver.HttpServer`
*   **Algorithm**: HMAC SHA-256 (HS256)
