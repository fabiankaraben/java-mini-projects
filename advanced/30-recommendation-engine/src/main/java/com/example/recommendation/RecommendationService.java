package com.example.recommendation;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    // Map<UserId, Map<ItemId, Rating>>
    private final Map<String, Map<String, Double>> userItemRatings = new HashMap<>();

    public void addRating(String userId, String itemId, Double rating) {
        userItemRatings.computeIfAbsent(userId, k -> new HashMap<>()).put(itemId, rating);
    }

    public Map<String, Double> getUserRatings(String userId) {
        return userItemRatings.getOrDefault(userId, Collections.emptyMap());
    }

    /**
     * Recommend items for a user based on user-based collaborative filtering.
     */
    public List<String> recommend(String targetUser, int limit) {
        Map<String, Double> targetRatings = userItemRatings.get(targetUser);
        if (targetRatings == null) {
            return Collections.emptyList();
        }

        Map<String, Double> potentialItems = new HashMap<>();
        Map<String, Double> similaritySums = new HashMap<>();

        for (String otherUser : userItemRatings.keySet()) {
            if (otherUser.equals(targetUser)) continue;

            double similarity = calculateCosineSimilarity(targetRatings, userItemRatings.get(otherUser));

            if (similarity <= 0) continue; // Only consider positive correlations

            for (Map.Entry<String, Double> entry : userItemRatings.get(otherUser).entrySet()) {
                String item = entry.getKey();
                Double rating = entry.getValue();

                // If target user hasn't rated this item
                if (!targetRatings.containsKey(item)) {
                    potentialItems.merge(item, rating * similarity, Double::sum);
                    similaritySums.merge(item, similarity, Double::sum);
                }
            }
        }

        // Normalize by sum of similarities
        Map<String, Double> finalScores = new HashMap<>();
        for (String item : potentialItems.keySet()) {
            if (similaritySums.get(item) > 0) {
                finalScores.put(item, potentialItems.get(item) / similaritySums.get(item));
            }
        }

        return finalScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculateCosineSimilarity(Map<String, Double> ratings1, Map<String, Double> ratings2) {
        Set<String> commonItems = new HashSet<>(ratings1.keySet());
        commonItems.retainAll(ratings2.keySet());

        if (commonItems.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String item : commonItems) {
            dotProduct += ratings1.get(item) * ratings2.get(item);
        }

        for (Double r : ratings1.values()) norm1 += r * r;
        for (Double r : ratings2.values()) norm2 += r * r;

        if (norm1 == 0 || norm2 == 0) return 0.0;

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
