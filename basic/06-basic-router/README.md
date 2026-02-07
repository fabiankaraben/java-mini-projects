# Basic Router

🔹 This is a basic backend in Java, implementing simple routing logic for GET, POST, and DELETE methods using the standard `com.sun.net.httpserver.HttpServer`.

## Requirements
* Java 21+
* Gradle

## Features
* Custom Router implementation implementing `HttpHandler`.
* Supports GET, POST, and DELETE methods.
* Handles 404 Not Found and 405 Method Not Allowed.

## Directory Structure
```
src/
├── main/
│   └── java/
│       └── com/fabiankaraben/basicrouter/
│           ├── BasicRouterApp.java  # Main entry point
│           └── Router.java          # Routing logic
└── test/
    └── java/
        └── com/fabiankaraben/basicrouter/
            ├── RouterTest.java      # Unit tests
            └── IntegrationTest.java # Integration tests
```

## How to Run

### Run with Gradle
```bash
./gradlew run
```
The server will start on `http://localhost:8080`.

### Run Tests
```bash
./gradlew clean test
```

## Usage Examples

**GET /users**
```bash
curl -v http://localhost:8080/users
```
Response: `List of users`

**POST /users**
```bash
curl -v -X POST http://localhost:8080/users
```
Response: `User created`

**DELETE /users**
```bash
curl -v -X DELETE http://localhost:8080/users
```
Response: `User deleted`

**404 Example**
```bash
curl -v http://localhost:8080/not-found
```
Response: `Not Found` (404)

**405 Example (Method Not Allowed)**
```bash
curl -v -X PUT http://localhost:8080/users
```
Response: `Method Not Allowed` (405)
