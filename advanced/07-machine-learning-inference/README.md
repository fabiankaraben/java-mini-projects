# Machine Learning Inference

This mini-project demonstrates how to serve a Machine Learning model using **Java**, **Spring Boot**, and **DJL (Deep Java Library)**. It exposes a REST API to classify images using a pre-trained ResNet50 model (PyTorch engine).

## Requirements

- Java 17 or higher
- Maven
- Docker & Docker Compose (optional, for containerized run)

## Project Structure

- `src/main/java/com/fabiankaraben/djl/InferenceService.java`: Loads the ResNet50 model and handles inference.
- `src/main/java/com/fabiankaraben/djl/InferenceController.java`: REST controller exposing the `/api/inference/classify` endpoint.
- `Dockerfile`: Multi-stage Docker build for the application.
- `docker-compose.yml`: Defines the service orchestration.

## How to Run

### Using Maven (Local)

1.  **Build the project:**
    ```bash
    mvn clean install
    ```
2.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

    *Note: On the first run, DJL will download the ResNet50 model. Ensure you have internet access.*

### Using Docker Compose

1.  **Build and start the container:**
    ```bash
    docker compose up --build
    ```
    The application will be accessible at `http://localhost:8080`.

## API Usage

### Classify an Image

**Endpoint:** `POST /api/inference/classify`

**Consumes:** `multipart/form-data`

**Parameters:**
- `file`: The image file to classify (jpg, png).

**Curl Example:**

```bash
curl -X POST -F "file=@/path/to/your/image.jpg" http://localhost:8080/api/inference/classify
```

**Response Example:**

```json
[
  {
    "className": "tabby, tabby cat",
    "probability": 0.452
  },
  {
    "className": "tiger cat",
    "probability": 0.321
  },
  ...
]
```

## Testing

This project includes unit tests to verify the API response format and error handling using `MockMvc` and `Mockito`.

To run the tests:

```bash
mvn clean test
```

## Implementation Details

- **DJL (Deep Java Library)**: Used to load the PyTorch ResNet50 model from the model zoo.
- **DjlInferenceService**: Initializes the model on startup (`@PostConstruct`) and cleans it up on shutdown (`@PreDestroy`). It resizes and transforms images to tensors before inference.
- **InferenceController**: Handles multipart file uploads, delegates to the service, and returns a JSON list of predictions (class name and probability).
- **InferenceController**: Handles multipart file uploads, delegates to the service, and returns a JSON list of predictions (class name and probability).
