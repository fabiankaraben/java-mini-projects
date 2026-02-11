package com.example.webscraper.service;

import com.example.webscraper.model.ScrapedData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScraperService {

    public ScrapedData scrape(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        return extractData(doc);
    }

    public ScrapedData extractData(Document doc) {
        ScrapedData data = new ScrapedData();
        data.setTitle(doc.title());

        Map<String, String> metaTags = new HashMap<>();
        for (Element meta : doc.select("meta")) {
            String name = meta.attr("name");
            if (name.isEmpty()) {
                name = meta.attr("property");
            }
            String content = meta.attr("content");
            if (!name.isEmpty() && !content.isEmpty()) {
                metaTags.put(name, content);
            }
        }
        data.setMetaTags(metaTags);

        Map<String, List<String>> headers = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            String tag = "h" + i;
            Elements elements = doc.select(tag);
            if (!elements.isEmpty()) {
                List<String> texts = new ArrayList<>();
                for (Element el : elements) {
                    texts.add(el.text());
                }
                headers.put(tag, texts);
            }
        }
        data.setHeaders(headers);

        return data;
    }
}
