package com.example.leaderboard;

import com.example.leaderboard.model.ScoreSubmission;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class LeaderboardIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }

    @Test
    void testSubmitAndGetTopN() {
        // Submit scores
        submitScore("user1", 100);
        submitScore("user2", 200);
        submitScore("user3", 150);
        submitScore("user4", 50);

        // Get top 3
        ResponseEntity<Set<String>> response = restTemplate.exchange(
                "/api/leaderboard/top/3",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Set<String>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly("user2", "user3", "user1");
    }

    @Test
    void testSubmitAndGetTopNWithScores() {
        // Submit scores
        submitScore("user1", 100);
        submitScore("user2", 200);

        // Get top 2 with scores
        ResponseEntity<List<ScoreSubmission>> response = restTemplate.exchange(
                "/api/leaderboard/top-with-scores/2",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ScoreSubmission>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ScoreSubmission> leaderboard = response.getBody();
        assertThat(leaderboard).hasSize(2);
        assertThat(leaderboard.get(0).getUserId()).isEqualTo("user2");
        assertThat(leaderboard.get(0).getScore()).isEqualTo(200.0);
        assertThat(leaderboard.get(1).getUserId()).isEqualTo("user1");
        assertThat(leaderboard.get(1).getScore()).isEqualTo(100.0);
    }

    @Test
    void testGetRank() {
        submitScore("userA", 10);
        submitScore("userB", 30);
        submitScore("userC", 20);

        // userB should be rank 0 (first)
        ResponseEntity<Long> responseB = restTemplate.getForEntity("/api/leaderboard/rank/userB", Long.class);
        assertThat(responseB.getBody()).isEqualTo(0L);

        // userC should be rank 1 (second)
        ResponseEntity<Long> responseC = restTemplate.getForEntity("/api/leaderboard/rank/userC", Long.class);
        assertThat(responseC.getBody()).isEqualTo(1L);
        
        // userA should be rank 2 (third)
        ResponseEntity<Long> responseA = restTemplate.getForEntity("/api/leaderboard/rank/userA", Long.class);
        assertThat(responseA.getBody()).isEqualTo(2L);
    }
    
    @Test
    void testGetScore() {
        submitScore("userX", 55.5);
        
        ResponseEntity<Double> response = restTemplate.getForEntity("/api/leaderboard/score/userX", Double.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(55.5);
    }

    private void submitScore(String userId, double score) {
        ScoreSubmission submission = new ScoreSubmission(userId, score);
        restTemplate.postForEntity("/api/leaderboard/submit", submission, Void.class);
    }
}
