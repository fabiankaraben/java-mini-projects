# Sitemap Generator

A simple backend service in Java that generates XML sitemaps compliant with the [Sitemap Protocol](https://www.sitemaps.org/protocol.html).

## What is this?

This mini-project provides a lightweight HTTP server that generates XML sitemaps. It offers two endpoints:
- A default sitemap with predefined URLs
- A custom sitemap endpoint that accepts JSON input to generate dynamic sitemaps

All generated sitemaps are validated against the official Sitemap XML schema to ensure compliance.

## Requirements

- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher

## Project Structure

```
.
├── src/
│   ├── main/java/com/example/sitemap/
│   │   ├── SitemapGenerator.java    # Core sitemap generation logic
│   │   └── SitemapServer.java       # HTTP server with endpoints
│   └── test/java/com/example/sitemap/
│       └── SitemapGeneratorTest.java # Unit tests with schema validation
├── pom.xml                          # Maven build configuration
└── README.md                        # This file
```

## How to Build

```bash
mvn clean package
```

## How to Run

Start the server:

```bash
mvn exec:java
```

The server will start on `http://localhost:8080` with the following endpoints:

### 1. Default Sitemap (GET)

Get a pre-configured sitemap with example URLs:

```bash
curl http://localhost:8080/sitemap.xml
```

**Response:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>https://example.com/</loc>
    <lastmod>2024-01-15</lastmod>
    <changefreq>daily</changefreq>
    <priority>1.0</priority>
  </url>
  <url>
    <loc>https://example.com/about</loc>
    <lastmod>2024-01-15</lastmod>
    <changefreq>monthly</changefreq>
    <priority>0.8</priority>
  </url>
  ...
</urlset>
```

### 2. Custom Sitemap (POST)

Generate a custom sitemap by sending a JSON array of URLs:

```bash
curl -X POST http://localhost:8080/custom-sitemap.xml \
  -H "Content-Type: application/json" \
  -d '[
    {
      "loc": "https://mysite.com/",
      "lastmod": "2024-01-15",
      "changefreq": "daily",
      "priority": "1.0"
    },
    {
      "loc": "https://mysite.com/blog",
      "lastmod": "2024-01-14",
      "changefreq": "weekly",
      "priority": "0.9"
    },
    {
      "loc": "https://mysite.com/contact",
      "changefreq": "yearly",
      "priority": "0.5"
    }
  ]'
```

**JSON Fields:**
- `loc` (required): The URL of the page
- `lastmod` (optional): Last modification date (ISO format: YYYY-MM-DD)
- `changefreq` (optional): How frequently the page changes (always, hourly, daily, weekly, monthly, yearly, never)
- `priority` (optional): Priority of this URL relative to other URLs (0.0 to 1.0)

### 3. Health Check (GET)

Check if the service is running:

```bash
curl http://localhost:8080/health
```

**Response:**
```json
{"status":"healthy","service":"sitemap-generator"}
```

## How to Run Tests

Run all unit tests with detailed output:

```bash
mvn clean test
```

The test suite includes:
- ✅ Basic sitemap generation tests
- ✅ XML escaping validation
- ✅ Schema validation against the official Sitemap XSD
- ✅ Tests for all valid `changefreq` values
- ✅ Tests for priority boundaries (0.0 to 1.0)
- ✅ Tests for optional fields

## Sitemap Schema Validation

All generated sitemaps are validated against the official Sitemap Protocol XML Schema. The tests ensure:
- Valid XML structure
- Proper namespace declaration
- Required `<loc>` elements
- Valid `changefreq` values (always, hourly, daily, weekly, monthly, yearly, never)
- Valid `priority` values (0.0 to 1.0)
- Proper XML character escaping

## Example Use Cases

### Generate a sitemap for a blog

```bash
curl -X POST http://localhost:8080/custom-sitemap.xml \
  -H "Content-Type: application/json" \
  -d '[
    {"loc": "https://myblog.com/", "changefreq": "daily", "priority": "1.0"},
    {"loc": "https://myblog.com/post-1", "changefreq": "monthly", "priority": "0.8"},
    {"loc": "https://myblog.com/post-2", "changefreq": "monthly", "priority": "0.8"},
    {"loc": "https://myblog.com/about", "changefreq": "yearly", "priority": "0.5"}
  ]'
```

### Save sitemap to file

```bash
curl http://localhost:8080/sitemap.xml -o sitemap.xml
```

### Validate sitemap format

The generated XML can be validated using online tools or submitted directly to search engines like Google Search Console.

## Technologies Used

- **Java 17**: Core programming language
- **Maven**: Build and dependency management
- **com.sun.net.httpserver**: Built-in HTTP server (no external web framework needed)
- **JUnit 5**: Testing framework
- **XMLUnit**: XML validation and comparison library
- **SLF4J**: Logging facade

## License

This is an educational project demonstrating sitemap generation in Java.
