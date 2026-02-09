# gRPC with Protobuf

This mini-project demonstrates a Java backend using **gRPC-Spring-Boot-Starter**, defining services with Protobuf.

## Requirements

- Java 17 or higher
- Maven 3.6+

## Project Structure

- `src/main/proto`: Contains the Protobuf service definition (`helloworld.proto`).
- `src/main/java`: Contains the Spring Boot application and gRPC service implementation.
- `src/test/java`: Contains integration tests using an in-process gRPC server.

## How to Run

1. **Build the project**:
   This will generate the Protobuf sources and compile the application.
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   The gRPC server will start (default port is usually 9090).

## How to Test

Run the integration tests using Maven:

```bash
mvn clean test
```

The tests use an in-process gRPC server to verify the service implementation without needing to open a real network port.

## Using the Service

You can use a gRPC client like [grpcurl](https://github.com/fullstorydev/grpcurl) to interact with the running service.

Assuming the server is running on localhost:9090 (default):

1. **List services**:
   ```bash
   grpcurl --plaintext localhost:9090 list
   ```

2. **Call the SayHello method**:
   ```bash
   grpcurl --plaintext -d '{"name": "Developer"}' localhost:9090 com.example.grpc.lib.SimpleService/SayHello
   ```
   
   Expected output:
   ```json
   {
     "message": "Hello, Developer"
   }
   ```
