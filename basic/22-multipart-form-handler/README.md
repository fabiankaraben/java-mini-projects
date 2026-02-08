# Multipart Form Handler

This is a basic backend in Java that parses multipart forms with files and text fields using the Javalin framework.

## Requirements

*   Java 21 or higher
*   Gradle

## How to Run

1.  Build and run the server:
    ```bash
    ./gradlew run
    ```
    The server will start at `http://localhost:7000`.

## API Usage

### Upload a File

You can use `curl` to upload files and send text fields.

**Example:**

```bash
curl -X POST http://localhost:7000/upload \
  -F "username=johndoe" \
  -F "description=My cool file" \
  -F "file=@/path/to/your/image.png"
```

**Response:**

```json
{
  "status": "success",
  "message": "Multipart data processed successfully",
  "fields": {
    "username": "johndoe",
    "description": "My cool file"
  },
  "files": [
    "image.png (12345 bytes)"
  ]
}
```

## Running Tests

This project includes integration tests that verify the multipart upload functionality.

To run the tests:

```bash
./gradlew clean test
```

The test logging is configured to show passed, skipped, and failed events.
