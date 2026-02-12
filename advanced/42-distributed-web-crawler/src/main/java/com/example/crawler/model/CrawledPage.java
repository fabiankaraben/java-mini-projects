package com.example.crawler.model;

import java.io.Serializable;
import java.util.Set;

public class CrawledPage implements Serializable {
    private String url;
    private String title;
    private Set<String> links;

    public CrawledPage() {}

    public CrawledPage(String url, String title, Set<String> links) {
        this.url = url;
        this.title = title;
        this.links = links;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getLinks() {
        return links;
    }

    public void setLinks(Set<String> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "CrawledPage{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", links=" + links.size() +
                '}';
    }
}
