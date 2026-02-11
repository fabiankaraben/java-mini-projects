package com.example.leaderboard.controller;

import com.example.leaderboard.model.ScoreSubmission;
import com.example.leaderboard.service.LeaderboardService;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping("/submit")
    public void submitScore(@RequestBody ScoreSubmission submission) {
        leaderboardService.submitScore(submission.getUserId(), submission.getScore());
    }

    @GetMapping("/top/{n}")
    public Set<String> getTopN(@PathVariable int n) {
        return leaderboardService.getTopN(n);
    }

    @GetMapping("/top-with-scores/{n}")
    public List<ScoreSubmission> getTopNWithScores(@PathVariable int n) {
        Set<ZSetOperations.TypedTuple<String>> result = leaderboardService.getTopNWithScores(n);
        if (result == null) {
            return List.of();
        }
        return result.stream()
                .map(tuple -> new ScoreSubmission(tuple.getValue(), tuple.getScore() != null ? tuple.getScore() : 0.0))
                .collect(Collectors.toList());
    }

    @GetMapping("/rank/{userId}")
    public Long getRank(@PathVariable String userId) {
        return leaderboardService.getRank(userId);
    }
    
    @GetMapping("/score/{userId}")
    public Double getScore(@PathVariable String userId) {
        return leaderboardService.getScore(userId);
    }
}
