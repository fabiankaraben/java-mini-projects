# High Concurrency with Virtual Threads

This mini-project demonstrates a Java backend using **Spring Boot 3.4** and **Java 21 Virtual Threads (Project Loom)** to handle high concurrency.

## 📌 Requirements

- Java 21+
- Maven
- Docker & Docker Compose (optional, for containerized run)
- k6 (for load testing)

## 🚀 Features

- **Virtual Threads**: Enabled via `spring.threads.virtual.enabled=true`.
- **Blocking Endpoint**: `/block` simulates a blocking operation (e.g., DB call) using `Thread.sleep`.
- **High Throughput**: Capable of handling thousands of concurrent requests without exhausting OS threads.

## 🛠 Usage

### Run Locally

1. Build the project:
   ```bash
   mvn clean package
   ```
2. Run the application:
   ```bash
   java -jar target/virtual-threads-demo-0.0.1-SNAPSHOT.jar
   ```

### Run with Docker Compose

1. Start the service:
   ```bash
   docker compose up --build
   ```

### Curl Example

Test the blocking endpoint (default delay 1000ms):
```bash
curl "http://localhost:8080/block?delayMs=500"
```

## 🧪 Testing

### Unit/Integration Tests

Run the standard test suite:
```bash
mvn clean test
```
*Note: Since this project uses Maven, we use `mvn` instead of `./gradlew`.*

### Load Testing with k6

This project includes a k6 script to verify the high concurrency capabilities of Virtual Threads.

1. Install k6: [Installation Guide](https://k6.io/docs/get-started/installation/)
2. Run the load test (ensure the app is running first):
   ```bash
   k6 run k6/load-test.js
   ```

The test ramps up to 2000 concurrent users hitting the blocking endpoint. With Virtual Threads, the application should handle this load with stable memory usage and response times close to the simulated delay.
