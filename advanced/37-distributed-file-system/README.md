# Distributed File System (Mini-HDFS)

This project is a simple implementation of a Distributed File System (DFS) in Java, inspired by HDFS. It demonstrates the core concepts of file chunking, distribution across multiple storage nodes (DataNodes), and a central metadata manager (NameNode).

## Features

-   **Chunking**: Files are split into configurable-sized chunks (default 1MB).
-   **Distribution**: Chunks are distributed across available DataNodes (Round-Robin strategy).
-   **Retrieval**: Files can be reassembled by fetching chunks from respective DataNodes.
-   **Architecture**:
    -   **NameNode**: Manages file metadata (filename, size, chunks mapping) and coordinates uploads/downloads.
    -   **DataNode**: Stores the actual raw byte chunks.

## Requirements

-   Java 17+
-   Maven 3.6+
-   Docker & Docker Compose (for running the full cluster)

## Project Structure

-   `NameNodeService`: Handles file upload requests, splits files, distributes chunks, and stores metadata.
-   `DataNodeService`: Handles low-level chunk storage and retrieval.
-   `DfsIntegrationTest`: Verifies the chunking and distribution logic using WireMock to simulate DataNodes.

## How to Run

### Using Docker Compose (Recommended)

This spins up a cluster with 1 NameNode and 2 DataNodes.

1.  Build and start the services:
    ```bash
    docker compose up --build
    ```

2.  The NameNode will be available at `http://localhost:8080`.

### Manual Run

You can run the application locally, but it defaults to acting as both NameNode and DataNode unless configured otherwise.

```bash
mvn spring-boot:run
```

## Usage Examples

### 1. Upload a File

Upload a file to the DFS. The NameNode will chunk it and distribute it to DataNodes.

```bash
# Create a test file
echo "This is a test file content that will be chunked" > test.txt

# Upload
curl -F "file=@test.txt" http://localhost:8080/namenode/files
```

**Response:**
```json
{
  "fileId": "uuid-...",
  "filename": "test.txt",
  "fileSize": 45,
  "chunks": [
    {
      "chunkId": "uuid-...",
      "sequence": 0,
      "dataNodeUrl": "http://datanode1:8080"
    }
  ]
}
```

### 2. Get File Metadata

Retrieve information about a file and its chunks.

```bash
curl http://localhost:8080/namenode/files/{fileId}/metadata
```

### 3. Download a File

Download the reassembled file.

```bash
curl -OJ http://localhost:8080/namenode/files/{fileId}
```

## Testing

The project includes integration tests that verify:
1.  File chunking logic.
2.  Distribution of chunks to configured DataNodes (simulated with WireMock).
3.  File reassembly.

To run the tests:

```bash
mvn clean test
```

**Note:** The tests use `WireMock` to mock the DataNodes, so no external infrastructure is required for the test suite.
