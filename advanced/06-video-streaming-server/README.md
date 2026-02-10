# Video Streaming Server

This is a backend mini-project built with Java and Spring Boot that handles real-time video streaming. It converts incoming RTMP streams or generates test streams into HLS (HTTP Live Streaming) format using FFmpeg.

## Features
- **RTMP Ingestion**: Listens for RTMP streams on port 1935.
- **HLS Delivery**: Serves HLS playlists (`.m3u8`) and segments (`.ts`) via HTTP.
- **FFmpeg Integration**: Wraps FFmpeg process to handle transcoding and segmentation.
- **Docker Support**: Containerized application with FFmpeg pre-installed.

## Requirements
- Java 17+
- Docker & Docker Compose
- FFmpeg (if running locally without Docker)

## Project Structure
- `src/main/java`: Spring Boot application source code.
- `src/test/java`: Integration tests.
- `Dockerfile`: Docker image definition including OpenJDK and FFmpeg.
- `docker-compose.yml`: Docker Compose configuration.
- `jmeter/`: JMeter load test plan.

## How to Run

### Using Docker (Recommended)
This approach ensures FFmpeg is available and configured correctly.

1. **Build and Start**:
   ```bash
   docker compose up --build
   ```
   The server will start on port `8080` (HTTP) and `1935` (RTMP).

### Running Locally
Ensure FFmpeg is installed and added to your system PATH.

1. **Build**:
   ```bash
   ./gradlew clean build
   ```
2. **Run**:
   ```bash
   ./gradlew bootRun
   ```

## Usage

### 1. Start a Test Stream
Generate a synthetic test pattern stream (color bars and beep).

```bash
curl -X POST http://localhost:8080/api/stream/start-test
```

### 2. Start RTMP Listener
Start listening for an incoming RTMP stream.

```bash
curl -X POST http://localhost:8080/api/stream/start-rtmp
```
Then push a stream using OBS or FFmpeg to: `rtmp://localhost:1935/live/stream`

### 3. Watch the Stream
Open a video player (like VLC) or a browser with HLS support and open:
- Test Stream: `http://localhost:8080/hls/test.m3u8`
- RTMP Stream: `http://localhost:8080/hls/stream.m3u8`

### 4. Stop Stream
Stops the FFmpeg process.

```bash
curl -X POST http://localhost:8080/api/stream/stop
```

## Testing

### Integration Tests
Run the integration tests to verify the application context and HLS manifest serving.

```bash
./gradlew clean test
```

Test results (passed, skipped, failed) will be logged to the console.

### Load Testing with JMeter
A JMeter test plan is provided in `jmeter/load-test.jmx`.
1. Open Apache JMeter.
2. Load `jmeter/load-test.jmx`.
3. Start the application (Docker or Local).
4. Run the test plan to simulate multiple viewers requesting the HLS manifest.
