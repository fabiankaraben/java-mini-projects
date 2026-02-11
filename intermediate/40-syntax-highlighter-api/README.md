# Syntax Highlighter API

This is a backend mini-project in Java that provides a REST API to highlight code syntax. It accepts a code snippet and a language identifier, and returns an HTML string with syntax highlighting applied (using span tags and inline styles).

## Requirements

- Java 17 or higher
- Gradle
- Docker (optional, for containerized execution)

## Dependencies

- **Spring Boot**: For the REST API framework.
- **RSyntaxTextArea**: For the underlying syntax analysis and tokenization.

## Usage

### Running Locally

To run the application locally using Gradle:

```bash
./gradlew bootRun
```

The server will start on port 8080.

### Running with Docker

To run the application using Docker Compose:

```bash
docker compose up --build
```

### API Endpoints

#### POST /api/highlight

Highlights the provided code snippet.

**Request Body (JSON):**

```json
{
  "code": "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}",
  "language": "java"
}
```

**Response (HTML):**

Returns a `text/html` response containing the highlighted code inside a `<pre>` block.

**Example with curl:**

```bash
curl -X POST http://localhost:8080/api/highlight \
     -H "Content-Type: application/json" \
     -d '{
           "code": "print(\"Hello World\")", 
           "language": "python"
         }'
```

Supported languages include: `java`, `python`, `xml`, `json`, `javascript`, `c`, `cpp`, `html`, `css`, `sql`, `bash`.

## Testing

This project includes unit and integration tests to verify the syntax highlighting logic and the REST controller.

To run the tests, execute:

```bash
./gradlew clean test
```

The test output will show "passed", "skipped", and "failed" events in the console.
