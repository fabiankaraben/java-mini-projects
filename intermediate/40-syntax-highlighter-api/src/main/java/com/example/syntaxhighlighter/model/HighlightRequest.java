package com.example.syntaxhighlighter.model;

public class HighlightRequest {
    private String code;
    private String language; // e.g., "java", "python", "json"

    public HighlightRequest() {
    }

    public HighlightRequest(String code, String language) {
        this.code = code;
        this.language = language;
    }

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
