# Video Transcoding Service

This is a backend service implemented in Java using Spring Boot and Jave2 (FFmpeg wrapper) to manage video conversion jobs. It allows users to upload video files, which are then asynchronously transcoded to MP4 format.

## Requirements

- Java 17+
- Docker & Docker Compose (for running the full stack)
- Maven (optional, if running locally without Docker)

## Features

- **Upload Video**: Endpoint to upload video files for transcoding.
- **Asynchronous Processing**: Videos are processed in the background.
- **Status Checking**: Endpoint to check the status of a transcoding job.
- **Download**: Endpoint to download the transcoded video.
- **Format**: Transcodes input videos to H.264/AAC MP4.

## API Usage

### 1. Upload a Video

```bash
curl -X POST -F "file=@/path/to/video.mov" http://localhost:8080/api/transcode
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "originalFilename": "video.mov",
  "outputFilename": null,
  "status": "PENDING",
  "message": null
}
```

### 2. Check Job Status

```bash
curl http://localhost:8080/api/transcode/{id}
```

**Response (Processing):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "originalFilename": "video.mov",
  "outputFilename": "550e8400-e29b-41d4-a716-446655440000.mp4",
  "status": "PROCESSING",
  "message": null
}
```

**Response (Completed):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "originalFilename": "video.mov",
  "outputFilename": "550e8400-e29b-41d4-a716-446655440000.mp4",
  "status": "COMPLETED",
  "message": null
}
```

### 3. Download Transcoded Video

```bash
curl -O -J http://localhost:8080/api/transcode/{id}/download
```

## Running the Application

### Using Docker Compose (Recommended)

To run the application with all dependencies:

```bash
docker compose up --build
```

The service will be available at `http://localhost:8080`.

### Running Locally

To run the application locally using Maven:

```bash
mvn spring-boot:run
```

## Running Tests

To run the integration tests:

```bash
mvn clean test
```

The tests mock the actual encoding process to ensure stability in CI/CD environments without requiring a full FFmpeg setup, but the application structure supports real transcoding using the bundled native binaries.
