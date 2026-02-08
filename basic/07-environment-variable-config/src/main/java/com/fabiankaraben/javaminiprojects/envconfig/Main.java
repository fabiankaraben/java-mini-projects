package com.fabiankaraben.javaminiprojects.envconfig;

public class Main {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        
        System.out.println("Loading configuration...");
        System.out.println("------------------------------------------------");
        
        System.out.println("Server Port: " + config.getServerPort());
        System.out.println("DB URL:      " + config.getDbUrl());
        System.out.println("DB User:     " + config.getDbUser());
        
        // Don't print the actual password in production!
        String password = config.getDbPassword();
        System.out.println("DB Password: " + "*".repeat(password.length()));
        
        try {
             System.out.println("API Key:     " + config.getApiKey());
        } catch (IllegalStateException e) {
             System.out.println("API Key:     (Not set - " + e.getMessage() + ")");
        }

        System.out.println("------------------------------------------------");
    }
}
