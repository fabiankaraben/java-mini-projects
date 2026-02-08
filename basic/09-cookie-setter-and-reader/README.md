# Cookie Setter and Reader

This is a simple backend in Java that demonstrates how to handle HTTP cookies. It sets a `user_session` cookie on the first `GET` request and reads it on subsequent requests.

## Requirements

*   Java 17 or higher
*   Maven

## How to Run

1.  Navigate to the project directory:
    ```bash
    cd basic/09-cookie-setter-and-reader
    ```

2.  Compile and run the server using Maven:
    ```bash
    mvn clean compile exec:java
    ```

    Alternatively, you can package the application and run the JAR:
    ```bash
    mvn package
    java -cp target/classes com.example.CookieServer
    ```

3.  The server will start on port `8080`.

## Usage Examples (curl)

**1. First Request (Setting the Cookie)**

Send a GET request to the server. The server will respond with a `Set-Cookie` header.

```bash
curl -i http://localhost:8080/
```

**Expected Output:**
```http
HTTP/1.1 200 OK
Date: ...
Set-Cookie: user_session=...; Path=/; HttpOnly
Content-length: 29

Cookie set! Reload to see it.
```

**2. Subsequent Request (Sending the Cookie)**

Copy the value of the `user_session` cookie from the previous response and send it back in the `Cookie` header.

```bash
curl -i -H "Cookie: user_session=YOUR_UUID_HERE" http://localhost:8080/
```

**Expected Output:**
```http
HTTP/1.1 200 OK
Date: ...
Content-length: ...

Welcome back! Found cookie: YOUR_UUID_HERE
```

## Running Tests

To run the API tests, use the following command:

```bash
mvn clean test
```

This will run the JUnit 5 tests which verify:
*   The `Set-Cookie` header is present in the response for new requests.
*   The `Cookie` header is correctly parsed and read from incoming requests.
