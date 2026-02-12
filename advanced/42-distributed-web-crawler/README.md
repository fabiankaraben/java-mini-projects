# Distributed Web Crawler

A distributed web crawler implementation using Java, Spring Boot, and Hazelcast. This system coordinates multiple crawler nodes to visit pages, extracting links and content in a distributed manner.

## Requirements

- Java 17+
- Docker & Docker Compose
- Gradle (wrapper provided)

## Features

- **Distributed Coordination**: Uses Hazelcast to share the crawl frontier (URL queue) and visited set across multiple nodes.
- **Scalable**: Can run multiple instances to parallelize crawling.
- **Politeness**: Simple visited check to avoid loops.
- **REST API**: Control the crawler and retrieve results via HTTP.

## Project Structure

- `src/main/java`: Source code
  - `model`: Data models (`CrawledPage`)
  - `service`: Core crawling logic (`CrawlerService`)
  - `config`: Hazelcast configuration
  - `controller`: REST endpoints
- `src/test/java`: Integration tests
- `Dockerfile`: Container definition
- `docker-compose.yml`: Multi-node deployment setup

## How to Run

### Local (Single Node)

1. Build and run the application:
   ```bash
   ./gradlew bootRun
   ```

### Distributed (Docker Compose)

1. Build the Docker image and start the cluster (2 nodes):
   ```bash
   docker compose up --build
   ```

   This will start two crawler nodes:
   - Node 1: Port 8080
   - Node 2: Port 8081

## Usage

You can interact with any node in the cluster.

### 1. Start a Crawl

Submit a seed URL to start the crawling process.

```bash
curl -X POST "http://localhost:8080/api/crawler/start?url=https://en.wikipedia.org/wiki/Distributed_computing&maxPages=20"
```

### 2. Get Results

Retrieve the list of crawled pages. Since the data is stored in a distributed Hazelcast map, you can query any node.

```bash
curl "http://localhost:8081/api/crawler/results" | json_pp
```

## Testing

This project includes integration tests that use **WireMock** to simulate a website and verify the crawler's behavior.

To run the tests:

```bash
./gradlew clean test
```

Expected output should show the test execution events:
```
> Task :test
com.example.crawler.DistributedWebCrawlerIntegrationTest > testCrawling() PASSED
```
