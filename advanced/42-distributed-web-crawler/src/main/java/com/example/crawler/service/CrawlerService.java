package com.example.crawler.service;

import com.example.crawler.model.CrawledPage;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.map.IMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CrawlerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
    private final HazelcastInstance hazelcastInstance;
    private final ExecutorService executorService;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private static final String CRAWL_QUEUE = "crawl-queue";
    private static final String VISITED_SET = "visited-set";
    private static final String CRAWLED_PAGES_MAP = "crawled-pages-map";

    public CrawlerService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.executorService = Executors.newFixedThreadPool(10); // Worker pool
    }

    public void startCrawl(String seedUrl, int maxPages) {
        if (isRunning.getAndSet(true)) {
            logger.info("Crawler is already running.");
            return;
        }

        IQueue<String> queue = hazelcastInstance.getQueue(CRAWL_QUEUE);
        ISet<String> visited = hazelcastInstance.getSet(VISITED_SET);
        IMap<String, CrawledPage> crawledPages = hazelcastInstance.getMap(CRAWLED_PAGES_MAP);

        // Reset state for a new crawl (optional, depending on requirements, but good for a mini-project)
        queue.clear();
        visited.clear();
        crawledPages.clear();

        try {
            queue.put(seedUrl);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Failed to add seed URL to queue", e);
        }

        // Start workers
        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> processQueue(maxPages));
        }
    }

    private void processQueue(int maxPages) {
        IQueue<String> queue = hazelcastInstance.getQueue(CRAWL_QUEUE);
        ISet<String> visited = hazelcastInstance.getSet(VISITED_SET);
        IMap<String, CrawledPage> crawledPages = hazelcastInstance.getMap(CRAWLED_PAGES_MAP);

        while (isRunning.get()) {
            if (crawledPages.size() >= maxPages) {
                logger.info("Max pages reached. Stopping crawler.");
                isRunning.set(false);
                break;
            }

            try {
                String url = queue.poll(1, TimeUnit.SECONDS);
                if (url == null) {
                    if (crawledPages.size() > 0 && queue.isEmpty()) {
                         // Wait a bit more to see if other workers add links, else stop
                         // Simplified: just continue for now
                         continue; 
                    }
                    continue;
                }

                if (!visited.add(url)) {
                    continue;
                }

                logger.info("Crawling: {}", url);
                try {
                    Document doc = Jsoup.connect(url).get();
                    String title = doc.title();
                    Elements links = doc.select("a[href]");
                    Set<String> extractedLinks = new HashSet<>();

                    for (org.jsoup.nodes.Element link : links) {
                        String nextUrl = link.attr("abs:href");
                        if (nextUrl != null && !nextUrl.isEmpty() && nextUrl.startsWith("http")) {
                            extractedLinks.add(nextUrl);
                            if (!visited.contains(nextUrl)) {
                                queue.put(nextUrl);
                            }
                        }
                    }

                    CrawledPage page = new CrawledPage(url, title, extractedLinks);
                    crawledPages.put(url, page);

                } catch (IOException e) {
                    logger.error("Error crawling {}: {}", url, e.getMessage());
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public IMap<String, CrawledPage> getCrawledPages() {
        return hazelcastInstance.getMap(CRAWLED_PAGES_MAP);
    }
}
