# Blockchain Integration Mini-Project

This project demonstrates a Java backend using **Spring Boot** and **Web3j** to interact with Ethereum-compatible blockchains (like Geth or Ganache).

## Requirements

- Java 17+
- Docker & Docker Compose (for running the local blockchain and integration tests)

## Technologies

- **Java 17**
- **Spring Boot 3.4.1**
- **Web3j** (Ethereum interaction)
- **Gradle** (Dependency Management)
- **Testcontainers** (Integration Testing with Ganache)

## Project Structure

- `src/main/java`: Source code
  - `config`: Web3j configuration
  - `service`: Blockchain interaction logic
  - `controller`: REST endpoints
- `src/test/java`: Integration tests
- `docker-compose.yml`: Runs the application and a Ganache instance

## How to Run

### Using Docker Compose (Recommended)

This will start both the Spring Boot application and a Ganache blockchain node.

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Local Development

1. Start Ganache independently (or use the one from docker-compose):
   ```bash
   docker compose up ganache
   ```
2. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints & Curl Examples

### 1. Get Client Version
Returns the version of the connected Ethereum client.

```bash
curl http://localhost:8080/api/blockchain/version
```
Response:
```json
{
  "version": "EthereumJS TestRPC/v2.13.2/ethereum-js"
}
```

### 2. Get Current Block Number
Returns the latest block number.

```bash
curl http://localhost:8080/api/blockchain/block-number
```
Response:
```json
{
  "blockNumber": 0
}
```

### 3. Get Account Balance
Returns the balance (in Ether) for a specific address.

```bash
# Replace with a valid address from your Ganache instance
curl http://localhost:8080/api/blockchain/balance/0x123...
```
Response:
```json
{
  "address": "0x123...",
  "balance_eth": "100"
}
```

## Testing

Integration tests use **Testcontainers** to spin up a Ganache instance automatically.
The build is configured to show test events (passed, skipped, failed).

Run the tests with:

```bash
./gradlew clean test
```

Expected output will show the test execution details.
