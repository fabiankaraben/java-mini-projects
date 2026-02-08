package com.fabiankaraben.javaminiprojects.envconfig;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

class AppConfigTest {

    @Test
    void testDefaults() throws Exception {
        // Ensure environment is clean or empty for these checks
        // System Lambda allows us to control the environment
        withEnvironmentVariable("DB_URL", null)
            .and("DB_USER", null)
            .and("DB_PASSWORD", null)
            .and("SERVER_PORT", null)
            .execute(() -> {
                AppConfig config = new AppConfig();
                
                assertEquals("jdbc:mysql://localhost:3306/defaultdb", config.getDbUrl());
                assertEquals("root", config.getDbUser());
                assertEquals("password", config.getDbPassword());
                assertEquals(8080, config.getServerPort());
            });
    }

    @Test
    void testCustomEnvironmentVariables() throws Exception {
        withEnvironmentVariable("DB_URL", "jdbc:postgresql://postgres:5432/mydb")
            .and("DB_USER", "admin")
            .and("DB_PASSWORD", "secret123")
            .and("SERVER_PORT", "9090")
            .execute(() -> {
                AppConfig config = new AppConfig();
                
                assertEquals("jdbc:postgresql://postgres:5432/mydb", config.getDbUrl());
                assertEquals("admin", config.getDbUser());
                assertEquals("secret123", config.getDbPassword());
                assertEquals(9090, config.getServerPort());
            });
    }
    
    @Test
    void testApiKeyRequired() throws Exception {
         withEnvironmentVariable("API_KEY", "my-secret-api-key")
            .execute(() -> {
                AppConfig config = new AppConfig();
                assertEquals("my-secret-api-key", config.getApiKey());
            });
    }
    
    @Test
    void testApiKeyMissingThrowsException() throws Exception {
        // Ensure API_KEY is not set
         withEnvironmentVariable("API_KEY", null)
            .execute(() -> {
                AppConfig config = new AppConfig();
                assertThrows(IllegalStateException.class, config::getApiKey);
            });
    }

    @Test
    void testInvalidPort() throws Exception {
        withEnvironmentVariable("SERVER_PORT", "invalid-port")
            .execute(() -> {
                AppConfig config = new AppConfig();
                // Should fall back to default
                assertEquals(8080, config.getServerPort());
            });
    }
}
