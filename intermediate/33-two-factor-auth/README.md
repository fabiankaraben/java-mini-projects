# Two-Factor Authentication (TOTP) Demo

This is a backend mini-project in Java implementing TOTP (Time-based One-Time Password) validation, similar to Google Authenticator. It uses the `GoogleAuth` library to generate secrets and validate codes.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- Docker & Docker Compose (optional, for running in a container)

## Project Structure

- **Controller**: `TotpController` handles HTTP requests for generating secrets and validating codes.
- **Service**: `TotpService` wraps the GoogleAuthenticator logic.
- **Model**: DTOs for requests and responses.

## How to Run

### Locally with Maven

1.  Build the project:
    ```bash
    mvn clean package
    ```
2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
    The application will start on port `8080`.

### With Docker

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```
    The application will be accessible at `http://localhost:8080`.

## API Usage

### 1. Generate a Secret

Generates a new TOTP secret and a QR code URL (for visualization/testing).

**Request:**

```bash
curl -X POST "http://localhost:8080/api/totp/generate?accountName=user@example.com"
```

**Response:**

```json
{
  "secret": "JBSWY3DPEHPK3PXP",
  "qrCodeUrl": "https://api.qrserver.com/v1/create-qr-code/?data=otpauth://totp/MyCompany:user@example.com?secret=JBSWY3DPEHPK3PXP&issuer=MyCompany"
}
```

*Note: You can use the `secret` in a TOTP app (like Google Authenticator) or use the `qrCodeUrl` to scan it.*

### 2. Validate a Code

Validates a TOTP code against a secret.

**Request:**

```bash
curl -X POST http://localhost:8080/api/totp/validate \
  -H "Content-Type: application/json" \
  -d '{
    "secret": "JBSWY3DPEHPK3PXP",
    "code": 123456
  }'
```

**Response:**

```json
{
  "valid": true
}
```

(or `"valid": false` if incorrect/expired)

## Testing

This project includes integration tests that verify the full flow: generating a secret, generating a valid code locally, and validating it against the API.

To run the tests:

```bash
mvn clean test
```

We expect to see the tests passing in the output.
