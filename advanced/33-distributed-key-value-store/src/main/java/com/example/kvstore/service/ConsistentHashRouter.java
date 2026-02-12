package com.example.kvstore.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

public class ConsistentHashRouter {
    private final SortedMap<Integer, String> ring = new TreeMap<>();

    public ConsistentHashRouter(List<String> nodes, int virtualNodes) {
        for (String node : nodes) {
            addNode(node, virtualNodes);
        }
    }

    private void addNode(String node, int virtualNodes) {
        for (int i = 0; i < virtualNodes; i++) {
            ring.put(hash(node + "#" + i), node);
        }
    }

    // Get the preference list for a key (primary + replicas)
    public List<String> getPreferenceList(String key, int replicationFactor) {
        if (ring.isEmpty()) {
            return new ArrayList<>();
        }

        int hash = hash(key);
        if (!ring.containsKey(hash)) {
            SortedMap<Integer, String> tailMap = ring.tailMap(hash);
            hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        }

        List<String> nodes = new ArrayList<>();
        // Start from the found node and move clockwise to find distinct nodes
        // This is a simplified approach. In a real ring, we iterate.
        // Since we have a TreeMap, getting the next N distinct values is slightly tricky if we just jump.
        // We will iterate through the keys starting from the found hash.
        
        // Find the iterator position
        SortedMap<Integer, String> tailMap = ring.tailMap(hash);
        // We need to wrap around.
        // Let's combine tailMap and headMap to iterate easily.
        
        List<String> allNodesInOrder = new ArrayList<>(tailMap.values());
        allNodesInOrder.addAll(ring.headMap(hash).values());
        
        for (String node : allNodesInOrder) {
            if (!nodes.contains(node)) {
                nodes.add(node);
            }
            if (nodes.size() >= replicationFactor) {
                break;
            }
        }
        
        return nodes;
    }

    private int hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(key.getBytes(StandardCharsets.UTF_8));
            // Convert first 4 bytes to int
            int h = 0;
            for (int i = 0; i < 4; i++) {
                h <<= 8;
                h |= ((int) digest[i]) & 0xFF;
            }
            return h;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
