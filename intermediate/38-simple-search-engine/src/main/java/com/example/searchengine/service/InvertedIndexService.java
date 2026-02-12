package com.example.searchengine.service;

import com.example.searchengine.model.Document;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InvertedIndexService {

    // Map: Word -> Set of Document IDs
    private final Map<String, Set<String>> invertedIndex = new ConcurrentHashMap<>();
    // Map: Document ID -> Document content (for retrieval if needed, though index stores IDs)
    private final Map<String, Document> documentStore = new ConcurrentHashMap<>();

    public void indexDocument(Document document) {
        if (document.getId() == null || document.getContent() == null) {
            throw new IllegalArgumentException("Document ID and content must not be null");
        }

        documentStore.put(document.getId(), document);
        
        // Simple tokenization: split by whitespace and remove non-alphanumeric characters
        String[] tokens = document.getContent().toLowerCase().split("\\s+");
        
        for (String token : tokens) {
            // Remove punctuation
            String term = token.replaceAll("[^a-zA-Z0-9]", "");
            if (!term.isEmpty()) {
                invertedIndex.computeIfAbsent(term, k -> ConcurrentHashMap.newKeySet()).add(document.getId());
            }
        }
    }

    public Set<String> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptySet();
        }

        String[] terms = query.toLowerCase().split("\\s+");
        Set<String> resultIds = null;

        for (String term : terms) {
            String cleanTerm = term.replaceAll("[^a-zA-Z0-9]", "");
            if (cleanTerm.isEmpty()) continue;

            Set<String> termDocs = invertedIndex.getOrDefault(cleanTerm, Collections.emptySet());

            if (resultIds == null) {
                resultIds = new HashSet<>(termDocs);
            } else {
                // AND search: intersect with existing results
                resultIds.retainAll(termDocs);
            }
        }

        return resultIds == null ? Collections.emptySet() : resultIds;
    }
    
    public Map<String, Set<String>> getIndex() {
        return invertedIndex;
    }

    public Document getDocument(String id) {
        return documentStore.get(id);
    }
}
