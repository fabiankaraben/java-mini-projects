package com.fabiankaraben.rss;

import java.util.Date;

public class FeedItem {
    private String title;
    private String link;
    private Date publishedDate;
    private String description;

    public FeedItem() {
    }

    public FeedItem(String title, String link, Date publishedDate, String description) {
        this.title = title;
        this.link = link;
        this.publishedDate = publishedDate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
