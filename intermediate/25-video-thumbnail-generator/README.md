# Video Thumbnail Generator

This is a backend mini-project built with Java and Spring Boot that provides an API to upload videos and generate thumbnail images using an FFmpeg wrapper.

## Requirements

- Java 17 or higher
- Maven
- FFmpeg (required if running locally without Docker)
- Docker and Docker Compose (optional, for running in a container)

## Project Structure

- `src/main/java`: Source code for the Spring Boot application.
- `src/test/java`: Integration tests.
- `Dockerfile`: Configuration for building the application container with FFmpeg.
- `docker-compose.yml`: Configuration for running the application service.

## How to Run

### Using Docker (Recommended)

This project handles the FFmpeg dependency automatically when running with Docker.

1. Build and start the application:
   ```bash
   docker compose up --build
   ```

2. The application will be accessible at `http://localhost:8080`.

### Running Locally

If you prefer to run locally, you must have `ffmpeg` and `ffprobe` installed on your system and available in your PATH.

1. Install FFmpeg.
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Usage

### Generate Thumbnail

Upload a video file to generate a PNG thumbnail. The thumbnail is extracted from the 10th frame of the video and scaled to 320px width.

**Endpoint:** `POST /api/videos/thumbnail`
**Content-Type:** `multipart/form-data`

**Curl Example:**

```bash
# Upload a video and save the response as thumbnail.png
curl -X POST -F "file=@/path/to/video.mp4" http://localhost:8080/api/videos/thumbnail -o thumbnail.png
```

## Running Tests

This project includes integration tests that verify the thumbnail generation flow. The tests use Mockito to mock the FFmpeg execution, so you can run them even if FFmpeg is not installed on your local machine.

To run the tests:

```bash
./mvnw clean test
```

The tests verify:
- The API endpoint accepts a file upload.
- The service invokes the FFmpeg wrapper (mocked).
- The response contains the generated thumbnail image data.
