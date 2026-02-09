# OAuth2 Authorization Server

This mini-project demonstrates a backend in Java using **Spring Authorization Server**. It serves as an OAuth2 Authorization Server that issues tokens using the **Client Credentials** flow.

## Requirements

-   Java 25
-   Gradle 8.x or later (wrapper provided)

## Project Structure

-   `src/main/java`: Source code for the application and security configuration.
-   `src/test/java`: Integration tests using MockMvc.

## Configuration

The application is configured with an in-memory registered client:

-   **Client ID**: `messaging-client`
-   **Client Secret**: `secret`
-   **Authorization Grant Types**: `authorization_code`, `refresh_token`, `client_credentials`
-   **Scopes**: `openid`, `profile`, `message.read`, `message.write`

And a default in-memory user:
-   **Username**: `user`
-   **Password**: `password`

## How to Run

1.  **Build and Run the application**:
    ```bash
    ./gradlew bootRun
    ```
    The server will start on port 8080.

## Usage

### Get an Access Token (Client Credentials Flow)

You can request an access token using `curl`:

```bash
curl -X POST http://localhost:8080/oauth2/token \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -u "messaging-client:secret" \
     -d "grant_type=client_credentials" \
     -d "scope=message.read"
```

**Expected Response**:

```json
{
  "access_token": "...",
  "scope": "message.read",
  "token_type": "Bearer",
  "expires_in": 300
}
```

## Testing

Integration tests are implemented using **MockMvc** to verify the token issuance.

To run the tests:

```bash
./gradlew clean test
```

The build is configured to log test events (`passed`, `skipped`, `failed`) to the console.
