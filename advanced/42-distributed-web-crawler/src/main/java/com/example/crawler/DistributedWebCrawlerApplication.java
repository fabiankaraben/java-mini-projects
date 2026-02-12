package com.example.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DistributedWebCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedWebCrawlerApplication.class, args);
    }
}
