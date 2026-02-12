package com.example.graphdb.service;

import com.example.graphdb.model.Edge;
import com.example.graphdb.model.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

    private GraphService graphService;

    @BeforeEach
    void setUp() {
        graphService = new GraphService();
    }

    @Test
    void testAddNode() {
        Node node = new Node("A");
        graphService.addNode(node);
        assertNotNull(graphService.getNode("A"));
    }

    @Test
    void testAddEdge() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        graphService.addNode(nodeA);
        graphService.addNode(nodeB);

        Edge edge = new Edge("A", "B", 1.0);
        graphService.addEdge(edge);

        List<Edge> edges = graphService.getEdges("A");
        assertEquals(1, edges.size());
        assertEquals("B", edges.get(0).getTo());
    }

    @Test
    void testBfs() {
        // A -> B -> C
        // |
        // v
        // D
        graphService.addNode(new Node("A"));
        graphService.addNode(new Node("B"));
        graphService.addNode(new Node("C"));
        graphService.addNode(new Node("D"));

        graphService.addEdge(new Edge("A", "B", 1.0));
        graphService.addEdge(new Edge("B", "C", 1.0));
        graphService.addEdge(new Edge("A", "D", 1.0));

        List<String> visited = graphService.bfs("A");
        assertTrue(visited.contains("A"));
        assertTrue(visited.contains("B"));
        assertTrue(visited.contains("C"));
        assertTrue(visited.contains("D"));
        assertEquals("A", visited.get(0)); // Start node
    }

    @Test
    void testDfs() {
        // A -> B -> C
        graphService.addNode(new Node("A"));
        graphService.addNode(new Node("B"));
        graphService.addNode(new Node("C"));

        graphService.addEdge(new Edge("A", "B", 1.0));
        graphService.addEdge(new Edge("B", "C", 1.0));

        List<String> visited = graphService.dfs("A");
        assertEquals(3, visited.size());
        assertTrue(visited.contains("A"));
        assertTrue(visited.contains("B"));
        assertTrue(visited.contains("C"));
        assertEquals("A", visited.get(0));
    }

    @Test
    void testShortestPath() {
        // A --1--> B --2--> C
        // |                 ^
        // --------5---------|
        graphService.addNode(new Node("A"));
        graphService.addNode(new Node("B"));
        graphService.addNode(new Node("C"));

        graphService.addEdge(new Edge("A", "B", 1.0));
        graphService.addEdge(new Edge("B", "C", 2.0));
        graphService.addEdge(new Edge("A", "C", 5.0));

        List<String> path = graphService.shortestPath("A", "C");
        // Path should be A -> B -> C (cost 3.0) vs A -> C (cost 5.0)
        assertEquals(3, path.size());
        assertEquals("A", path.get(0));
        assertEquals("B", path.get(1));
        assertEquals("C", path.get(2));
    }
}
