package com.example.graphdb.controller;

import com.example.graphdb.model.Edge;
import com.example.graphdb.model.Node;
import com.example.graphdb.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;

    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping("/nodes")
    public ResponseEntity<String> addNode(@RequestBody Node node) {
        if (node.getId() == null || node.getId().isEmpty()) {
            return ResponseEntity.badRequest().body("Node ID is required");
        }
        graphService.addNode(node);
        return ResponseEntity.ok("Node added: " + node.getId());
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<Node> getNode(@PathVariable String id) {
        Node node = graphService.getNode(id);
        if (node == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(node);
    }

    @GetMapping("/nodes")
    public ResponseEntity<Map<String, Node>> getAllNodes() {
        return ResponseEntity.ok(graphService.getAllNodes());
    }

    @PostMapping("/edges")
    public ResponseEntity<String> addEdge(@RequestBody Edge edge) {
        if (edge.getFrom() == null || edge.getTo() == null) {
            return ResponseEntity.badRequest().body("Edge must have 'from' and 'to' nodes");
        }
        try {
            graphService.addEdge(edge);
            return ResponseEntity.ok("Edge added from " + edge.getFrom() + " to " + edge.getTo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/nodes/{id}/edges")
    public ResponseEntity<List<Edge>> getEdges(@PathVariable String id) {
        return ResponseEntity.ok(graphService.getEdges(id));
    }

    @GetMapping("/bfs/{startNodeId}")
    public ResponseEntity<List<String>> bfs(@PathVariable String startNodeId) {
        return ResponseEntity.ok(graphService.bfs(startNodeId));
    }

    @GetMapping("/dfs/{startNodeId}")
    public ResponseEntity<List<String>> dfs(@PathVariable String startNodeId) {
        return ResponseEntity.ok(graphService.dfs(startNodeId));
    }

    @GetMapping("/shortest-path")
    public ResponseEntity<List<String>> shortestPath(@RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(graphService.shortestPath(start, end));
    }
}
