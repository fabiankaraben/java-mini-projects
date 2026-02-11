package com.example.twofactorauth.model;

public class TotpValidationRequest {
    private String secret;
    private int code;

    public TotpValidationRequest() {
    }

    public TotpValidationRequest(String secret, int code) {
        this.secret = secret;
        this.code = code;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
