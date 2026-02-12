package com.example.leaderboard.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LeaderboardService {

    private static final String LEADERBOARD_KEY = "leaderboard";
    private final RedisTemplate<String, String> redisTemplate;

    public LeaderboardService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void submitScore(String userId, double score) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, userId, score);
    }

    public Set<String> getTopN(int n) {
        // Reverse range because higher score is better (descending order)
        // range is 0-indexed, so we want 0 to n-1
        return redisTemplate.opsForZSet().reverseRange(LEADERBOARD_KEY, 0, n - 1);
    }
    
    public Set<ZSetOperations.TypedTuple<String>> getTopNWithScores(int n) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, n - 1);
    }

    public Double getScore(String userId) {
        return redisTemplate.opsForZSet().score(LEADERBOARD_KEY, userId);
    }

    public Long getRank(String userId) {
        // Reverse rank because higher score is better (rank 0 is highest score)
        return redisTemplate.opsForZSet().reverseRank(LEADERBOARD_KEY, userId);
    }
}
