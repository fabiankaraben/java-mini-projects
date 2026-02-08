# File Download Server

This is a simple backend in Java, serving files for download with proper headers. It demonstrates how to handle file downloads in a raw HTTP server without using heavy frameworks.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

- `src/main/java`: Source code for the HTTP server.
- `src/test/java`: Integration tests.
- `src/main/resources/files`: Directory containing files available for download.

## How to Run

1.  **Compile and Run:**

    ```bash
    mvn clean compile exec:java -Dexec.mainClass="com.fabiankaraben.filedownload.FileDownloadServer"
    ```

    Or simply run the `main` method in `com.fabiankaraben.filedownload.FileDownloadServer` from your IDE.

2.  **Server Info:**

    The server starts on port `8080`.

## How to Use

You can download files using `curl` or a web browser. The server is pre-populated with a `sample.txt` file.

### Download with curl

To download a file and save it with the filename specified by the server (Content-Disposition header), use the `-O -J` flags.

```bash
curl -v -O -J http://localhost:8080/download/sample.txt
```

- `-v`: Verbose mode (to see headers).
- `-O`: Write output to a local file named like the remote file.
- `-J`: Use the header-provided filename (`Content-Disposition`).

### Example Output

```
< HTTP/1.1 200 OK
< Content-type: application/octet-stream
< Content-disposition: attachment; filename="sample.txt"
< Content-length: 71
...
```

## Running Tests

This project includes integration tests that verify:
- The `Content-Disposition` header is set correctly.
- The downloaded file content matches the original file exactly (byte-for-byte).
- 404 responses for missing files.

To run the tests:

```bash
mvn clean test
```
