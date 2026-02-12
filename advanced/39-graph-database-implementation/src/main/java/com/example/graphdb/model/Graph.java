package com.example.graphdb.model;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodes;
    private final Map<String, List<Edge>> adjacencyList;

    public Graph() {
        this.nodes = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
        adjacencyList.putIfAbsent(node.getId(), new ArrayList<>());
    }

    public void addEdge(Edge edge) {
        if (!nodes.containsKey(edge.getFrom()) || !nodes.containsKey(edge.getTo())) {
            throw new IllegalArgumentException("Both nodes must exist before adding an edge");
        }
        adjacencyList.get(edge.getFrom()).add(edge);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges(String nodeId) {
        return adjacencyList.getOrDefault(nodeId, Collections.emptyList());
    }

    // BFS Traversal
    public List<String> bfs(String startNodeId) {
        if (!nodes.containsKey(startNodeId)) return Collections.emptyList();

        List<String> visitedOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(startNodeId);
        queue.add(startNodeId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            visitedOrder.add(currentId);

            for (Edge edge : adjacencyList.getOrDefault(currentId, Collections.emptyList())) {
                String neighborId = edge.getTo();
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(neighborId);
                }
            }
        }
        return visitedOrder;
    }

    // DFS Traversal
    public List<String> dfs(String startNodeId) {
        if (!nodes.containsKey(startNodeId)) return Collections.emptyList();

        List<String> visitedOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        stack.push(startNodeId);

        while (!stack.isEmpty()) {
            String currentId = stack.pop();

            if (!visited.contains(currentId)) {
                visited.add(currentId);
                visitedOrder.add(currentId);

                // Add neighbors to stack in reverse order to visit them in natural order (optional but common)
                List<Edge> edges = adjacencyList.getOrDefault(currentId, Collections.emptyList());
                // We reverse just to mimic recursive DFS behavior if we process neighbors left-to-right
                // But since it's a list, order matters.
                // Let's just push them.
                for (Edge edge : edges) {
                   if (!visited.contains(edge.getTo())) {
                       stack.push(edge.getTo());
                   }
                }
            }
        }
        return visitedOrder;
    }

    // Dijkstra's Shortest Path
    public List<String> shortestPath(String startId, String endId) {
        if (!nodes.containsKey(startId) || !nodes.containsKey(endId)) return Collections.emptyList();

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        Set<String> visited = new HashSet<>();

        for (String nodeId : nodes.keySet()) {
            distances.put(nodeId, Double.MAX_VALUE);
        }
        distances.put(startId, 0.0);
        queue.add(startId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            visited.add(currentId);

            if (currentId.equals(endId)) break;

            if (distances.get(currentId) == Double.MAX_VALUE) break;

            for (Edge edge : adjacencyList.getOrDefault(currentId, Collections.emptyList())) {
                String neighborId = edge.getTo();
                if (visited.contains(neighborId)) continue;

                double newDist = distances.get(currentId) + edge.getWeight();
                if (newDist < distances.get(neighborId)) {
                    distances.put(neighborId, newDist);
                    previous.put(neighborId, currentId);
                    // PriorityQueue doesn't support decreaseKey efficiently, so we re-add
                    queue.add(neighborId);
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String step = endId;
        if (!previous.containsKey(step) && !step.equals(startId)) {
            return Collections.emptyList(); // No path
        }
        
        while (step != null) {
            path.add(0, step);
            step = previous.get(step);
        }
        return path;
    }
}
