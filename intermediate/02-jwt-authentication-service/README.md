# JWT Authentication Service

This is a backend mini-project built with Java and Spring Boot that implements JSON Web Token (JWT) based authentication. It provides endpoints for user registration, login, and token refreshing, securing a demo endpoint to verify authentication.

## Features

- **User Registration**: Create a new user account with a username, password, and role.
- **Login**: Authenticate with username and password to receive an Access Token and a Refresh Token.
- **Token Refresh**: Use a valid Refresh Token to obtain a new Access Token.
- **Protected Endpoints**: Secure API endpoints that require a valid Access Token (Bearer Token).
- **In-Memory Database**: Uses H2 database for easy setup and testing (data is lost on restart).

## Requirements

- Java 17 or higher
- Gradle (Wrapper included)

## Getting Started

1. Navigate to the project directory:
   ```bash
   cd intermediate/02-jwt-authentication-service
   ```

2. Build the project:
   ```bash
   ./gradlew build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`.

## API Usage (Curl Examples)

### 1. Register a new user

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "fabian",
    "password": "password123",
    "role": "USER"
  }'
```
**Response:** JSON containing `accessToken` and `refreshToken`.

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "fabian",
    "password": "password123"
  }'
```
**Response:** JSON containing `accessToken` and `refreshToken`.

### 3. Access Protected Endpoint

Replace `<ACCESS_TOKEN>` with the token received from login or register.

```bash
curl -X GET http://localhost:8080/api/demo \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```
**Response:** `Hello from secured endpoint`

### 4. Refresh Token

Replace `<REFRESH_TOKEN>` with the refresh token received from login.

```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Authorization: Bearer <REFRESH_TOKEN>"
```
**Response:** JSON containing a new `accessToken` and a new `refreshToken`.

## Testing

The project includes Unit tests for the JWT utility class and Integration tests using Spring Security Test to verify the authentication flow.

To run the tests, execute:

```bash
./gradlew clean test
```

Test output is configured to show detailed events (`passed`, `skipped`, `failed`) in the console.
