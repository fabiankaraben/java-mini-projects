package com.example.sandbox;

public class CodeExecutionRequest {
    private String code;
    // For simplicity, we default to python, but could be extended
    private String language = "python"; 

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
