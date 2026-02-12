# Custom Identity Provider

This project implements a custom Identity Provider using **Spring Authorization Server**, supporting OIDC (OpenID Connect).

## Requirements

- Java 17+
- Maven
- Docker (optional, for containerized execution)

## Features

- **OAuth2 / OIDC Support**: Implements Authorization Code Grant with OIDC.
- **In-Memory User & Client Repository**: Pre-configured user (`user`/`password`) and client (`oidc-client`).
- **JWK Source**: Generates RSA keys for signing tokens.

## Getting Started

### Run with Maven

```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080`.

### Run with Docker

```bash
docker compose up --build
```

## Usage (OIDC Flow)

The configured client is `oidc-client` with secret `secret` and redirect URI `http://127.0.0.1:8080/login/oauth2/code/oidc-client`.

1. **Authorize**:
   Open the following URL in a browser:
   ```
   http://localhost:8080/oauth2/authorize?response_type=code&client_id=oidc-client&scope=openid%20profile&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/oidc-client
   ```

2. **Login**:
   Log in with `user` / `password`.

3. **Get Code**:
   After login, you will be redirected to the `redirect_uri` with a `code` parameter.

4. **Exchange Code for Token**:
   Use the code to get an ID token:

   ```bash
   curl -X POST http://localhost:8080/oauth2/token \
     -u oidc-client:secret \
     -d grant_type=authorization_code \
     -d redirect_uri=http://127.0.0.1:8080/login/oauth2/code/oidc-client \
     -d code=<YOUR_CODE>
   ```

## Testing

Run the integration tests to verify the OIDC flow:

```bash
mvn clean test
```

The tests simulate the full login flow using `MockMvc` and verify that a valid ID Token is issued.
