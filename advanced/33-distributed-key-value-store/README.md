# Distributed Key-Value Store

A simple distributed key-value store in Java, implementing consistent hashing and replication (inspired by DynamoDB basics).

## Requirements

- Java 17+
- Maven
- Docker & Docker Compose (for running the full cluster and integration tests)

## Features

- **Consistent Hashing**: Distributes keys across nodes using a hash ring.
- **Replication**: Replicates data to multiple nodes (Replication Factor = 2) for fault tolerance.
- **REST API**: Simple `GET` and `PUT` operations.
- **Fault Tolerance**: Can retrieve data even if a node goes down (provided replicas are available).

## Project Structure

- `src/main/java`: Source code.
  - `config`: Configuration classes.
  - `controller`: REST API endpoints.
  - `model`: Data models (if any).
  - `service`: Core logic (ConsistentHashRouter, KeyValueService).
- `src/test/java`: Integration tests.
- `Dockerfile`: Application container definition.
- `docker-compose.yml`: Cluster definition (3 nodes).

## How to Run

### 1. Build the Application

```bash
mvn clean package -DskipTests
```

### 2. Run with Docker Compose

This starts a 3-node cluster.

```bash
docker compose up --build
```

Nodes will be available at:
- Node 1: `http://localhost:8081`
- Node 2: `http://localhost:8082`
- Node 3: `http://localhost:8083`

### 3. Usage (Curl Examples)

**Store a Key-Value Pair:**

You can send the request to any node. The node will route it or replicate it accordingly.

```bash
curl -X POST http://localhost:8081/api/kv \
     -H "Content-Type: application/json" \
     -d '{"key": "user:123", "value": "Fabian"}'
```

**Retrieve a Value:**

```bash
curl "http://localhost:8081/api/kv?key=user:123"
```

Try retrieving from other nodes to verify replication:

```bash
curl "http://localhost:8082/api/kv?key=user:123"
```

## Testing

The project includes integration tests that verify fault tolerance by killing a node and checking data availability.

To run the tests, you first need to build the docker image (since tests use it):

```bash
docker build -t distributed-kv-store:latest .
```

Then run the tests:

```bash
mvn test
```

*Note: The integration test uses Testcontainers and requires a running Docker environment.*
