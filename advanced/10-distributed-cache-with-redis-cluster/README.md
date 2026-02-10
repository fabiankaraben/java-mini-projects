# Distributed Cache with Redis Cluster

This mini-project demonstrates a **Distributed Cache** using **Java (Spring Boot)** and **Redis Cluster**. It uses the **Lettuce** Redis client which supports Redis Cluster features like topology refresh and redirect handling.

## Requirements
- Java 17+
- Docker & Docker Compose (for running the cluster locally)
- Gradle

## Features
- **Redis Cluster Support**: Connects to a sharded Redis Cluster.
- **REST API**: Simple endpoints to Set, Get, and Delete cache entries.
- **Integration Tests**: Uses `Testcontainers` (specifically `GenericContainer` with `grokzen/redis-cluster`) to spin up a Redis Cluster in Docker for tests.

## How to Run

### 1. Start Redis Cluster (Docker)
This project includes a `docker-compose.yml` configured with `grokzen/redis-cluster`, which sets up a minimal 6-node cluster (3 masters, 3 slaves).

It maps the cluster ports to **7010-7015** on the host to avoid conflicts with common services (like AirPlay Receiver on port 7000).

```bash
docker compose up -d
```

### 2. Run the Application
```bash
./gradlew bootRun
```

### 3. Usage (Curl Examples)

**Set a Value**
```bash
curl -X POST -H "Content-Type: text/plain" -d "Hello Distributed World" http://localhost:8080/api/cache/myKey
```

**Get a Value**
```bash
curl http://localhost:8080/api/cache/myKey
```

**Delete a Value**
```bash
curl -X DELETE http://localhost:8080/api/cache/myKey
```

## Testing
This project uses **Testcontainers** to verify interaction with a real Redis Cluster environment.

Run the tests with:
```bash
./gradlew clean test
```

You should see output indicating test events:
```
> Task :test
com.example.rediscluster.RedisClusterIntegrationTest > testClusterOperations() PASSED
```

## Project Structure
- `src/main/java`: Spring Boot Application & Controller.
- `src/test/java`: Integration Tests using Testcontainers.
- `docker-compose.yml`: Local Redis Cluster setup.
- `Dockerfile`: Application container definition.
