package com.example.graphdb.controller;

import com.example.graphdb.model.Edge;
import com.example.graphdb.model.Node;
import com.example.graphdb.service.GraphService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GraphController.class)
public class GraphControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GraphService graphService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddNode() throws Exception {
        Node node = new Node("A");
        
        mockMvc.perform(post("/api/graph/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(node)))
                .andExpect(status().isOk())
                .andExpect(content().string("Node added: A"));
                
        Mockito.verify(graphService).addNode(any(Node.class));
    }

    @Test
    public void testAddEdge() throws Exception {
        Edge edge = new Edge("A", "B", 1.0);

        mockMvc.perform(post("/api/graph/edges")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(edge)))
                .andExpect(status().isOk())
                .andExpect(content().string("Edge added from A to B"));

        Mockito.verify(graphService).addEdge(any(Edge.class));
    }

    @Test
    public void testBfs() throws Exception {
        List<String> path = Arrays.asList("A", "B", "C");
        Mockito.when(graphService.bfs("A")).thenReturn(path);

        mockMvc.perform(get("/api/graph/bfs/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("A"))
                .andExpect(jsonPath("$[1]").value("B"))
                .andExpect(jsonPath("$[2]").value("C"));
    }

    @Test
    public void testShortestPath() throws Exception {
        List<String> path = Arrays.asList("A", "B", "C");
        Mockito.when(graphService.shortestPath("A", "C")).thenReturn(path);

        mockMvc.perform(get("/api/graph/shortest-path")
                .param("start", "A")
                .param("end", "C"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0]").value("A"))
                .andExpect(jsonPath("$[2]").value("C"));
    }
}
