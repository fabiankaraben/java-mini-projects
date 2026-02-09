package com.example.rediscachelayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisCacheLayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisCacheLayerApplication.class, args);
    }
}
