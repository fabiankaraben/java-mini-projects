package com.example.configmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigManagementApplication.class, args);
    }

}
