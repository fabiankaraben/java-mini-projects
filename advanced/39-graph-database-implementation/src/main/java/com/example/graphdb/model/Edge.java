package com.example.graphdb.model;

import java.util.HashMap;
import java.util.Map;

public class Edge {
    private String from;
    private String to;
    private double weight;
    private Map<String, Object> properties;

    public Edge() {
        this.properties = new HashMap<>();
        this.weight = 1.0;
    }

    public Edge(String from, String to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.properties = new HashMap<>();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Edge{from='" + from + "', to='" + to + "', weight=" + weight + "}";
    }
}
