# Audio Streaming Server

This mini-project is a simple Audio Streaming Server built with Java and Spring Boot. It demonstrates how to stream audio files (e.g., MP3) using HTTP Byte Ranges, allowing clients (browsers, media players) to seek to specific parts of the audio file without downloading the entire file.

## Requirements

- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **HTTP Byte Range Support**: Handles `Range` headers to serve partial content (Status 206).
- **Audio Storage**: Serves audio files from a local directory or classpath fallback.
- **Dockerized**: Includes Dockerfile and docker-compose.yml for easy deployment.

## Project Structure

```
src/
  main/
    java/com/example/audiostreaming/
      controller/       # REST Controller handling streaming requests
      service/          # Service to load audio resources
      AudioStreamingApplication.java
    resources/
      audio/            # Fallback audio files for testing
  test/                 # Integration tests for Byte Range verification
Dockerfile
docker-compose.yml
pom.xml
```

## How to Run

### Using Maven

1.  **Build the project**:
    ```bash
    mvn clean package
    ```

2.  **Run the application**:
    ```bash
    java -jar target/audio-streaming-server-0.0.1-SNAPSHOT.jar
    ```
    
    *Note: By default, the app looks for audio files in a `data` directory in the current working directory. If not found, it falls back to `src/main/resources/audio` (which is packaged in the JAR).*

### Using Docker Compose

1.  **Prepare audio files**:
    Create a `data` directory in the project root and place your MP3 files there.
    ```bash
    mkdir data
    # Copy some mp3 files into ./data/
    ```

2.  **Start the service**:
    ```bash
    docker compose up --build
    ```

    The application will be accessible at `http://localhost:8080`.

## Usage

You can test the streaming using `curl` or a web browser.

### 1. Stream the whole file (or let browser handle ranges)
Navigate to `http://localhost:8080/api/audio/{filename}` in your browser.

### 2. Test Partial Content with cURL

**Get the first 10 bytes:**
```bash
curl -v -H "Range: bytes=0-9" http://localhost:8080/api/audio/test.mp3
```
*Expected Response:* `HTTP/1.1 206 Partial Content`, `Content-Range: bytes 0-9/LENGTH`

**Get a middle chunk:**
```bash
curl -v -H "Range: bytes=100-199" http://localhost:8080/api/audio/test.mp3
```

**Get the end of the file:**
```bash
curl -v -H "Range: bytes=500-" http://localhost:8080/api/audio/test.mp3
```

## Testing

The project includes integration tests that verify the HTTP Range behavior using `MockMvc`.

Run the tests using Maven:
```bash
mvn clean test
```

The tests verify:
- 200 OK for full content requests.
- 206 Partial Content for valid range requests.
- Correct `Content-Range` and `Content-Length` headers.
- 416 Requested Range Not Satisfiable for invalid ranges.
- 404 Not Found for missing files.
