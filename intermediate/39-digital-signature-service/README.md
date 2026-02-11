# Digital Signature Service

A simple Java backend service implemented with Spring Boot that provides digital signature capabilities using `java.security`. It allows users to generate RSA key pairs, sign data with a private key, and verify signatures using a public key.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure

- `src/main/java`: Source code for the application.
  - `service`: Contains `DigitalSignatureService` for cryptographic operations.
  - `controller`: Contains `DigitalSignatureController` for REST endpoints.
  - `model`: DTOs for requests and responses.
- `src/test/java`: Unit and Integration tests.
- `Dockerfile`: Multi-stage Docker build file.
- `docker-compose.yml`: Docker Compose configuration.

## API Endpoints

### 1. Generate Key Pair

Generates a new RSA public/private key pair.

**Request:**

`POST /api/signature/generate-keys`

**Response:**

```json
{
    "publicKey": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...",
    "privateKey": "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQ..."
}
```

**Curl Example:**

```bash
curl -X POST http://localhost:8080/api/signature/generate-keys
```

### 2. Sign Data

Signs a string of data using a provided private key.

**Request:**

`POST /api/signature/sign`

**Body:**

```json
{
    "data": "Important message to sign",
    "privateKey": "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQ..."
}
```

**Response:**

```json
{
    "signature": "lbf...signature_string...",
    "algorithm": "SHA256withRSA"
}
```

**Curl Example:**

```bash
curl -X POST http://localhost:8080/api/signature/sign \
     -H "Content-Type: application/json" \
     -d '{
           "data": "Important message to sign",
           "privateKey": "<YOUR_PRIVATE_KEY>"
         }'
```

### 3. Verify Signature

Verifies the digital signature of the data using the public key.

**Request:**

`POST /api/signature/verify`

**Body:**

```json
{
    "data": "Important message to sign",
    "signature": "lbf...signature_string...",
    "publicKey": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA..."
}
```

**Response:**

```json
{
    "valid": true,
    "message": "Signature is valid."
}
```

**Curl Example:**

```bash
curl -X POST http://localhost:8080/api/signature/verify \
     -H "Content-Type: application/json" \
     -d '{
           "data": "Important message to sign",
           "signature": "<YOUR_SIGNATURE>",
           "publicKey": "<YOUR_PUBLIC_KEY>"
         }'
```

## Running the Application

### Locally with Maven

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### With Docker Compose

Build and start the container:

```bash
docker compose up --build
```

The application will be accessible at `http://localhost:8080`.

To stop the application:

```bash
docker compose down
```

## Running Tests

To run the unit and integration tests, use the following command:

```bash
mvn clean test
```

This will compile the project and execute all tests defined in `src/test/java`.
