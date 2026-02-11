# QR Code Generator

## Description
This is a backend in Java using **ZXing** (Zebra Crossing) library that generates QR code images from text. It exposes a REST API where users can send text and receive a PNG image of the QR code.

## Requirements
- Java 17+
- Gradle
- Docker (optional, for running with Docker Compose)

## API Endpoints

### Generate QR Code
**GET** `/api/qrcode`

**Parameters:**
- `text` (required): The content to encode in the QR code.
- `width` (optional): The width of the image (default: 300).
- `height` (optional): The height of the image (default: 300).

**Example Usage:**

Generate a QR code for "Hello World":
```bash
curl "http://localhost:8080/api/qrcode?text=Hello%20World" --output qr.png
```

Generate a larger QR code:
```bash
curl "http://localhost:8080/api/qrcode?text=https://example.com&width=500&height=500" --output website.png
```

## Running the Application

### Locally
```bash
./gradlew bootRun
```

### With Docker Compose
```bash
docker compose up --build
```

## Running Tests
This project includes integration tests that verify the QR code generation by creating a QR code image and then decoding it back to text to ensure the content matches.

```bash
./gradlew clean test
```

The test output will show the passed/failed status of the tests.
