# PDF Generator

This is a backend mini-project in Java that generates PDFs from templates (simple text in this case) using **OpenPDF** and verifies the content using **PDFBox** in tests.

## Requirements

- Java 17+
- Docker (optional, for containerization)
- Gradle

## Dependencies

- **Spring Boot 3.4.1**: Web framework.
- **OpenPDF**: For generating PDF files.
- **PDFBox**: For parsing and verifying PDF content in tests.

## How to Run

### Using Gradle

1.  Build the project:
    ```bash
    ./gradlew build
    ```
2.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

The server will start on port 8080.

### Using Docker

1.  Build the JAR:
    ```bash
    ./gradlew bootJar
    ```
2.  Build the Docker image:
    ```bash
    docker build -t pdf-generator .
    ```
3.  Run the container:
    ```bash
    docker run -p 8080:8080 pdf-generator
    ```

## Usage

You can generate a PDF by sending a GET request to `/generate-pdf`.

### Examples

**Default PDF:**

```bash
curl -o sample.pdf http://localhost:8080/generate-pdf
```

**Custom PDF:**

```bash
curl -o custom.pdf "http://localhost:8080/generate-pdf?title=My%20Custom%20Title&content=Here%20is%20some%20custom%20content."
```

## Testing

This project includes integration tests that:
1.  Call the API to generate a PDF.
2.  Parse the generated binary content using **PDFBox**.
3.  Verify that the expected text is present in the PDF.

To run the tests:

```bash
./gradlew clean test
```

You should see output indicating passed tests, as `testLogging` is configured to show events.
