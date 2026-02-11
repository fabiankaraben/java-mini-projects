# File Encryption Service

This is a backend service implemented in Java using Spring Boot and `javax.crypto`. It exposes REST endpoints to encrypt and decrypt files using AES encryption.

## Requirements

- Java 17+
- Docker & Docker Compose (optional, for containerized run)

## Project Structure

- `src/main/java/com/example/encryption`: Source code
  - `service/EncryptionService.java`: Core logic for AES encryption/decryption.
  - `controller/FileController.java`: REST endpoints for file handling.
- `src/test/java`: Integration tests.

## How to Run

### Using Gradle

1.  Make sure you are in the project directory:
    ```bash
    cd intermediate/42-file-encryption-service
    ```
2.  Run the application:
    ```bash
    ./gradlew bootRun
    ```
    The application will start on port `8080`.

### Using Docker

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```

## Usage

### 1. Encrypt a File

Upload a file to be encrypted. The response will contain the encrypted data (Base64 encoded) and the secret key (Base64 encoded) used for encryption.

**Endpoint:** `POST /api/files/encrypt`
**Content-Type:** `multipart/form-data`

```bash
curl -X POST -F "file=@/path/to/your/secret.txt" http://localhost:8080/api/files/encrypt
```

**Example Response:**
```json
{
  "fileName": "secret.txt",
  "encryptedData": "VGhpcyBpcyBhbiBlbmNyeXB0ZWQgbWVzc2FnZQ==",
  "key": "c29tZXNlY3JldGtleTEyMw==",
  "algorithm": "AES"
}
```

### 2. Decrypt a File

Send the encrypted data and the key back to decrypt it.

**Endpoint:** `POST /api/files/decrypt`
**Content-Type:** `application/json`

```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{
           "encryptedData": "VGhpcyBpcyBhbiBlbmNyeXB0ZWQgbWVzc2FnZQ==",
           "key": "c29tZXNlY3JldGtleTEyMw=="
         }' \
     http://localhost:8080/api/files/decrypt --output decrypted.txt
```

## Running Tests

To run the integration tests:

```bash
./gradlew clean test
```

The test output will show the status of each test event (passed, skipped, failed).
