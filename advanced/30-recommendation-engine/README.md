# Recommendation Engine

This is a backend mini-project in Java using Spring Boot and a simple User-Based Collaborative Filtering algorithm to provide recommendations.

## Requirements
- Java 17
- Docker & Docker Compose
- Gradle

## Features
- **Add Ratings**: Users can rate items.
- **Get Recommendations**: Returns top N recommended items for a user based on similarity with other users (Cosine Similarity).
- **Get User Ratings**: Retrieve ratings given by a user.

## Project Structure
- `src/main/java`: Source code
- `src/test/java`: Unit tests
- `Dockerfile`: Multi-stage Docker build
- `docker-compose.yml`: Docker Compose configuration

## API Endpoints

### 1. Add Rating
Records a user's rating for an item.
```bash
curl -X POST "http://localhost:8080/api/recommendations/rate?userId=UserA&itemId=Item1&rating=5.0"
```

### 2. Get Recommendations
Get top recommendations for a user.
```bash
curl -X GET "http://localhost:8080/api/recommendations/UserA?limit=3"
```

### 3. Get User Ratings
See what a user has rated.
```bash
curl -X GET "http://localhost:8080/api/recommendations/ratings/UserA"
```

## Running the Application

### Using Docker Compose (Recommended)
```bash
docker compose up --build
```

### Using Gradle
```bash
./gradlew bootRun
```

## Testing
The project includes unit tests verifying the collaborative filtering logic with a known dataset matrix.

To run the tests:
```bash
./gradlew clean test
```
**Note**: The test output will show events: "passed", "skipped", "failed".
