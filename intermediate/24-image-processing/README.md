# Image Processing Mini-Project

This is a backend service developed in Java using Spring Boot that processes image uploads with filters using the **ImageJ** library.

## Requirements

- Java 17+
- Gradle (provided via wrapper)
- Docker (optional, for containerization)

## Features

- **Upload Image**: Endpoint to upload an image and apply a filter.
- **Filters**:
  - `grayscale`: Converts image to grayscale.
  - `blur`: Applies a Gaussian blur.
  - `edges`: Detects edges in the image.
  - `invert`: Inverts the image colors.
- **Image Info**: Get dimensions of an uploaded image.

## Usage

### Running the Application

1. **Using Gradle**:
   ```bash
   ./gradlew bootRun
   ```

2. **Using Docker**:
   Build the jar first:
   ```bash
   ./gradlew bootJar
   ```
   Build and run the image:
   ```bash
   docker build -t image-processing .
   docker run -p 8080:8080 image-processing
   ```
   
   Or if using `docker-compose` (not strictly required as no external db, but provided for consistency if you expand):
   ```bash
   docker compose up --build
   ```

### API Endpoints

#### 1. Process Image
Apply a filter to an uploaded image.

- **URL**: `/api/images/process`
- **Method**: `POST`
- **Parameters**:
  - `file`: The image file (multipart/form-data)
  - `filter`: Filter name (`grayscale`, `blur`, `edges`, `invert`). Default: `grayscale`.

**Example (cURL)**:
```bash
curl -X POST -F "file=@/path/to/image.jpg" -F "filter=blur" http://localhost:8080/api/images/process --output processed.png
```

#### 2. Get Image Info
Get basic information (width, height) about the image.

- **URL**: `/api/images/info`
- **Method**: `POST`
- **Parameters**:
  - `file`: The image file.

**Example (cURL)**:
```bash
curl -X POST -F "file=@/path/to/image.jpg" http://localhost:8080/api/images/info
```

## Testing

Run the integration tests using Gradle:

```bash
./gradlew clean test
```

The build is configured to log test events (`passed`, `skipped`, `failed`) to the console.
