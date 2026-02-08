# File Upload Server

This is a basic backend in Java that handles file uploads via HTTP POST requests and saves them to disk. It serves as a practical example of handling `multipart/form-data` requests using Java's built-in `com.sun.net.httpserver` without relying on heavy external frameworks for the server implementation itself.

## Requirements

*   Java 17 or higher
*   Gradle (wrapper included)

## Project Structure

*   `src/main/java`: Source code for the HTTP server and file upload handler.
*   `src/test/java`: Integration tests using Apache HttpClient.
*   `uploads/`: Directory where uploaded files are stored (created automatically).

## How to Run

1.  **Start the server:**

    ```bash
    ./gradlew run
    ```

    The server will start on port `8080`.

2.  **Upload a file:**

    You can use `curl` to upload a file. Replace `path/to/your/file.txt` with the actual file you want to upload.

    ```bash
    curl -F "file=@path/to/your/file.txt" http://localhost:8080/api/upload
    ```

    **Example Output:**
    ```
    File uploaded successfully: file.txt
    ```

    The uploaded file will be saved in the `uploads/` directory in the project root.

3.  **Stop the server:**

    Press `Ctrl+C` in the terminal where the server is running.

## How to Run Tests

The project includes integration tests that verify the file upload functionality by starting the server, uploading a file, and checking if it exists on disk with the correct content.

To run the tests, use the following command:

```bash
./gradlew clean test
```

You should see output indicating the test results (passed, skipped, failed).

## Implementation Details

*   **Server**: Uses `com.sun.net.httpserver.HttpServer`.
*   **Handler**: `FileUploadHandler` manually parses the `multipart/form-data` request body to extract the file content and filename. *Note: This is a simplified educational implementation. For production systems, use a robust library like Apache Commons FileUpload.*
*   **Testing**: Uses `JUnit 5` and `Apache HttpClient 5` to send real HTTP requests to the running server instance.
