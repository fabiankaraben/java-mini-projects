# URL Shortener

This is a simple backend application in Java that shortens URLs and handles redirects using an in-memory map store. It uses **Javalin** as the web framework and **Base62** encoding for generating short IDs.

## Requirements

*   Java 17+
*   Maven

## How to Run

1.  **Build the project:**

    ```bash
    mvn clean package
    ```

2.  **Run the application:**

    ```bash
    java -jar target/url-shortener-1.0-SNAPSHOT.jar
    ```

    The server will start on port `7070`.

## Usage

### 1. Shorten a URL

Use `curl` to send a POST request with the URL you want to shorten.

```bash
curl -X POST http://localhost:7070/shorten \
     -d '{"url": "https://www.google.com/search?q=java+mini+projects"}'
```

**Response:**

```json
{
  "id": "qi",
  "shortUrl": "http://localhost:7070/qi",
  "originalUrl": "https://www.google.com/search?q=java+mini+projects"
}
```

### 2. Access the Short URL

You can open the `shortUrl` in your browser, or verify the redirect using `curl` with the `-v` (verbose) flag to see the headers.

```bash
curl -v http://localhost:7070/qi
```

**Expected Output (Headers):**

```
< HTTP/1.1 302 Found
< Location: https://www.google.com/search?q=java+mini+projects
...
```

## Running Tests

This project includes integration tests that verify the full flow: creating a short URL and then accessing it to verify the redirect.

Run the tests using Maven:

```bash
mvn clean test
```
