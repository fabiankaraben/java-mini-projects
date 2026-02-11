package com.example.digitalsignature.model;

public class SignResponse {
    private String signature;
    private String algorithm;

    public SignResponse(String signature, String algorithm) {
        this.signature = signature;
        this.algorithm = algorithm;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
