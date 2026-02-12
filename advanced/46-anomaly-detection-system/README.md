# Anomaly Detection System

## Description
This is a Java backend project that implements a statistical Anomaly Detection System using the Z-score method on streaming data. The system maintains a sliding window of recent data points to calculate the mean and standard deviation, allowing it to identify outliers in real-time.

## Requirements
- Java 17+
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure
- **Controller**: `AnomalyController` exposes a REST endpoint to receive data points.
- **Service**: `AnomalyDetectionService` implements the Z-score algorithm with a sliding window.
- **Model**: `DataPoint` (input) and `AnomalyResult` (output).

## Usage

### Running Locally with Gradle
```bash
./gradlew bootRun
```

### Running with Docker Compose
```bash
docker compose up --build
```

### API Endpoints

#### Detect Anomaly
**POST** `/api/anomaly/detect`

Accepts a JSON object with a `value`.

**Request:**
```bash
curl -X POST http://localhost:8080/api/anomaly/detect \
  -H "Content-Type: application/json" \
  -d '{"value": 10.5}'
```

**Response (Normal):**
```json
{
  "value": 10.5,
  "zscore": 0.12,
  "anomaly": false,
  "message": "Normal data point"
}
```

**Response (Anomaly):**
```json
{
  "value": 100.0,
  "zscore": 4.5,
  "anomaly": true,
  "message": "Anomaly detected!"
}
```

## Testing

The project includes unit tests that simulate a stream of normal data followed by an outlier to verify alert generation.

To run the tests:
```bash
./gradlew clean test
```

Expected output will show passed tests with details:
```
> Task :test
...
com.example.anomalydetection.service.AnomalyDetectionServiceTest > testAnomalyDetection() PASSED
...
```
