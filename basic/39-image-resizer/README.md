# Image Resizer

🔹 A simple backend in Java using `java.awt` and `Javalin` that resizes uploaded images on the fly.

## Requirements

*   Java 17 or higher
*   Maven

## How to Run

1.  Navigate to the project directory:
    ```bash
    cd basic/39-image-resizer
    ```

2.  Build and run the application:
    ```bash
    mvn clean package
    java -jar target/image-resizer-1.0-SNAPSHOT.jar
    ```
    Or simply:
    ```bash
    mvn exec:java -Dexec.mainClass="com.fabiankaraben.imageresizer.App"
    ```

The server will start on port `7070`.

## How to Use

You can resize an image by sending a POST request to `/resize` with the image file and the desired dimensions.

### Parameters

*   `image`: The image file to resize (multipart/form-data).
*   `width`: The target width (integer).
*   `height`: The target height (integer).

### Examples (using curl)

**Resize an image to 200x200:**

```bash
curl -X POST -F "image=@/path/to/your/image.jpg" "http://localhost:7070/resize?width=200&height=200" --output resized.png
```

**Resize an image to 100x50:**

```bash
curl -X POST -F "image=@/path/to/logo.png" "http://localhost:7070/resize?width=100&height=50" --output resized_logo.png
```

## Testing

This project includes integration tests that upload an image and verify the returned image dimensions using `ImageIO`.

To run the tests:

```bash
mvn clean test
```
