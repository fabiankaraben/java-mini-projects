# Graph Database Implementation

This project implements a simple in-memory Graph Database in Java using Spring Boot. It supports Nodes, Edges (directed and weighted), and basic graph algorithms like Breadth-First Search (BFS), Depth-First Search (DFS), and Dijkstra's Shortest Path.

## Requirements

- Java 17+
- Maven 3.6+
- Docker (optional, for containerized execution)

## Features

- **Node Management**: Create and retrieve nodes with properties.
- **Edge Management**: Create directed, weighted edges between nodes.
- **Traversal Algorithms**:
  - BFS (Breadth-First Search)
  - DFS (Depth-First Search)
- **Pathfinding**:
  - Dijkstra's Shortest Path Algorithm

## Project Structure

```
src/
├── main/java/com/example/graphdb/
│   ├── controller/       # REST API Controllers
│   ├── model/            # Graph Data Structures (Node, Edge, Graph)
│   ├── service/          # Business Logic
│   └── GraphDatabaseApplication.java
└── test/java/com/example/graphdb/
    ├── controller/       # Controller Integration Tests
    └── service/          # Service Unit Tests
```

## How to Run

### Local Execution (Maven)

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### Docker Execution

```bash
docker compose up --build
```

## Testing

To run the unit and integration tests:

```bash
mvn clean test
```

## API Usage

### 1. Add Nodes

Create nodes 'A', 'B', 'C', 'D', 'E'.

```bash
curl -X POST http://localhost:8080/api/graph/nodes \
  -H "Content-Type: application/json" \
  -d '{"id": "A", "properties": {"label": "City"}}'

curl -X POST http://localhost:8080/api/graph/nodes \
  -H "Content-Type: application/json" \
  -d '{"id": "B"}'

curl -X POST http://localhost:8080/api/graph/nodes \
  -H "Content-Type: application/json" \
  -d '{"id": "C"}'

curl -X POST http://localhost:8080/api/graph/nodes \
  -H "Content-Type: application/json" \
  -d '{"id": "D"}'

curl -X POST http://localhost:8080/api/graph/nodes \
  -H "Content-Type: application/json" \
  -d '{"id": "E"}'
```

### 2. Add Edges

Create edges: A->B, B->C, A->C, C->D, B->E.

```bash
# A -> B (weight 1)
curl -X POST http://localhost:8080/api/graph/edges \
  -H "Content-Type: application/json" \
  -d '{"from": "A", "to": "B", "weight": 1.0}'

# B -> C (weight 2)
curl -X POST http://localhost:8080/api/graph/edges \
  -H "Content-Type: application/json" \
  -d '{"from": "B", "to": "C", "weight": 2.0}'

# A -> C (weight 5) - Longer path direct
curl -X POST http://localhost:8080/api/graph/edges \
  -H "Content-Type: application/json" \
  -d '{"from": "A", "to": "C", "weight": 5.0}'

# C -> D (weight 1)
curl -X POST http://localhost:8080/api/graph/edges \
  -H "Content-Type: application/json" \
  -d '{"from": "C", "to": "D", "weight": 1.0}'

# B -> E (weight 10)
curl -X POST http://localhost:8080/api/graph/edges \
  -H "Content-Type: application/json" \
  -d '{"from": "B", "to": "E", "weight": 10.0}'
```

### 3. Get Node

```bash
curl http://localhost:8080/api/graph/nodes/A
```

### 4. Run BFS

Explore the graph from 'A'.

```bash
curl http://localhost:8080/api/graph/bfs/A
# Output: ["A", "B", "C", "E", "D"] (Order may vary slightly for siblings)
```

### 5. Run DFS

Explore the graph from 'A' depth-first.

```bash
curl http://localhost:8080/api/graph/dfs/A
```

### 6. Find Shortest Path

Find shortest path from 'A' to 'D'. 
Path 1: A -> C -> D (Cost: 5 + 1 = 6)
Path 2: A -> B -> C -> D (Cost: 1 + 2 + 1 = 4) -> Winner

```bash
curl "http://localhost:8080/api/graph/shortest-path?start=A&end=D"
# Output: ["A", "B", "C", "D"]
```
