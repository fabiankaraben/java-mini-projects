# Modular Backend with JPMS

This is a basic backend in Java utilizing the Java Platform Module System (JPMS) to separate the application into distinct modules with strict encapsulation. It demonstrates how to structure a multi-module Maven project where modules explicitly declare their dependencies and only export what is absolutely necessary.

## Modules Overview

- **`model`**: Contains the domain models. No external dependencies.
- **`service`**: Business logic. Requires `model` and exposes `com.example.jpms.service` interfaces via JPMS `provides`.
- **`web`**: Exposes REST endpoints cleanly utilizing Java's built-in `jdk.httpserver`.
- **`app`**: The application entry point. Starts the WebServer using `ServiceLoader` to dynamically load the `ProductService`.
- **`integration-test`**: Exercises the entire application running via the module path with integration tests.

## Requirements

- Java 17 or higher
- Maven 3.6.3 or higher

## Commands

### Building the Project

Run the following command from the root directory to build all modules:
```bash
mvn clean package
```
*Note: Due to the `maven-dependency-plugin` and `maven-jar-plugin` defined in the `app` module, all runtime modules required to start the application are copied directly to `app/target/modules`.*

### Running the Application

After building the project, you can run the backend exactly as a modular deployment using `java -p` (module-path) like this:

```bash
java --module-path app/target/modules -m com.example.jpms.app/com.example.jpms.app.Application
```

The server should start and listen on HTTP on port 8080.

### Running Tests

To run the unit and integration tests across all modules:

```bash
mvn clean verify
```

The integration tests will transparently start the backend locally and run automated HTTP checks against it via HTTP requests.

## Usage (cURL Examples)

Once the server is up and running on `http://localhost:8080`, test the standard REST endpoints:

**1. Get all products**
```bash
curl -i http://localhost:8080/products
```

**2. Get a single product by ID**
```bash
curl -i http://localhost:8080/products/1
```

**3. Create a new product**
```bash
curl -i -X POST -H "Content-Type: application/json" -d '{"name": "Gaming Keyboard", "price": 150}' http://localhost:8080/products
```

**4. Delete a product**
```bash
curl -i -X DELETE http://localhost:8080/products/1
```
