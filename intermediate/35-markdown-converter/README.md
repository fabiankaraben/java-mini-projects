# Markdown to HTML Converter

This mini-project is a **Spring Boot** application that converts Markdown text to HTML using the **CommonMark** library.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **Markdown Conversion**: Converts standard Markdown syntax to HTML.
- **Extensions**: Supports GitHub Flavored Markdown (GFM) tables.
- **REST API**: Exposes a POST endpoint to accept raw Markdown and return JSON with HTML.

## Project Structure

- `src/main/java`: Source code.
- `src/test/java`: Unit and integration tests.
- `Dockerfile`: Multi-stage Docker build.
- `docker-compose.yml`: Docker Compose configuration.

## How to Run

### Using Maven

1.  Navigate to the project directory:
    ```bash
    cd intermediate/35-markdown-converter
    ```
2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

### Using Docker Compose

1.  Build and start the container:
    ```bash
    docker compose up --build
    ```
2.  The application will be available at `http://localhost:8080`.

## API Usage

### Convert Markdown to HTML

**Endpoint:** `POST /api/markdown/convert`

**Body:** Raw Markdown text

**Example:**

```bash
curl -X POST http://localhost:8080/api/markdown/convert \
     -H "Content-Type: text/plain" \
     -d '# Hello World
     
This is a **bold** statement.

| Header 1 | Header 2 |
| --- | --- |
| Row 1 | Row 2 |'
```

**Response:**

```json
{
  "html": "<h1>Hello World</h1>\n<p>This is a <strong>bold</strong> statement.</p>\n<table>\n<thead>\n<tr>\n<th>Header 1</th>\n<th>Header 2</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td>Row 1</td>\n<td>Row 2</td>\n</tr>\n</tbody>\n</table>\n"
}
```

## Running Tests

To run the unit tests, use the following command:

```bash
mvn clean test
```

The tests verify:
- Basic Markdown parsing (headers, bold, lists).
- GFM Tables extension support.
- Controller integration with mocked service.
