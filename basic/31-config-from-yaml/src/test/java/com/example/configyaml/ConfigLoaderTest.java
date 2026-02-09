package com.example.configyaml;

import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    @Test
    void testLoadConfigFromResources() throws IOException {
        AppConfig config = ConfigLoader.loadConfigFromResources("test-config.yaml");

        assertNotNull(config, "Config object should not be null");
        assertEquals("Test Service", config.getAppName());
        assertEquals(9090, config.getPort());
        
        // Database config assertions
        assertNotNull(config.getDatabase(), "Database config should not be null");
        assertEquals("db.test.local", config.getDatabase().getHost());
        assertEquals(3306, config.getDatabase().getPort());
        assertEquals("testuser", config.getDatabase().getUsername());
        
        // Features assertions
        assertNotNull(config.getFeatures(), "Features list should not be null");
        assertEquals(2, config.getFeatures().size());
        assertTrue(config.getFeatures().contains("feature1"));
        assertTrue(config.getFeatures().contains("feature2"));
    }
}
