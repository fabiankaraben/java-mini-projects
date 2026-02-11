# Peer-to-Peer File Sharing

This is a backend in Java using raw TCP for decentralized file sharing. It allows multiple peer instances to share files directly with each other.

## Requirements

- Java 17
- Docker & Docker Compose (optional, for running with containers)
- Gradle (wrapper included)

## Features

- **Decentralized Architecture**: Each node acts as both a client and a server.
- **TCP File Transfer**: Files are transferred directly between peers using raw TCP sockets.
- **REST API**: Control peers via HTTP endpoints to list files and trigger downloads.

## Project Structure

- `src/main/java`: Source code.
- `src/test/java`: Integration tests.
- `docker-compose.yml`: Configuration to run multiple peers.

## How to Run

### Local (Gradle)

1.  Build the project:
    ```bash
    ./gradlew clean build
    ```

2.  Run a peer instance (example for Peer 1):
    ```bash
    java -jar build/libs/peer-to-peer-file-sharing-0.0.1-SNAPSHOT.jar --server.port=8081 --p2p.server.port=9091 --p2p.storage.path=./storage/peer1
    ```

3.  Run another peer instance (example for Peer 2):
    ```bash
    java -jar build/libs/peer-to-peer-file-sharing-0.0.1-SNAPSHOT.jar --server.port=8082 --p2p.server.port=9092 --p2p.storage.path=./storage/peer2
    ```

### Docker Compose

1.  Build and start the peers:
    ```bash
    docker compose up --build
    ```
    This will start two peers:
    - **Peer 1**: HTTP 8081, P2P 9091
    - **Peer 2**: HTTP 8082, P2P 9092

## Usage Examples

Assume you have Peer 1 (HTTP 8081, P2P 9091) and Peer 2 (HTTP 8082, P2P 9092).

1.  **Add a file to Peer 1**:
    Place a file named `test.txt` in `./storage/peer1` (or the volume mapped in docker-compose).

2.  **List files on Peer 1**:
    ```bash
    curl http://localhost:8081/api/files
    ```

3.  **Download file from Peer 1 to Peer 2**:
    Trigger Peer 2 to download `test.txt` from Peer 1.
    ```bash
    curl -X POST "http://localhost:8082/api/peers/download?host=peer1&port=9091&fileName=test.txt"
    ```
    *Note: If running locally without Docker, use `host=localhost`.*

4.  **Verify file on Peer 2**:
    ```bash
    curl http://localhost:8082/api/files
    ```

## Testing

Run the integration tests to verify the peer-to-peer file transfer logic locally.

```bash
./gradlew clean test
```

The tests spawn two Spring Boot application contexts on different ports and simulate a file transfer between them.
