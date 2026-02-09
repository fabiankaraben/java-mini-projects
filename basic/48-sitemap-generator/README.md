# Sitemap Generator

This is a simple backend mini-project in Java that generates a simple XML sitemap. It includes a basic HTTP server that serves the generated sitemap at `/sitemap.xml`.

## Requirements

*   Java 17 or higher
*   Gradle

## Project Structure

*   `src/main/java/com/fabiankaraben/sitemap/SitemapGenerator.java`: Logic for generating the XML sitemap.
*   `src/main/java/com/fabiankaraben/sitemap/SitemapApp.java`: Simple HTTP server to serve the sitemap.
*   `src/test/java/com/fabiankaraben/sitemap/SitemapGeneratorTest.java`: Unit tests.

## How to Use

### Running the Server

1.  Compile and run the application using Gradle:
    ```bash
    ./gradlew run
    ```
    (If `./gradlew` is not generated yet, use `gradle run`)

2.  The server will start on port 8080.

### Accessing the Sitemap

You can verify the sitemap generation using `curl`:

```bash
curl -v http://localhost:8080/sitemap.xml
```

Expected output (formatted for readability):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>http://localhost:8080/</loc>
    <lastmod>2023-10-27</lastmod>
    <changefreq>daily</changefreq>
    <priority>0.8</priority>
  </url>
  <url>
    <loc>http://localhost:8080/about</loc>
    <lastmod>2023-10-27</lastmod>
    <changefreq>daily</changefreq>
    <priority>0.8</priority>
  </url>
  ...
</urlset>
```

## Running Tests

To run the unit tests and see the output:

```bash
./gradlew clean test
```

The test logging is configured to show passed, skipped, and failed events.
