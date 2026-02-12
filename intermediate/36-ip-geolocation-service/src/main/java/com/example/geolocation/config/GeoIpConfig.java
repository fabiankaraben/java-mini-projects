package com.example.geolocation.config;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class GeoIpConfig {

    @Value("${geoip.database.path:classpath:GeoLite2-City.mmdb}")
    private String databasePath;

    @Bean
    public DatabaseReader databaseReader(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource(databasePath);
        if (resource.exists()) {
            return new DatabaseReader.Builder(resource.getInputStream()).build();
        } else {
            // For development/testing without the DB, we might want to handle this gracefully or fail fast.
            // Failing fast is better so the user knows they need the DB.
            // However, for the purpose of the test, we mock this bean.
            // If the file is missing in production, this will throw an exception.
             throw new IOException("GeoLite2-City.mmdb not found at " + databasePath + ". Please download it and place it in src/main/resources or configure geoip.database.path.");
        }
    }
}
