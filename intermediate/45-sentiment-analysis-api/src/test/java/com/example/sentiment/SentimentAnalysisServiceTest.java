package com.example.sentiment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SentimentAnalysisServiceTest {

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Test
    void testPositiveSentiment() {
        String text = "I love this product, it is amazing and fantastic!";
        SentimentResult result = sentimentAnalysisService.analyze(text);
        
        assertEquals("POSITIVE", result.getSentiment());
        assertTrue(result.getScore() > 0);
        assertTrue(result.getConfidence() > 0);
    }

    @Test
    void testNegativeSentiment() {
        String text = "This is terrible and I hate it. The worst experience ever.";
        SentimentResult result = sentimentAnalysisService.analyze(text);
        
        assertEquals("NEGATIVE", result.getSentiment());
        assertTrue(result.getScore() < 0);
        assertTrue(result.getConfidence() > 0);
    }

    @Test
    void testNeutralSentiment() {
        String text = "This is a book. It has pages.";
        SentimentResult result = sentimentAnalysisService.analyze(text);
        
        assertEquals("NEUTRAL", result.getSentiment());
        assertEquals(0, result.getScore());
        assertEquals(0.0, result.getConfidence());
    }

    @Test
    void testMixedSentiment() {
        // "love" (+1), "amazing" (+1), "hate" (-1) -> net +1
        String text = "I love the design, it is amazing, but I hate the color.";
        SentimentResult result = sentimentAnalysisService.analyze(text);
        
        assertEquals("POSITIVE", result.getSentiment());
        assertEquals(1, result.getScore());
    }

    @Test
    void testEmptyInput() {
        SentimentResult result = sentimentAnalysisService.analyze("");
        assertEquals("NEUTRAL", result.getSentiment());
        assertEquals(0, result.getScore());
    }

    @Test
    void testNullInput() {
        SentimentResult result = sentimentAnalysisService.analyze(null);
        assertEquals("NEUTRAL", result.getSentiment());
        assertEquals(0, result.getScore());
    }
}
