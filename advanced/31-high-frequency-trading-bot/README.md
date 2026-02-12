# High Frequency Trading Bot

This is a high-performance Java backend designed for ultra-low latency event processing using the **LMAX Disruptor** pattern. It simulates a High Frequency Trading (HFT) system where trade "ticks" are ingested and processed into "orders" with microsecond-level latency.

## Overview

The project uses the LMAX Disruptor, a high-performance inter-thread messaging library, to achieve low-latency and high-throughput.

- **Ingestion**: Ticks (Symbol, Price, Volume) are received via a REST API.
- **Processing**: The Disruptor RingBuffer handles the events.
- **Output**: Orders are generated (simulated) and latencies are logged.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (optional, for containerized execution)

## Project Structure

- `src/main/java/com/example/hft/config`: Disruptor configuration.
- `src/main/java/com/example/hft/model`: Event objects (TradeEvent).
- `src/main/java/com/example/hft/service`: Event Producers and Handlers.
- `src/main/java/com/example/hft/controller`: REST API controller.
- `src/test/java/com/example/hft/benchmark`: Benchmark tests for latency measurement.

## How to Run

### Local Execution

1.  **Build the project:**
    ```bash
    mvn clean package
    ```

2.  **Run the application:**
    ```bash
    java -jar target/high-frequency-trading-bot-0.0.1-SNAPSHOT.jar
    ```

### Docker Execution

The project can be run entirely using Docker Compose.

1.  **Start the application:**
    ```bash
    docker compose up --build
    ```
    (Note: Use `docker compose`, not `docker-compose`)

2.  **Stop the application:**
    ```bash
    docker compose down
    ```

## Usage

Once the application is running (locally on port 8080 or via Docker), you can send trade ticks using `curl`.

**Send a Trade Tick:**

```bash
curl -X POST "http://localhost:8080/api/trading/tick?symbol=AAPL&price=150.50&volume=100"
```

**Expected Response:**
`Tick processed`

**Logs:**
Check the application logs to see the generated orders:
```
INFO  c.e.h.s.TradeEventHandler - Order generated: <UUID> for symbol: AAPL at price: 150.5
```

## Testing & Benchmarks

This project includes a benchmark test to measure the latency from "tick" ingestion to "order" generation.

To run the tests and see the benchmark results:

```bash
mvn clean test
```

**Sample Benchmark Output:**
```
Starting latency test with 1000000 events...
Latency Results (microseconds):
Min: 0.2
Avg: 0.5
P50: 0.4
P99: 1.2
P99.9: 5.4
Max: 150.0
Throughput: 2500000 ops/sec
```
*Note: Actual results will vary based on hardware.*
