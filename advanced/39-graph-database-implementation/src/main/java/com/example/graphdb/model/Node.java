package com.example.graphdb.model;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private String id;
    private Map<String, Object> properties;

    public Node() {
        this.properties = new HashMap<>();
    }

    public Node(String id) {
        this.id = id;
        this.properties = new HashMap<>();
    }

    public Node(String id, Map<String, Object> properties) {
        this.id = id;
        this.properties = properties != null ? properties : new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Node{id='" + id + "', properties=" + properties + "}";
    }
}
