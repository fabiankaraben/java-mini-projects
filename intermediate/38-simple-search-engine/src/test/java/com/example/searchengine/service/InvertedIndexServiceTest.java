package com.example.searchengine.service;

import com.example.searchengine.model.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InvertedIndexServiceTest {

    private InvertedIndexService service;

    @BeforeEach
    void setUp() {
        service = new InvertedIndexService();
    }

    @Test
    void testIndexingAndSearch() {
        Document doc1 = new Document("1", "Hello world this is a test");
        Document doc2 = new Document("2", "Hello java world");
        Document doc3 = new Document("3", "Testing search engine");

        service.indexDocument(doc1);
        service.indexDocument(doc2);
        service.indexDocument(doc3);

        // Search for "hello" -> should be in 1 and 2
        Set<String> helloResults = service.search("hello");
        assertTrue(helloResults.contains("1"));
        assertTrue(helloResults.contains("2"));
        assertFalse(helloResults.contains("3"));

        // Search for "world" -> should be in 1 and 2
        Set<String> worldResults = service.search("world");
        assertTrue(worldResults.contains("1"));
        assertTrue(worldResults.contains("2"));

        // Search for "test" -> should be in 1 ("test") and 3 ("Testing" -> split/clean might need adjustment if stemming not used)
        // Implementation splits by whitespace and removes non-alphanumeric. 
        // "test" is in doc1.
        // "Testing" in doc3 becomes "testing".
        Set<String> testResults = service.search("test");
        assertTrue(testResults.contains("1"));
        assertFalse(testResults.contains("3")); // exact match on "test"

        // Search for "java" -> 2
        Set<String> javaResults = service.search("java");
        assertTrue(javaResults.contains("2"));
        assertEquals(1, javaResults.size());
    }

    @Test
    void testSearchIntersection() {
        Document doc1 = new Document("1", "apple banana");
        Document doc2 = new Document("2", "banana cherry");
        Document doc3 = new Document("3", "apple cherry");

        service.indexDocument(doc1);
        service.indexDocument(doc2);
        service.indexDocument(doc3);

        // Search "apple banana" -> should be only 1
        Set<String> results = service.search("apple banana");
        assertTrue(results.contains("1"));
        assertFalse(results.contains("2"));
        assertFalse(results.contains("3"));
    }

    @Test
    void testEmptySearch() {
        Set<String> results = service.search("");
        assertTrue(results.isEmpty());
    }
}
