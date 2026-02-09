package com.example.configyaml;

import java.util.List;

public class AppConfig {
    private String appName;
    private int port;
    private DatabaseConfig database;
    private List<String> features;

    // Getters and Setters
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseConfig database) {
        this.database = database;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "appName='" + appName + '\'' +
                ", port=" + port +
                ", database=" + database +
                ", features=" + features +
                '}';
    }

    public static class DatabaseConfig {
        private String host;
        private int port;
        private String username;
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "DatabaseConfig{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", username='" + username + '\'' +
                    '}'; // Don't print password
        }
    }
}
