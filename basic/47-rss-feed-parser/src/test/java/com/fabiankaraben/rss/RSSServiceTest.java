package com.fabiankaraben.rss;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RSSServiceTest {

    @Test
    public void testParseFeedContent() throws Exception {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<rss version=\"2.0\">\n" +
                "<channel>\n" +
                " <title>RSS Title</title>\n" +
                " <description>This is an example of an RSS feed</description>\n" +
                " <link>http://www.example.com/main.html</link>\n" +
                " <item>\n" +
                "  <title>Test Item 1</title>\n" +
                "  <description>Description for item 1</description>\n" +
                "  <link>http://www.example.com/blog/post/1</link>\n" +
                "  <pubDate>Mon, 06 Sep 2010 00:01:00 +0000</pubDate>\n" +
                " </item>\n" +
                " <item>\n" +
                "  <title>Test Item 2</title>\n" +
                "  <description>Description for item 2</description>\n" +
                "  <link>http://www.example.com/blog/post/2</link>\n" +
                "  <pubDate>Mon, 06 Sep 2010 00:02:00 +0000</pubDate>\n" +
                " </item>\n" +
                "</channel>\n" +
                "</rss>";

        RSSService service = new RSSService();
        List<FeedItem> items = service.parseFeedContent(xmlContent);

        assertNotNull(items);
        assertEquals(2, items.size());

        FeedItem item1 = items.get(0);
        assertEquals("Test Item 1", item1.getTitle());
        assertEquals("http://www.example.com/blog/post/1", item1.getLink());
        assertEquals("Description for item 1", item1.getDescription());

        FeedItem item2 = items.get(1);
        assertEquals("Test Item 2", item2.getTitle());
    }
}
