# File Storage with S3

This mini-project demonstrates how to build a Java backend using Spring Boot and the AWS SDK to interact with an S3-compatible storage service (MinIO).

## Project Overview

- **Framework**: Spring Boot 3.4.1
- **Language**: Java 17
- **Dependency Manager**: Gradle
- **Storage**: MinIO (S3-compatible)
- **Testing**: Testcontainers (MinIO)

## Requirements

- Java 17+
- Docker (for running MinIO and tests)

## Project Structure

```
intermediate/10-file-storage-with-s3/
├── src/
│   ├── main/java/com/example/filestorage/  # Application code
│   └── test/java/com/example/filestorage/  # Integration tests
├── build.gradle                            # Gradle build file
├── docker-compose.yml                      # Docker Compose for running the app
├── Dockerfile                              # Dockerfile for the application
└── README.md                               # This file
```

## How to Run

### Using Docker Compose (Recommended)

To run the entire application (backend + MinIO) using Docker Compose:

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.
MinIO Console will be available at `http://localhost:9001` (User/Pass: `minioadmin`/`minioadmin`).

### Running Locally

1. Start MinIO using Docker Compose (only MinIO):
   ```bash
   docker compose up minio
   ```
2. Run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```

## API Usage

### 1. Upload a File

```bash
curl -X POST -F "file=@/path/to/your/file.txt" http://localhost:8080/api/files/upload
```

**Response:**
Returns the file key (ID) needed for download.
```
e5f6g7h8-file.txt
```

### 2. Download a File

Replace `<file-key>` with the key returned from the upload endpoint.

```bash
curl -O -J http://localhost:8080/api/files/download/<file-key>
```

## Testing

This project uses **Testcontainers** to spin up a MinIO container for integration testing. This ensures that tests run against a real S3-compatible environment.

To run the tests:

```bash
./gradlew clean test
```

The `testLogging` is configured to show passed, skipped, and failed events.
