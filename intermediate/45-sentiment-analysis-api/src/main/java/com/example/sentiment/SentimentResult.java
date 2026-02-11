package com.example.sentiment;

public class SentimentResult {
    private int score;
    private String sentiment;
    private double confidence;

    public SentimentResult(int score, String sentiment, double confidence) {
        this.score = score;
        this.sentiment = sentiment;
        this.confidence = confidence;
    }

    public int getScore() {
        return score;
    }

    public String getSentiment() {
        return sentiment;
    }

    public double getConfidence() {
        return confidence;
    }
}
