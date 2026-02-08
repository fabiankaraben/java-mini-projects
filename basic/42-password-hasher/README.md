# Password Hasher

This is a basic backend in Java using BCrypt for hashing and verifying passwords. It provides a REST API to hash a plain text password and to verify if a plain text password matches a given hash.

## Requirements

*   Java 17 or later
*   Gradle (optional, wrapper provided)

## Usage

### Running the Application

To run the application, use the following command:

```bash
./gradlew bootRun
```

The server will start on port 8080.

### API Endpoints

#### 1. Hash a Password

Hashes a plain text password using BCrypt.

**Endpoint:** `POST /hash`
**Content-Type:** `application/json`

**Request Body:**

```json
{
  "password": "mySecretPassword"
}
```

**Curl Example:**

```bash
curl -X POST http://localhost:8080/hash \
  -H "Content-Type: application/json" \
  -d '{"password": "mySecretPassword"}'
```

**Response:**

```json
{
  "hash": "$2a$10$..."
}
```

#### 2. Verify a Password

Verifies if a plain text password matches a given BCrypt hash.

**Endpoint:** `POST /verify`
**Content-Type:** `application/json`

**Request Body:**

```json
{
  "password": "mySecretPassword",
  "hash": "$2a$10$..."
}
```

**Curl Example:**

```bash
curl -X POST http://localhost:8080/verify \
  -H "Content-Type: application/json" \
  -d '{"password": "mySecretPassword", "hash": "$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW"}'
```

**Response:**

```json
{
  "match": true
}
```

## Testing

This project includes unit tests to verify the password hashing and verification logic.

To run the tests, execute:

```bash
./gradlew clean test
```

The test logging is configured to show passed, skipped, and failed events.
