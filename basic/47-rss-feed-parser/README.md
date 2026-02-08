# RSS Feed Parser

This is a simple backend in Java that parses and serves RSS feeds as JSON. It acts as a proxy/converter, taking an RSS feed URL and returning a simplified JSON representation of the feed items.

## Requirements

- Java 17 or higher
- Maven 3.6+

## Project Structure

- `src/main/java`: Source code
- `src/test/java`: Unit tests
- `pom.xml`: Maven dependency configuration

## Dependencies

- **Rome**: For parsing RSS/Atom feeds
- **Jackson**: For JSON serialization
- **JUnit 5**: For unit testing

## How to Run

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Run the application:**
   ```bash
   java -jar target/rss-feed-parser-1.0-SNAPSHOT.jar
   ```
   The server will start on port 8080.

## Usage

### Parse an RSS Feed

Endpoint: `GET /parse`
Query Parameter: `url` (The URL of the RSS feed to parse)

**Example with curl:**

```bash
curl "http://localhost:8080/parse?url=https://news.ycombinator.com/rss"
```

**Response Format:**

```json
[
  {
    "title": "Item Title",
    "link": "https://example.com/item",
    "publishedDate": "2023-10-27T10:00:00Z",
    "description": "Item description..."
  },
  ...
]
```

## Running Tests

To run the unit tests, use the following command:

```bash
mvn test
```

The tests verify the parsing logic using a sample RSS XML string.
