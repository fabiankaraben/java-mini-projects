package com.example.leaderboard.model;

public class ScoreSubmission {
    private String userId;
    private double score;

    public ScoreSubmission() {}

    public ScoreSubmission(String userId, double score) {
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
