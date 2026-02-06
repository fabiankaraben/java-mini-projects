# Static File Server

This is a basic backend mini-project in Java using `com.sun.net.httpserver`. It serves static HTML, CSS, and JS files from a directory (`src/main/resources/public`), mimicking a simple web server like Nginx or Apache for static content.

## Features

- **Static File Serving**: Serves HTML, CSS, JavaScript, and image files.
- **MIME Type Support**: Automatically detects and sets the correct `Content-Type` header based on file extension.
- **Root Redirection**: Automatically serves `index.html` when requesting the root `/`.
- **Security**: Basic protection against directory traversal attacks (e.g., `../`).
- **Testing**: Includes integration tests using `java.net.http.HttpClient` to verify file serving and content types.

## Requirements

- Java 17 or higher
- Gradle

## Project Structure

```
basic/02-static-file-server/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── fabiankaraben/
│   │   │           └── staticserver/
│   │   │               ├── StaticFileServer.java   # Main application class
│   │   │               └── StaticFileHandler.java  # Request handler logic
│   │   └── resources/
│   │       └── public/
│   │           ├── index.html
│   │           ├── styles.css
│   │           └── script.js
│   └── test/
│       └── java/
│           └── com/
│               └── fabiankaraben/
│                   └── staticserver/
│                       └── StaticFileServerIntegrationTest.java # Integration tests
├── build.gradle
├── settings.gradle
└── README.md
```

## How to Use

1. **Build the project**:
   ```bash
   ./gradlew build
   ```

2. **Run the server**:
   You can run the server using the `run` task provided by the application plugin:
   ```bash
   ./gradlew run
   ```
   
   The server will start on port **8080**.

3. **View the site**:
   Open your browser and navigate to:
   [http://localhost:8080/](http://localhost:8080/)

   You should see the "Hello from Static File Server!" page with styling and a functional button.

## How to Run Tests

Run the integration tests using Gradle:

```bash
./gradlew test
```

or

```bash
./gradlew clean test
```

This will execute `StaticFileServerIntegrationTest`, which spins up the server and verifies that:
- `index.html` is served correctly at `/` and `/index.html`.
- CSS and JS files are served with correct MIME types.
- 404 is returned for missing files.
- 405 is returned for non-GET requests.
