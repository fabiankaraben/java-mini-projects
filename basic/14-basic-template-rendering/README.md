# Basic Template Rendering

This mini-project is a basic backend application in Java that demonstrates rendering a simple HTML template with dynamic data. It uses a custom `TemplateRenderer` to inject values into an HTML file and serves the result via an HTTP server.

## Requirements

- Java 17+
- Gradle

## How to Run

1.  **Build the project**:
    ```bash
    ./gradlew clean build
    ```

2.  **Run the application**:
    ```bash
    ./gradlew run
    ```
    The server will start on port `8080`.

## How to Use

You can access the rendered page using `curl` or a web browser.

**Example using curl:**

```bash
curl -v http://localhost:8080/
```

**Expected Output:**

You should see an HTML response with the dynamic data injected:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Mini Project</title>
</head>
<body>
    <h1>Hello, User!</h1>
    <p>Welcome to basic template rendering in Java!</p>
    <footer>Generated at: 2023-10-27T10:00:00.123456</footer>
</body>
</html>
```

*(Note: The timestamp will vary based on when you run the request.)*

## Testing

To run the unit tests, use the following command:

```bash
./gradlew clean test
```

The test results will be displayed in the console, showing passed, skipped, and failed events.
