# Basic Authentication Server

This is a basic backend in Java, implementing HTTP Basic Auth for a protected endpoint.

## Requirements

-   Java 21 or higher
-   Gradle (via wrapper)

## Project Structure

-   `src/main/java/com/fabiankaraben/basicauth/BasicAuthServer.java`: Main server class implementing Basic Auth logic.
-   `src/test/java/com/fabiankaraben/basicauth/BasicAuthServerTest.java`: Integration tests.

## How to Run

1.  Clone the repository and navigate to this directory.
2.  Run the server using Gradle:

```bash
./gradlew run
```

The server will start on port `8080`.

## API Usage

The server exposes a protected endpoint at `/protected`.

**Credentials:**
-   Username: `admin`
-   Password: `secret`

### Examples

**1. Valid Request**

```bash
curl -v -u admin:secret http://localhost:8080/protected
```

**Expected Output:**
```
< HTTP/1.1 200 OK
...
Authentication successful! Welcome to the protected area.
```

**2. Invalid Credentials**

```bash
curl -v -u user:wrongpass http://localhost:8080/protected
```

**Expected Output:**
```
< HTTP/1.1 401 Unauthorized
< WWW-Authenticate: Basic realm="Protected Area"
...
Unauthorized
```

**3. Missing Credentials**

```bash
curl -v http://localhost:8080/protected
```

**Expected Output:**
```
< HTTP/1.1 401 Unauthorized
< WWW-Authenticate: Basic realm="Protected Area"
...
Unauthorized
```

## Testing

This project includes integration tests that verify the authentication logic by sending requests with valid, invalid, and missing credentials.

To run the tests, execute:

```bash
./gradlew clean test
```

The test output will show the status of each test event (passed, skipped, failed).
