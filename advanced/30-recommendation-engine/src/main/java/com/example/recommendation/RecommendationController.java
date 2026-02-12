package com.example.recommendation;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/rate")
    public void addRating(@RequestParam String userId, @RequestParam String itemId, @RequestParam Double rating) {
        recommendationService.addRating(userId, itemId, rating);
    }

    @GetMapping("/{userId}")
    public List<String> getRecommendations(@PathVariable String userId, @RequestParam(defaultValue = "5") int limit) {
        return recommendationService.recommend(userId, limit);
    }
    
    @GetMapping("/ratings/{userId}")
    public Map<String, Double> getUserRatings(@PathVariable String userId) {
        return recommendationService.getUserRatings(userId);
    }
}
