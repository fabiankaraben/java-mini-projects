package com.example.e2echat.model;

public class UserKey {
    private String username;
    private String publicKey; // Base64 encoded public key

    public UserKey() {}

    public UserKey(String username, String publicKey) {
        this.username = username;
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
