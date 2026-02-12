# Voice Recognition API

This is a backend service in Java using **Vosk** for offline speech-to-text recognition. It exposes a REST API to upload audio files and receive their text transcriptions.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure

- `src/main/java`: Source code
- `src/test/java`: Integration tests
- `Dockerfile`: Multi-stage Docker build including Vosk model download
- `docker-compose.yml`: Docker Compose configuration

## Getting Started

### Running with Docker (Recommended)

This project requires a Vosk model to function. The Docker setup handles downloading the model automatically.

1.  Build and start the service:
    ```bash
    docker compose up --build
    ```
2.  The API will be available at `http://localhost:8080`.

### Running Locally

To run locally, you must manually download a Vosk model:

1.  Download a model (e.g., `vosk-model-small-en-us-0.15`) from [Vosk Models](https://alphacephei.com/vosk/models).
2.  Extract the archive and rename the folder to `model` in the project root directory.
3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

## Usage

### 1. Check Transcription

Upload a WAV file (16kHz mono recommended for best results) to the API.

**Request:**

```bash
curl -X POST -F "file=@/path/to/audio.wav" http://localhost:8080/api/recognize
```

**Response:**

```json
{
  "text" : "what zero zero zero one"
}
```

## Testing

The project includes integration tests that verify the API endpoint using a mocked Voice Recognition service (to avoid requiring the full model for basic tests).

To run the tests:

```bash
mvn clean test
```
