package com.example.graphdb.service;

import com.example.graphdb.model.Edge;
import com.example.graphdb.model.Graph;
import com.example.graphdb.model.Node;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GraphService {

    private final Graph graph;

    public GraphService() {
        this.graph = new Graph();
    }

    public void addNode(Node node) {
        graph.addNode(node);
    }

    public void addEdge(Edge edge) {
        graph.addEdge(edge);
    }

    public Node getNode(String id) {
        return graph.getNode(id);
    }

    public Map<String, Node> getAllNodes() {
        return graph.getNodes();
    }
    
    public List<Edge> getEdges(String nodeId) {
        return graph.getEdges(nodeId);
    }

    public List<String> bfs(String startNodeId) {
        return graph.bfs(startNodeId);
    }

    public List<String> dfs(String startNodeId) {
        return graph.dfs(startNodeId);
    }

    public List<String> shortestPath(String startId, String endId) {
        return graph.shortestPath(startId, endId);
    }
}
