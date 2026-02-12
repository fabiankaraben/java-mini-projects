# Digital Asset Management

This mini-project implements a Digital Asset Management (DAM) system backend in Java. It handles file uploads, extracts metadata (EXIF, XMP) using `metadata-extractor`, and supports versioning of assets.

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **Asset Upload**: Upload images (or other files) via multipart/form-data.
- **Metadata Extraction**: Automatically extracts metadata (e.g., EXIF tags) from uploaded images.
- **Versioning**: Uploading a file with the same filename increments the version number.
- **Metadata Search**: Search for assets based on extracted metadata key-value pairs.
- **History**: Retrieve version history for a specific filename.

## Usage

### Running Locally

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### Running with Docker

```bash
docker compose up --build
```

### API Endpoints & Curl Examples

#### 1. Upload an Asset

```bash
# Upload an image (ensure you have an image file, e.g., photo.jpg)
curl -F "file=@photo.jpg" http://localhost:8080/api/assets
```

Response:
```json
{
  "id": 1,
  "filename": "photo.jpg",
  "contentType": "image/jpeg",
  "size": 12345,
  "version": 1,
  "uploadedAt": "2023-10-27T10:00:00",
  "metadata": {
    "Exif SubIFD - ISO Speed Ratings": "100",
    "Exif IFD0 - Make": "Canon"
  }
}
```

#### 2. Get Asset Metadata

```bash
curl http://localhost:8080/api/assets/1
```

#### 3. Download Asset

```bash
curl -O -J http://localhost:8080/api/assets/1/download
```

#### 4. Search by Metadata

```bash
curl "http://localhost:8080/api/assets/search?key=Exif%20IFD0%20-%20Make&value=Canon"
```

#### 5. Get Asset History

```bash
curl http://localhost:8080/api/assets/history/photo.jpg
```

## Testing

This project includes integration tests that verify the upload process, metadata extraction mock, and search functionality.

To run the tests:

```bash
mvn clean test
```

## Project Structure

- `src/main/java/com/example/dam/model`: Entity classes (`Asset`).
- `src/main/java/com/example/dam/repository`: JPA repositories.
- `src/main/java/com/example/dam/service`: Business logic (`AssetService`, `MetadataExtractionService`).
- `src/main/java/com/example/dam/controller`: REST controllers.
- `src/test/java/com/example/dam`: Integration tests.
