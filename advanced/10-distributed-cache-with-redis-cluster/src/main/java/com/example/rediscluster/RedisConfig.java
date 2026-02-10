package com.example.rediscluster;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    // Spring Boot auto-configuration handles Redis Cluster if spring.data.redis.cluster.nodes is set.
    // We can add custom LettuceClientConfiguration here if needed for advanced topology refresh options.
}
