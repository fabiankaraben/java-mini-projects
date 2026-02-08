# Basic OAuth Client

This project is a basic backend in Java that demonstrates how to authenticate with an OAuth 2.0 provider (like Google). It uses **Javalin** for the web server and **Java HttpClient** to communicate with the OAuth provider.

## Requirements

*   Java 21 or higher
*   Gradle 8.5 or higher (wrapper included if you run `gradle wrapper`)

## Dependencies

*   **Javalin**: Lightweight web framework for Java.
*   **Java HttpClient**: Built-in Java client for making HTTP requests.
*   **Jackson**: For JSON parsing.
*   **SLF4J**: Logging facade.
*   **WireMock** (Test): For mocking the OAuth provider in tests.
*   **JUnit 5** (Test): Testing framework.
*   **AssertJ** (Test): Fluent assertions.

## Setup & Usage

1.  **Build the project:**
    ```bash
    ./gradlew build
    ```

2.  **Run the application:**
    ```bash
    ./gradlew run
    ```
    The server will start on `http://localhost:7070`.

3.  **Environment Variables:**
    You can configure the OAuth provider settings using environment variables.
    
    *   `OAUTH_CLIENT_ID`: Your Client ID (default: `test-client-id`)
    *   `OAUTH_CLIENT_SECRET`: Your Client Secret (default: `test-client-secret`)
    *   `OAUTH_AUTH_ENDPOINT`: Authorization endpoint (default: Google's auth endpoint)
    *   `OAUTH_TOKEN_ENDPOINT`: Token endpoint (default: Google's token endpoint)
    *   `OAUTH_REDIRECT_URI`: Redirect URI (default: `http://localhost:7070/callback`)

### Testing the Flow

1.  Open your browser and navigate to:
    ```
    http://localhost:7070/
    ```
    You will see a welcome message.

2.  Click or navigate to:
    ```
    http://localhost:7070/login
    ```
    This will redirect you to the OAuth provider's login page (by default, Google).

    **Note:** Since the default client ID and secret are placeholders, the Google login page will show an error. To make it work with a real provider, you must register an application with that provider (e.g., Google Cloud Console) and set the environment variables accordingly.

3.  **Simulating a Callback (Manual Test):**
    If you want to simulate a successful callback without a real provider, you would need to run a mock server or modify the code to accept a fake code. However, the application expects a valid code to exchange for a token from the `OAUTH_TOKEN_ENDPOINT`.

    You can manually test the callback endpoint if you have a valid code (or if you point the token endpoint to a mock service you control):
    ```bash
    curl "http://localhost:7070/callback?code=YOUR_AUTH_CODE"
    ```
    The server will try to exchange this code for a token.

## Running Tests

This project uses **WireMock** to simulate the OAuth provider's token endpoint, verifying the authentication flow without needing a real provider.

To run the tests, execute:

```bash
./gradlew clean test
```

The output will show the status of each test event (passed, skipped, failed).
