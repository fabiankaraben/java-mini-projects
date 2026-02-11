# Leaderboard Service

A real-time leaderboard service implemented in Java using Spring Boot and Redis Sorted Sets.

## Features
- Submit scores for users
- Retrieve top N users (leaderboard)
- Retrieve rank and score for specific users
- Fast and efficient ranking using Redis Sorted Sets (ZSET)

## Requirements
- Java 17+
- Docker & Docker Compose

## Tech Stack
- **Java 17**
- **Spring Boot 3.4.1**
- **Redis** (via Spring Data Redis)
- **Gradle**
- **Testcontainers** (for integration testing)

## How to Run

### Using Docker Compose (Recommended)
This will start both the application and the Redis container.

```bash
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Running Tests
This project uses Testcontainers for integration testing. Ensure you have a Docker daemon running.

```bash
./gradlew clean test
```

## API Usage

### Submit a Score
```bash
curl -X POST http://localhost:8080/api/leaderboard/submit \
  -H "Content-Type: application/json" \
  -d '{"userId": "player1", "score": 100}'
```

### Get Top N Users (e.g., Top 10)
```bash
curl http://localhost:8080/api/leaderboard/top/10
```

### Get Top N Users with Scores
```bash
curl http://localhost:8080/api/leaderboard/top-with-scores/10
```

### Get Rank of a User
Returns the 0-based rank (0 is the best).
```bash
curl http://localhost:8080/api/leaderboard/rank/player1
```

### Get Score of a User
```bash
curl http://localhost:8080/api/leaderboard/score/player1
```

## Project Structure
- `src/main/java`: Source code
- `src/test/java`: Integration tests
- `Dockerfile`: Multi-stage Docker build
- `docker-compose.yml`: Docker composition for App + Redis
