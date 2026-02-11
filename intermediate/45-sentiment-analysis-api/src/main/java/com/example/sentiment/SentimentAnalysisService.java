package com.example.sentiment;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class SentimentAnalysisService {

    private Set<String> positiveWords;
    private Set<String> negativeWords;

    @PostConstruct
    public void init() {
        // Initialize with a basic list of English words
        positiveWords = new HashSet<>(Arrays.asList(
            "good", "great", "excellent", "amazing", "wonderful", "fantastic", "happy", "joy", "love", "like",
            "best", "better", "awesome", "beautiful", "nice", "pleasant", "superb", "brilliant", "exciting", "fun"
        ));

        negativeWords = new HashSet<>(Arrays.asList(
            "bad", "terrible", "awful", "horrible", "worst", "worse", "sad", "unhappy", "hate", "dislike",
            "poor", "disappointing", "boring", "annoying", "painful", "ugly", "nasty", "dreadful", "miserable", "fail"
        ));
    }

    public SentimentResult analyze(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new SentimentResult(0, "NEUTRAL", 0.0);
        }

        int score = 0;
        int wordCount = 0;
        
        // Simple tokenization by splitting on non-alphanumeric characters
        String[] words = text.toLowerCase().split("[^a-zA-Z]+");

        for (String word : words) {
            if (word.isEmpty()) continue;
            wordCount++;
            if (positiveWords.contains(word)) {
                score++;
            } else if (negativeWords.contains(word)) {
                score--;
            }
        }

        String sentiment;
        if (score > 0) {
            sentiment = "POSITIVE";
        } else if (score < 0) {
            sentiment = "NEGATIVE";
        } else {
            sentiment = "NEUTRAL";
        }

        // Calculate a normalized score between -1.0 and 1.0 roughly
        // We can't strictly bound it without more complex logic, but ratio of score/wordCount gives intensity
        double confidence = wordCount > 0 ? (double) Math.abs(score) / wordCount : 0.0;
        // Cap confidence at 1.0
        confidence = Math.min(confidence, 1.0);

        return new SentimentResult(score, sentiment, confidence);
    }
}
