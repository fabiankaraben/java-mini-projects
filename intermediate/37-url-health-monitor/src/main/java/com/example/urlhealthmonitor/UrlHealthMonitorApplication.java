package com.example.urlhealthmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlHealthMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrlHealthMonitorApplication.class, args);
    }
}
