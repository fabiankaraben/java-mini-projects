package com.fabiankaraben.javaminiprojects.envconfig;


public class AppConfig {

    public String getDbUrl() {
        return getEnv("DB_URL", "jdbc:mysql://localhost:3306/defaultdb");
    }

    public String getDbUser() {
        return getEnv("DB_USER", "root");
    }

    public String getDbPassword() {
        return getEnv("DB_PASSWORD", "password");
    }

    public int getServerPort() {
        String portStr = getEnv("SERVER_PORT", "8080");
        try {
            return Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid SERVER_PORT format, defaulting to 8080");
            return 8080;
        }
    }
    
    public String getApiKey() {
        // Required environment variable, no default
        String apiKey = System.getenv("API_KEY");
        if (apiKey == null) {
             throw new IllegalStateException("API_KEY environment variable is missing");
        }
        return apiKey;
    }

    // Helper method to get env var with default value
    private String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null) ? value : defaultValue;
    }
}
