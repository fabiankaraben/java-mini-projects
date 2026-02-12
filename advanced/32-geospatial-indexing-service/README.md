# Geospatial Indexing Service

This project implements a backend service for geospatial indexing using a **Quadtree** data structure in Java. It allows adding geospatial points (latitude, longitude) and querying for points within a specified radius.

## Requirements

- Java 17+
- Gradle
- Docker & Docker Compose (optional, for containerized execution)

## Features

- **Quadtree Implementation**: Efficient spatial indexing for points.
- **Radius Search**: Find points within a given distance (in km) from a coordinate.
- **REST API**: Simple endpoints to interact with the service.

## Project Structure

- `src/main/java/com/example/geospatial/structure/Quadtree.java`: The core Quadtree logic.
- `src/main/java/com/example/geospatial/service/GeospatialService.java`: Service layer handling distance calculations and tree operations.
- `src/main/java/com/example/geospatial/controller/GeospatialController.java`: REST controller.

## How to Run

### Local Execution
```bash
./gradlew bootRun
```

### Docker Execution
```bash
docker compose up --build
```

## API Usage

### 1. Add a Point
```bash
curl -X POST http://localhost:8080/api/geospatial/points \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 40.7580,
    "longitude": -73.9855,
    "id": "nyc",
    "data": "Times Square"
  }'
```

### 2. Search Points within Radius
Find points within 5km of a location (e.g., near Times Square):
```bash
curl "http://localhost:8080/api/geospatial/search?lat=40.7580&lon=-73.9855&radius=5.0"
```

## Running Tests

To run the unit tests and verify the spatial accuracy:

```bash
./gradlew clean test
```

Expected output will show test events:
```
GeospatialServiceTest > testAddAndFindPointsWithinRadius() PASSED
GeospatialServiceTest > testBoundaryConditions() PASSED
GeospatialServiceTest > testEmptyResult() PASSED
```
