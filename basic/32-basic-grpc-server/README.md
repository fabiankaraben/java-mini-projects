# Basic GRPC Server

This mini-project demonstrates a basic backend in Java using **gRPC-Java**. It defines a service using Protocol Buffers and implements a simple RPC method for greeting.

## 📋 Requirements

- Java 17+
- Gradle 8+ (Wrapper included)
- [grpcurl](https://github.com/fullstorydev/grpcurl) (Recommended for manual testing, analogous to `curl` for gRPC)

## 🚀 How to Run

1. **Start the Server**
   Use the Gradle application plugin to run the server directly:
   ```bash
   ./gradlew run
   ```
   The server will start listening on port `50051`.

2. **Verify it is running**
   You should see logs indicating:
   ```
   Server started, listening on 50051
   ```

## 🛠 How to Use (with grpcurl)

Since gRPC uses Protocol Buffers (binary format) over HTTP/2, standard `curl` is difficult to use without complex setup. Instead, we use `grpcurl`, which is the standard CLI tool for gRPC.

### 1. Install grpcurl
- **macOS**: `brew install grpcurl`
- **Linux/Windows**: See [installation guide](https://github.com/fullstorydev/grpcurl#installation).

### 2. List Available Services
This server has **Server Reflection** enabled, allowing `grpcurl` to discover services automatically.
```bash
grpcurl -plaintext localhost:50051 list
```
Output:
```
grpc.reflection.v1alpha.ServerReflection
helloworld.Greeter
```

### 3. Send a Request
Call the `SayHello` method with a JSON payload:
```bash
grpcurl -plaintext -d '{"name": "Developer"}' localhost:50051 helloworld.Greeter/SayHello
```

**Response:**
```json
{
  "message": "Hello, Developer"
}
```

## 🧪 Testing

This project includes integration tests that spin up an in-process gRPC server and client to verify functionality.

Run the tests with:
```bash
./gradlew clean test
```

You will see detailed output for the test execution:
```
> Task :test

GrpcServerTest > testSayHello() PASSED
```
