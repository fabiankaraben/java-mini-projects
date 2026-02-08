# In-Memory Data Store

This mini-project demonstrates a simple backend in Java using a `HashMap` as an in-memory data store for CRUD (Create, Read, Update, Delete) operations. It uses the `com.sun.net.httpserver.HttpServer` for handling HTTP requests and Gson for JSON processing.

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## How to Run

1. **Build the project:**
   ```bash
   mvn clean package
   ```

2. **Run the server:**
   ```bash
   # Using Maven
   mvn exec:java -Dexec.mainClass="com.fabiankaraben.inmemorydatastore.Main"
   
   # Or using the built jar (after package)
   java -cp target/in-memory-data-store-1.0-SNAPSHOT.jar:target/dependency/* com.fabiankaraben.inmemorydatastore.Main
   ```
   The server will start on port `8080`.

## API Usage

### Create an Item
```bash
curl -X POST http://localhost:8080/items \
     -H "Content-Type: application/json" \
     -d '{"id": "1", "content": "Hello World"}'
```

### Get All Items
```bash
curl -v http://localhost:8080/items
```

### Get an Item by ID
```bash
curl -v http://localhost:8080/items/1
```

### Update an Item
```bash
curl -X PUT http://localhost:8080/items/1 \
     -H "Content-Type: application/json" \
     -d '{"id": "1", "content": "Updated Content"}'
```

### Delete an Item
```bash
curl -X DELETE http://localhost:8080/items/1
```

## Running Tests

This project includes both unit tests for the data store logic and integration tests for the HTTP API.

To run the tests, execute:

```bash
mvn clean test
```

You should see output indicating the status of the tests, for example:
```
[INFO] Running com.fabiankaraben.inmemorydatastore.DataStoreTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 s
[INFO] Running com.fabiankaraben.inmemorydatastore.StoreIntegrationTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.198 s
```
