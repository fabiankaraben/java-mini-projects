# Basic Error Handling

🔹 This is a basic backend in Java, implementing global exception handling to return proper error responses.

## Requirements

- Java 17 or higher
- Gradle 8.x (or use the provided wrapper)

## Features

- **Global Exception Handling**: Uses `@ControllerAdvice` and `@ExceptionHandler` to handle exceptions globally.
- **Custom Error Response**: Returns a JSON structure with timestamp, status, error, message, and path.
- **Custom Exceptions**: Defines specific exceptions for different error scenarios (e.g., `ResourceNotFoundException`, `InvalidInputException`).
- **Integration Testing**: Verifies that the correct HTTP status codes and error messages are returned.

## Usage

### Running the Application

1. Open a terminal in the project directory:
   ```bash
   cd basic/10-basic-error-handling
   ```

2. Run the application using Gradle:
   ```bash
   ./gradlew bootRun
   ```

The application will start on port 8080.

### API Endpoints & Examples

You can test the endpoints using `curl` or any API client.

#### 1. Success Case
Get a valid product.
```bash
curl -v http://localhost:8080/api/products/123
```
**Response (200 OK):**
```
Product 123
```

#### 2. Resource Not Found (404)
Try to get a product that doesn't exist (ID 999 triggers this).
```bash
curl -v http://localhost:8080/api/products/999
```
**Response (404 Not Found):**
```json
{
  "status": 404,
  "message": "Product not found with ID: 999",
  "timestamp": "2023-10-27T10:00:00.123456",
  "path": "/api/products/999"
}
```

#### 3. Bad Request (400)
Try to provide invalid input (negative ID).
```bash
curl -v http://localhost:8080/api/products/-1
```
**Response (400 Bad Request):**
```json
{
  "status": 400,
  "message": "Product ID cannot be negative",
  "timestamp": "2023-10-27T10:00:05.123456",
  "path": "/api/products/-1"
}
```

#### 4. Internal Server Error (500)
Trigger an unexpected server error (ID "error" triggers this).
```bash
curl -v http://localhost:8080/api/products/error
```
**Response (500 Internal Server Error):**
```json
{
  "status": 500,
  "message": "An unexpected error occurred: Simulated unexpected error",
  "timestamp": "2023-10-27T10:00:10.123456",
  "path": "/api/products/error"
}
```

## Testing

Run the integration tests to verify the error handling logic:

```bash
./gradlew clean test
```

The test output will show the status of each test case (passed, skipped, or failed).
