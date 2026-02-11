# IoT Device Gateway

## Description
This mini-project is a **Java backend** that acts as a gateway between IoT devices (simulated via **MQTT**) and a cloud system (via **REST API**). It uses **Spring Boot** and the **HiveMQ MQTT Client** to bridge messages.

## Features
- **MQTT Ingestion**: Subscribes to device data topics (`devices/{id}/data`) and stores the latest state in memory.
- **Command Control**: Exposes a REST endpoint to send commands to devices via MQTT (`devices/{id}/command`).
- **REST API**: Retrieve the latest data from devices.

## Requirements
- Java 17+
- Docker & Docker Compose (for running the broker and app)
- Gradle

## Project Structure
- `src/main/java`: Spring Boot application and MQTT service.
- `src/test/java`: Integration tests using **Testcontainers**.
- `mosquitto/config`: Configuration for the Mosquitto MQTT broker.

## How to Run
### Using Docker Compose (Recommended)
This starts both the MQTT broker (Mosquitto) and the Application.
```bash
docker compose up --build
```

### Local Development
1. Start the MQTT broker:
   ```bash
   docker run -p 1883:1883 -v $(pwd)/mosquitto/config/mosquitto.conf:/mosquitto/config/mosquitto.conf eclipse-mosquitto:2.0
   ```
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## How to Use

### 1. Simulate a Device sending Data (MQTT)
You can use `mosquitto_pub` or another MQTT client.
```bash
# Publish temperature data for device-1
docker exec -it $(docker compose ps -q mosquitto) mosquitto_pub -t "devices/device-1/data" -m "{\"temp\": 22.5, \"humidity\": 60}"
```

### 2. Get Device Data (REST)
```bash
curl http://localhost:8080/api/devices/device-1/data
# Output: {"temp": 22.5, "humidity": 60}
```

### 3. Send Command to Device (REST -> MQTT)
```bash
curl -X POST -H "Content-Type: text/plain" -d "REBOOT" http://localhost:8080/api/devices/device-1/command
```

### 4. Verify Command (MQTT Listener)
You can listen to the command topic:
```bash
docker exec -it $(docker compose ps -q mosquitto) mosquitto_sub -t "devices/device-1/command"
# Output should be: REBOOT
```

## Testing
This project uses **Testcontainers** to spin up an ephemeral Mosquitto broker for integration tests.

To run tests:
```bash
./gradlew clean test
```
The test output will show `passed`, `skipped`, and `failed` events.
