# Gzip Compression

This is a simple backend in Java using `GZIPOutputStream`, compressing responses for text-based content. It demonstrates how to handle the `Accept-Encoding` header and dynamically compress response bodies using GZIP to improve network efficiency.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## How to Run

1.  **Compile the project:**

    ```bash
    mvn clean compile
    ```

2.  **Start the server:**

    ```bash
    mvn exec:java -Dexec.mainClass="com.example.gzip.GzipServer"
    ```
    
    Or manually:
    
    ```bash
    javac -d target/classes src/main/java/com/example/gzip/GzipServer.java
    java -cp target/classes com.example.gzip.GzipServer
    ```

    The server will start on port `8080`.

## How to Use

You can interact with the server using `curl`.

### 1. Request with GZIP Compression

Send a request with the `Accept-Encoding: gzip` header. The server will respond with GZIP-compressed content.

```bash
curl -v -H "Accept-Encoding: gzip" http://localhost:8080/ --output - | gunzip
```

**Expected Behavior:**
- The response headers should include `Content-Encoding: gzip`.
- The output is piped to `gunzip` to decompress and display the original text.

### 2. Request without Compression

Send a normal request without the compression header.

```bash
curl -v http://localhost:8080/
```

**Expected Behavior:**
- The response headers should **not** include `Content-Encoding: gzip`.
- The plain text response is displayed directly.

## Running Tests

This project includes integration tests to verify the GZIP compression logic.

Run the tests using Maven:

```bash
mvn clean test
```

The tests verify:
- `Accept-Encoding: gzip` triggers a compressed response (checked via `Content-Encoding` header and body decompression).
- Requests without the header receive uncompressed plain text.
