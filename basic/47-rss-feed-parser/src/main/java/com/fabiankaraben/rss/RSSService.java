package com.fabiankaraben.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class RSSService {

    public List<FeedItem> parseFeed(String feedUrl) throws Exception {
        URL url = java.net.URI.create(feedUrl).toURL();
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));

        return feed.getEntries().stream()
                .map(this::mapToFeedItem)
                .collect(Collectors.toList());
    }

    public List<FeedItem> parseFeedContent(String xmlContent) throws Exception {
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new StringReader(xmlContent));

        return feed.getEntries().stream()
                .map(this::mapToFeedItem)
                .collect(Collectors.toList());
    }

    private FeedItem mapToFeedItem(SyndEntry entry) {
        return new FeedItem(
                entry.getTitle(),
                entry.getLink(),
                entry.getPublishedDate(),
                entry.getDescription() != null ? entry.getDescription().getValue() : null
        );
    }
}
