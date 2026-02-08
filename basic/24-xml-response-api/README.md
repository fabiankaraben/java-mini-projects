# XML Response API

🔹 This is a basic backend in Java, serving a GET endpoint that returns XML data.

## Requirements

- Java 17 or higher
- Gradle (provided via wrapper)

## How to use it

1. **Start the server:**

   ```bash
   ./gradlew run
   ```

2. **Make a request:**

   The server listens on port 8080. You can use `curl` to fetch the XML data:

   ```bash
   curl -v http://localhost:8080/api/user
   ```

   **Expected Output:**

   ```xml
   <user>
     <id>1</id>
     <name>John Doe</name>
     <email>john.doe@example.com</email>
   </user>
   ```

## Testing

To run the unit tests, use the following command:

```bash
./gradlew clean test
```

The tests validate the XML output structure using Jackson XML.
