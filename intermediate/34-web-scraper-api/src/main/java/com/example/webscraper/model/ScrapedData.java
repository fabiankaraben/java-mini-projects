package com.example.webscraper.model;

import java.util.List;
import java.util.Map;

public class ScrapedData {
    private String title;
    private Map<String, String> metaTags;
    private Map<String, List<String>> headers;

    public ScrapedData() {
    }

    public ScrapedData(String title, Map<String, String> metaTags, Map<String, List<String>> headers) {
        this.title = title;
        this.metaTags = metaTags;
        this.headers = headers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getMetaTags() {
        return metaTags;
    }

    public void setMetaTags(Map<String, String> metaTags) {
        this.metaTags = metaTags;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
}
