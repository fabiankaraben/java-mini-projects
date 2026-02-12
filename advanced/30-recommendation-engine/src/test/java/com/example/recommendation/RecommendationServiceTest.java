package com.example.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationServiceTest {

    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationService();
    }

    @Test
    void testCollaborativeFilteringRecommendations() {
        // Dataset
        // User A: Item1 (5.0), Item2 (3.0)
        // User B: Item1 (5.0), Item2 (3.0), Item3 (5.0) - Very similar to A
        // User C: Item1 (2.0), Item4 (4.0) - Dissimilar to A
        
        // Target: User A
        // Expected Recommendation: Item3 (strongly recommended by B)

        recommendationService.addRating("UserA", "Item1", 5.0);
        recommendationService.addRating("UserA", "Item2", 3.0);

        recommendationService.addRating("UserB", "Item1", 5.0);
        recommendationService.addRating("UserB", "Item2", 3.0);
        recommendationService.addRating("UserB", "Item3", 5.0);

        recommendationService.addRating("UserC", "Item1", 2.0);
        recommendationService.addRating("UserC", "Item4", 4.0);

        List<String> recommendations = recommendationService.recommend("UserA", 3);

        System.out.println("Recommendations for UserA: " + recommendations);

        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty(), "Recommendations should not be empty");
        assertEquals("Item3", recommendations.get(0), "Item3 should be the top recommendation");
        
        // Item4 is from UserC. UserC gave Item1 a 2.0 while UserA gave it 5.0. 
        // Their similarity might be lower or negative depending on implementation details (if centered).
        // With simple cosine on raw ratings:
        // A = [5, 3, 0, 0]
        // C = [2, 0, 0, 4]
        // dot = 10. norms = sqrt(34), sqrt(20). sim = 10 / (5.83 * 4.47) = 10 / 26.06 = ~0.38
        // A and B:
        // A = [5, 3, 0]
        // B = [5, 3, 5]
        // dot = 25 + 9 = 34. norms = sqrt(34), sqrt(59) = 7.68. sim = 34 / (5.83 * 7.68) = 34 / 44.77 = ~0.76
        // So B is more similar. Item3 score comes from B. Item4 score comes from C.
        // Item3 score ~ 5.0 * 0.76 = 3.8
        // Item4 score ~ 4.0 * 0.38 = 1.52
        // So Item3 > Item4.
        
        assertTrue(recommendations.contains("Item3"));
    }

    @Test
    void testNoRecommendationsForNewUserWithoutSimilarity() {
        recommendationService.addRating("UserA", "Item1", 5.0);
        recommendationService.addRating("UserB", "Item2", 5.0); // No common items

        List<String> recommendations = recommendationService.recommend("UserA", 5);
        
        assertTrue(recommendations.isEmpty(), "Should not recommend anything if no similarity found");
    }
}
