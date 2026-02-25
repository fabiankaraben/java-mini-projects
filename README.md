# java-mini-projects
Java mini-projects, each one a new challenge.

<p align="center"><img src="https://fabiankaraben.github.io/mini-projects/imgs/java.webp" alt="Featured Image"></p>

## Setup instructions
1. Install Java 21 or higher (LTS)
2. Install Docker and Docker Compose
3. Run `./mvnw clean install` or `./gradlew build` in each project directory depending on the project's dependency manager.

## Basic
1. **Hello World HTTP Server**  
   🔹 This is a simple backend in Java using `com.sun.net.httpserver`, serving a single GET endpoint that returns a "Hello World" message, with basic error handling and logging.  
   📦 **Dependency Manager**: Maven  
   🧪 **Testing**: Unit tests using **JUnit 5**. Integration tests using `java.net.http.HttpClient` to verify the endpoint response.  
   🔹 [Project directory](basic/01a-hello-world-jetty-http-server)
   🔹 [Project directory](basic/01b-hello-world-netty-http-server)
   🔹 [Project directory](basic/01c-hello-world-tomcat-http-server)

2. **Static File Server**  
   🔹 This is a basic backend in Java using `com.sun.net.httpserver`, serving static HTML, CSS, and JS files from a directory.  
   📦 **Dependency Manager**: Gradle  
   🧪 **Testing**: Integration tests using **JUnit 5** and `HttpClient` to request static files and verify content types.  
   🔹 [Project directory](basic/02-static-file-server)

3. **JSON Response API**  
   🔹 This is a simple backend in Java, providing a GET endpoint that returns a JSON object (manually constructed or using a library like Jackson/Gson).  
   📦 **Dependency Manager**: Maven  
   🧪 **Testing**: Unit tests for JSON serialization using **Jackson** or **Gson**. API tests using **JUnit 5** to validate JSON structure.  
   🔹 [Project directory](basic/03-json-response-api)

4. **Form Data Handler**  
   🔹 This is a basic backend in Java, handling POST requests with form data, parsing it, and echoing back the submitted values.  
   📦 **Dependency Manager**: Gradle  
   🧪 **Testing**: Integration tests sending `application/x-www-form-urlencoded` requests and asserting the echoed response.  
   🔹 [Project directory](basic/04-form-data-handler)

5. **Query Parameter Parser**  
   🔹 This is a simple backend in Java, parsing query parameters from a GET request and returning them in a JSON response.  
   📦 **Dependency Manager**: Maven  
   🧪 **Testing**: Unit tests for the parser logic. API tests verifying different query parameter combinations.  
   🔹 [Project directory](basic/05-query-parameter-parser)

6. **Basic Router**  
   🔹 This is a basic backend in Java, implementing simple routing logic for GET, POST, and DELETE methods.  
   📦 **Dependency Manager**: Gradle  
   🧪 **Testing**: Unit tests for the routing mechanism. Integration tests verifying 404s for undefined routes and correct handlers for defined ones.  
   🔹 [Project directory](basic/06-basic-router)

7. **Environment Variable Config**  
   🔹 This is a simple backend in Java using `System.getenv()`, reading environment variables for configuration.  
   📦 **Dependency Manager**: Maven  
   🧪 **Testing**: Unit tests extracting configuration logic, possibly using a library like **System Lambda** to mock environment variables.  
   🔹 [Project directory](basic/07-environment-variable-config)

8. **File Upload Server**  
   🔹 This is a basic backend in Java, handling file uploads via POST and saving them to disk.  
   📦 **Dependency Manager**: Gradle  
   🧪 **Testing**: Integration tests using `multipart/form-data` requests. Verify file existence and content on disk after upload.  
   🔹 [Project directory](basic/08-file-upload-server)

9. **Cookie Setter and Reader**  
   🔹 This is a simple backend in Java, setting a cookie on a GET request and reading it on subsequent requests.  
   📦 **Dependency Manager**: Maven  
   🧪 **Testing**: API tests checking `Set-Cookie` headers in responses and sending `Cookie` headers in requests.  
   🔹 [Project directory](basic/09-cookie-setter-and-reader)

10. **Basic Error Handling**  
    🔹 This is a basic backend in Java, implementing global exception handling to return proper error responses.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests triggering exceptions to verify correct HTTP status codes (e.g., 500, 400) and error message formats.  
    🔹 [Project directory](basic/10-basic-error-handling)

11. **Simple Logger Middleware**  
    🔹 This is a simple backend in Java using `java.util.logging`, logging request method, path, and duration.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests with a custom `Handler` to capture and verify log messages.  
    🔹 [Project directory](basic/11-simple-logger-middleware)

12. **JSON Request Parser**  
    🔹 This is a basic backend in Java, parsing JSON from POST body and validating basic fields.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests verifying correct parsing of valid JSON and error handling for malformed JSON using **JUnit 5**.  
    🔹 [Project directory](basic/12-json-request-parser)

13. **Redirect Handler**  
    🔹 This is a simple backend in Java, handling a GET request that redirects to another URL.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests checking for 301/302 status codes and the `Location` header.  
    🔹 [Project directory](basic/13-redirect-handler)

14. **Basic Template Rendering**  
    🔹 This is a basic backend in Java, rendering a simple HTML template with dynamic data.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests verifying the string output matches the expected HTML structure with injected data.  
    🔹 [Project directory](basic/14-basic-template-rendering)

15. **In-Memory Data Store**  
    🔹 This is a simple backend in Java using a `HashMap` as an in-memory store for CRUD operations.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests for the data store class. Integration tests verifying state persistence across multiple HTTP calls.  
    🔹 [Project directory](basic/15-in-memory-data-store)

16. **Rate Limiter with Time**  
    🔹 This is a basic backend in Java, implementing a simple rate limiter.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Concurrency tests sending requests in rapid succession to verify 429 Too Many Requests responses.  
    🔹 [Project directory](basic/16-rate-limiter-with-time)

17. **CORS Handler**  
    🔹 This is a simple backend in Java, adding CORS headers to allow cross-origin requests.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests sending `OPTIONS` requests and checking `Access-Control-Allow-Origin` headers.  
    🔹 [Project directory](basic/17-cors-handler)

18. **Basic Authentication**  
    🔹 This is a basic backend in Java, implementing HTTP Basic Auth for a protected endpoint.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests sending valid/invalid `Authorization` headers and checking for 200 vs 401 responses.  
    🔹 [Project directory](basic/18-basic-authentication)

19. **Gzip Compression**  
    🔹 This is a simple backend in Java using `GZIPOutputStream`, compressing responses for text-based content.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests sending `Accept-Encoding: gzip` and verifying the response `Content-Encoding` and body decompressibility.  
    🔹 [Project directory](basic/19-gzip-compression)

20. **Header Manipulation**  
    🔹 This is a simple backend in Java, reading custom headers from requests and setting them in responses.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: API tests sending custom headers and asserting their presence/modification in the response.  
    🔹 [Project directory](basic/20-header-manipulation)

21. **Signal Handling for Shutdown**  
    🔹 This is a simple backend in Java using Shutdown Hooks, gracefully shutting down the server.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Hard to automate fully; manual verification or shell script tests to send `SIGINT` and check logs.  
    🔹 [Project directory](basic/21-signal-handling-for-shutdown)

22. **Multipart Form Handler**  
    🔹 This is a basic backend in Java, parsing multipart forms with files and fields.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests constructing multipart requests with text fields and file attachments.  
    🔹 [Project directory](basic/22-multipart-form-handler)

23. **Simple WebSocket Echo**  
    🔹 This is a simple backend in Java using the WebSocket API, echoing messages back to the client.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using a Java WebSocket Client (like `Tyrus` or `Java-WebSocket`) to connect and verify echoed messages.  
    🔹 [Project directory](basic/23-simple-websocket-echo)

24. **XML Response API**  
    🔹 This is a basic backend in Java, serving a GET endpoint that returns XML data.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests validating XML output structure using **Jackson XML** or regular expressions.  
    🔹 [Project directory](basic/24-xml-response-api)

25. **Timeout Handler**  
    🔹 This is a simple backend in Java, setting request timeouts to prevent long-running handlers.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Tests using a client with a higher timeout, hitting an endpoint that sleeps longer than the server timeout to verify 503/error response.  
    🔹 [Project directory](basic/25-timeout-handler)

26. **Basic Cache with Map**  
    🔹 This is a basic backend in Java, implementing an in-memory cache for expensive computations.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests verifying cache hits vs misses (checking computation counts or timing).  
    🔹 [Project directory](basic/26-basic-cache-with-map)

27. **URL Shortener**  
    🔹 This is a simple backend in Java, shortening URLs and redirecting with a simple map store.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration flow: Create short URL -> Verify ID -> Access short URL -> Verify Redirect to original.  
    🔹 [Project directory](basic/27-url-shortener)

28. **Health Check Endpoint**  
    🔹 This is a basic backend in Java, providing a /health endpoint that returns 200 OK.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Simple integration test verifying 200 OK status and JSON/text status message.  
    🔹 [Project directory](basic/28-health-check-endpoint)

29. **Prometheus Metrics**  
    🔹 This is a simple backend in Java using Micrometer, exposing basic request metrics.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration test hitting endpoints and then verifying the `/metrics` endpoint contains expected metric keys.  
    🔹 [Project directory](basic/29-prometheus-metrics)

30. **Dockerized HTTP Server**  
    🔹 This is a basic backend in Java, packaged in a Docker image for easy deployment.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Use **Testcontainers** to build and run the Docker image, then verify the endpoint responds correctly.  
    🔹 [Project directory](basic/30-dockerized-http-server)

31. **Config from YAML**  
    🔹 This is a simple backend in Java, loading configuration from a YAML file (e.g. using SnakeYAML or Jackson).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests loading a sample YAML file and asserting the configuration object properties.  
    🔹 [Project directory](basic/31-config-from-yaml)

32. **Basic GRPC Server**  
    🔹 This is a basic backend in Java using gRPC-Java, defining a single RPC method for greeting.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using a **gRPC Client** stub to call the server method and verify the response.  
    🔹 [Project directory](basic/32-basic-grpc-server)

33. **File Download Server**  
    🔹 This is a simple backend in Java, serving files for download with proper headers.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests verifying `Content-Disposition` headers and comparing downloaded byte array with original file.  
    🔹 [Project directory](basic/33-file-download-server)

34. **Request Validator**  
    🔹 This is a basic backend in Java, validating request bodies (e.g. using Bean Validation).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests passing valid/invalid objects to the validator and checking for `ConstraintViolationException`.  
    🔹 [Project directory](basic/34-request-validator)

35. **Panic/Exception Recovery**  
    🔹 This is a simple backend in Java, recovering from unchecked exceptions in handlers.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests hitting a "poison" endpoint that throws RuntimeException and verifying the server keeps running and returns 500.  
    🔹 [Project directory](basic/35-exception-recovery)

36. **Simple Queue with BlockingQueue**  
    🔹 This is a basic backend in Java using `BlockingQueue`, processing tasks in a FIFO queue via HTTP triggers.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests adding items and verifying they are processed (e.g., via logs or side effects) in order.  
    🔹 [Project directory](basic/36-simple-queue)

37. **Email Sender**  
    🔹 This is a simple backend in Java using JavaMail, sending plain text emails via an SMTP server.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Use **GreenMail** (Testcontainers or embedded) to mock an SMTP server and verify email receipt.  
    🔹 [Project directory](basic/37-email-sender)

38. **CSV Parser API**  
    🔹 This is a basic backend in Java, parsing uploaded CSV files and returning JSON.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests with sample CSV strings. Integration tests uploading CSV files and checking JSON response.  
    🔹 [Project directory](basic/38-csv-parser-api)

39. **Image Resizer**  
    🔹 This is a simple backend in Java using `java.awt`, resizing uploaded images on the fly.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests uploading an image and verifying the returned image dimensions using `ImageIO`.  
    🔹 [Project directory](basic/39-image-resizer)

40. **Basic OAuth Client**  
    🔹 This is a basic backend in Java, authenticating with a provider like Google.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Use **WireMock** to simulate the OAuth provider's token endpoint and verify the authentication flow.  
    🔹 [Project directory](basic/40-basic-oauth-client)

41. **JWT Token Generator**  
    🔹 This is a simple backend in Java using JJWT or similar, generating and signing JWT tokens.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests generating a token and immediately validating/decoding it to check claims.  
    🔹 [Project directory](basic/41-jwt-token-generator)

42. **Password Hasher**  
    🔹 This is a basic backend in Java using BCrypt, hashing and verifying passwords.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests verifying that the same password matches its hash and different passwords do not.  
    🔹 [Project directory](basic/42-password-hasher)

43. **UUID Generator API**  
    🔹 This is a simple backend in Java using `java.util.UUID`, generating unique IDs on request.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests calling the API multiple times to ensure uniqueness and correct UUID format.  
    🔹 [Project directory](basic/43-uuid-generator-api)

44. **Time Zone Converter**  
    🔹 This is a basic backend in Java using `java.time`, converting times between zones via API.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests for specific conversion cases (e.g., UTC to EST) including daylight savings edge cases.  
    🔹 [Project directory](basic/44-time-zone-converter)

45. **Currency Converter**  
    🔹 This is a simple backend in Java using `java.net.http.HttpClient`, fetching rates from a free API.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Use **WireMock** to stub the external currency API response and verify the internal logic.  
    🔹 [Project directory](basic/45-currency-converter)

46. **Weather Fetcher**  
    🔹 This is a simple backend in Java using `HttpClient`, proxying weather data from a public API.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Use **WireMock** to simulate the weather API and verify the proxy response format.  
    🔹 [Project directory](basic/46-weather-fetcher)

47. **RSS Feed Parser**  
    🔹 This is a simple backend in Java, parsing and serving RSS feeds.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests with a sample RSS XML string to verify parsing logic.  
    🔹 [Project directory](basic/47-rss-feed-parser)

48. **Sitemap Generator**  
    🔹 This is a simple backend in Java, generating a simple XML sitemap.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests verifying the generated XML against the Sitemap schema.  
    🔹 [Project directory](basic/48-sitemap-generator)

49. **Captcha Generator**  
    🔹 This is a simple backend in Java, generating image captchas.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests verifying the endpoint returns an image content type and storing the text in session/map for validation.  
    🔹 [Project directory](basic/49-captcha-generator)

50. **Basic Cron Job**  
    🔹 This is a basic backend in Java using `ScheduledExecutorService`, scheduling a simple task.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests using `CountDownLatch` to verify the task runs multiple times within a short period.  
    🔹 [Project directory](basic/50-basic-cron-job)

## Intermediate
1. **REST API with SQLite**  
    🔹 This is a backend in Java using **Spring Boot**, using SQLite for data storage, REST APIs with CRUD on users.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Spring Boot Test** and `MockMvc`. Use a temporary file or in-memory SQLite for isolation.  
    🔹 [Project directory](intermediate/01-rest-api-with-sqlite)

2. **JWT Authentication Service**  
    🔹 This is a backend in Java using **Spring Boot**, implementing login/register/refresh endpoints with JWT security.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: **Spring Security Test** to test protected endpoints. Unit tests for JWT utility class (generating/parsing).  
    🔹 [Project directory](intermediate/02-jwt-authentication-service)

3. **PostgreSQL CRUD API**  
    🔹 This is a backend in Java using **Spring Boot**, using PostgreSQL for persistent data and Spring Data JPA.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (PostgreSQL) to run against a real database instance.  
    🔹 [Project directory](intermediate/03-postgresql-crud-api)

4. **Redis Cache Layer**  
    🔹 This is a backend in Java using **Spring Boot**, using Redis for caching responses.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Redis). Verify cache hits/misses by checking execution time or mocked service calls.  
    🔹 [Project directory](intermediate/04-redis-cache-layer)

5. **GraphQL Server**  
    🔹 This is a backend in Java using **Spring for GraphQL**, using in-memory data and a GraphQL schema.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Tests using `GraphQlTester` to execute queries/mutations and verify the response data.  
    🔹 [Project directory](intermediate/05-graphql-server)

6. **MongoDB Document Store**  
    🔹 This is a backend in Java using **Spring Boot** and **MongoDB**, for NoSQL data persistence.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (MongoDB) to verify document insertion and retrieval.  
    🔹 [Project directory](intermediate/06-mongodb-document-store)

7. **Rate Limiting Service**  
    🔹 This is a backend in Java using **Spring Boot**, using Redis for token bucket rate limiting.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (Redis). Simulate high concurrency to verify rate limits are enforced.  
    🔹 [Project directory](intermediate/07-rate-limiting-service)

8. **OAuth2 Authorization Server**  
    🔹 This is a backend in Java using **Spring Authorization Server**, issuing tokens with client credentials flow.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **MockMvc** to hit the `/oauth2/token` endpoint and verify JWT issuance.  
    🔹 [Project directory](intermediate/08-oauth2-authorization-server)

9. **WebSocket Chat Server**  
    🔹 This is a backend in Java using **Spring WebSocket**, using Redis for pub/sub messaging.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using `WebSocketStompClient` to connect, subscribe, and verify message broadcast. Use **Testcontainers** for Redis.  
    🔹 [Project directory](intermediate/09-websocket-chat-server)

10. **File Storage with S3**  
    🔹 This is a backend in Java using AWS SDK, using MinIO (S3-compatible) for storage.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (MinIO). Verify upload/download functionality against the mock S3 bucket.  
    🔹 [Project directory](intermediate/10-file-storage-with-s3)

11. **Email Notification Service**  
    🔹 This is a backend in Java using **Spring Boot**, using RabbitMQ for queuing email tasks.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (RabbitMQ) for the queue and **GreenMail** for the SMTP server.  
    🔹 [Project directory](intermediate/11-email-notification-service)

12. **Logging with SLF4J/Logback**  
    🔹 This is a backend in Java using **Spring Boot**, sending structured logs to Elasticsearch/ELK.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Verify logs using a custom Appender in tests, or use **Testcontainers** (Elasticsearch) and query for the logs.  
    🔹 [Project directory](intermediate/12-logging-with-elk)

13. **Metrics with Prometheus**  
    🔹 This is a backend in Java using **Spring Boot** and **Micrometer**, exposing custom metrics.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests hitting endpoints and checking the `/actuator/prometheus` endpoint for specific custom metric values.  
    🔹 [Project directory](intermediate/13-metrics-with-prometheus)

14. **Tracing with Jaeger/OpenTelemetry**  
    🔹 This is a backend in Java using **Spring Boot** and **OpenTelemetry**, using Jaeger for traces.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Jaeger) to verify traces are sent, or check for `trace-id` headers in responses.  
    🔹 [Project directory](intermediate/14-tracing-with-jaeger)

15. **Circuit Breaker Pattern**  
    🔹 This is a backend in Java using **Spring Boot** and **Resilience4j**, handling external API calls with fallbacks.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit/Integration tests using **WireMock** to simulate failure and verify the fallback method is invoked.  
    🔹 [Project directory](intermediate/15-circuit-breaker-pattern)

16. **Message Queue with RabbitMQ**  
    🔹 This is a backend in Java using **Spring AMQP**, using RabbitMQ for pub/sub and worker queues.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (RabbitMQ) to publish messages and verify consumption.  
    🔹 [Project directory](intermediate/16-message-queue-with-rabbitmq)

17. **Kafka Producer/Consumer**  
    🔹 This is a backend in Java using **Spring for Apache Kafka**, using Kafka for streaming.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (Kafka) or `EmbeddedKafka` to verify message production and consumption.  
    🔹 [Project directory](intermediate/17-kafka-producer-consumer)

18. **gRPC with Protobuf**  
    🔹 This is a backend in Java using **gRPC-Spring-Boot-Starter**, defining services with Protobuf.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using `GrpcCleanupRule` or Spring Boot gRPC test support to make RPC calls to the service.  
    🔹 [Project directory](intermediate/18-grpc-with-protobuf)

19. **API Gateway**  
    🔹 This is a backend in Java using **Spring Cloud Gateway**, acting as an API gateway with rate limiting.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **WireMock** as backend services. Verify requests are routed correctly and filters are applied.  
    🔹 [Project directory](intermediate/19-api-gateway)

20. **Search with Elasticsearch**  
    🔹 This is a backend in Java using **Spring Data Elasticsearch**, for indexing and searching.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Elasticsearch). Index documents and run search queries to verify results.  
    🔹 [Project directory](intermediate/20-search-with-elasticsearch)

21. **Caching with Memcached**  
    🔹 This is a backend in Java, using Memcached for distributed cache.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (Memcached). Verify data persistence and expiration in the cache.  
    🔹 [Project directory](intermediate/21-caching-with-memcached)

22. **Session Management**  
    🔹 This is a backend in Java using **Spring Session**, using Redis for session storage.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Redis). Verify session data persists across requests even if the app restarts.  
    🔹 [Project directory](intermediate/22-session-management)

23. **Payment Gateway Integration**  
    🔹 This is a backend in Java using Stripe Java SDK, handling payments and webhooks.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Use **Stripe-mock** (via Testcontainers) or **WireMock** to simulate Stripe API responses and webhook events.  
    🔹 [Project directory](intermediate/23-payment-gateway-integration)

24. **Image Processing**  
    🔹 This is a backend in Java, processing uploads with filters (e.g. using ImageJ or OpenCV).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests uploading images and asserting properties (color histogram, dimensions) of the processed output.  
    🔹 [Project directory](intermediate/24-image-processing)

25. **Video Thumbnail Generator**  
    🔹 This is a backend in Java using FFmpeg wrapper, extracting thumbnails from videos.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests with sample video files. Verify a thumbnail image is created and has content.  
    🔹 [Project directory](intermediate/25-video-thumbnail-generator)

26. **PDF Generator**  
    🔹 This is a backend in Java (using iText or PDFBox), generating PDFs from templates.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests generating PDFs and using **PDFBox** to parse the generated file and verify text content.  
    🔹 [Project directory](intermediate/26-pdf-generator)

27. **CSV Export Service**  
    🔹 This is a backend in Java, exporting data from DB to CSV for download.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests seeding the database (H2 or Testcontainer), calling the export endpoint, and validating CSV rows.  
    🔹 [Project directory](intermediate/27-csv-export-service)

28. **Real-Time Notifications**  
    🔹 This is a backend in Java, using WebSockets or SSE for pushing events.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using a client to subscribe to the SSE stream/WebSocket and wait for pushed events.  
    🔹 [Project directory](intermediate/28-real-time-notifications)

29. **Background Job Processor**  
    🔹 This is a backend in Java using **JobRunr** or **Quartz**, using databases/Redis for job queues.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests ensuring jobs are enqueued and executed (checking side effects or database status updates).  
    🔹 [Project directory](intermediate/29-background-job-processor)

30. **Config Management**  
    🔹 This is a backend in Java using **Spring Cloud Config**, loading configuration from a central source.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests verifying the application startup loads properties from a Git repo or local config backend.  
    🔹 [Project directory](intermediate/30-config-management)

31. **Feature Flag Service**  
    🔹 This is a backend in Java using **FF4j** or custom logic, managing feature toggles via API.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests enabling/disabling flags and verifying endpoint behavior changes.  
    🔹 [Project directory](intermediate/31-feature-flag-service)

32. **QR Code Generator**  
    🔹 This is a backend in Java using **ZXing**, generating QR code images from text.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests generating a QR code and using a reader library to decode and verify the content.  
    🔹 [Project directory](intermediate/32-qr-code-generator)

33. **Two-Factor Authentication (TOTP)**  
    🔹 This is a backend in Java, implementing TOTP (Time-based One-Time Password) validation (like Google Authenticator).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests generating a secret, creating a code, and validating it within the time window.  
    🔹 [Project directory](intermediate/33-two-factor-auth)

34. **Web Scraper API**  
    🔹 This is a backend in Java using **Jsoup**, extracting specific data (e.g., meta tags, headers) from a provided URL.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests with mocked HTML content to verify extraction logic handles various DOM structures.  
    🔹 [Project directory](intermediate/34-web-scraper-api)

35. **Markdown to HTML Converter**  
    🔹 This is a backend in Java using **CommonMark** or **Flexmark**, converting Markdown text to HTML.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests providing Markdown input and asserting the correct HTML output structure.  
    🔹 [Project directory](intermediate/35-markdown-converter)

36. **IP Geolocation Service**  
    🔹 This is a backend in Java using **MaxMind GeoIP2**, returning location data for a given IP address.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests with known IP addresses (or mocked database reader) to verify returned country/city data.  
    🔹 [Project directory](intermediate/36-ip-geolocation-service)

37. **URL Health Monitor**  
    🔹 This is a backend in Java using scheduled tasks, periodically checking a list of URLs and recording their status.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests adding a URL to monitor and verifying the status history is updated after the schedule runs.  
    🔹 [Project directory](intermediate/37-url-health-monitor)

38. **Simple Search Engine**  
    🔹 This is a backend in Java, implementing a basic inverted index to search text documents.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests indexing a set of documents and asserting that search queries return the correct document IDs.  
    🔹 [Project directory](intermediate/38-simple-search-engine)

39. **Digital Signature Service**  
    🔹 This is a backend in Java using `java.security`, signing data with a private key and verifying with a public key.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests signing data and verifying the signature with the corresponding key pair.  
    🔹 [Project directory](intermediate/39-digital-signature-service)

40. **Syntax Highlighter API**  
    🔹 This is a backend in Java, taking code text and returning HTML with syntax highlighting (e.g. using a library like **RSyntaxTextArea** internals or similar).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests submitting code snippets and checking for expected HTML span tags and classes.  
    🔹 [Project directory](intermediate/40-syntax-highlighter-api)

41. **Stock Price Simulator**  
    🔹 This is a backend in Java, streaming random stock price updates via Server-Sent Events (SSE).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests connecting to the SSE endpoint and verifying the stream format and data frequency.  
    🔹 [Project directory](intermediate/41-stock-price-simulator)

42. **File Encryption Service**  
    🔹 This is a backend in Java using `javax.crypto`, exposing endpoints to encrypt and decrypt files.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests uploading a file to encrypt, then decrypting it and verifying it matches the original.  
    🔹 [Project directory](intermediate/42-file-encryption-service)

43. **Audio Streaming Server**  
    🔹 This is a backend in Java, streaming audio files using HTTP Byte Ranges.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests requesting specific byte ranges and verifying `Content-Range` headers and partial content.  
    🔹 [Project directory](intermediate/43-audio-streaming-server)

44. **Whois Lookup API**  
    🔹 This is a backend in Java using **Apache Commons Net**, querying WHOIS servers for domain information.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests (possibly with a mock WHOIS server) to parse and return domain registration details.  
    🔹 [Project directory](intermediate/44-whois-lookup-api)

45. **Sentiment Analysis API**  
    🔹 This is a backend in Java using a simple NLP library (like **Stanford CoreNLP** or a basic word list), scoring text sentiment.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests with clear positive/negative text samples to verify scoring logic.  
    🔹 [Project directory](intermediate/45-sentiment-analysis-api)

46. **Leaderboard Service**  
    🔹 This is a backend in Java using **Redis Sorted Sets**, managing real-time high scores.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Redis) to submit scores and retrieve the top N rank.  
    🔹 [Project directory](intermediate/46-leaderboard-service)

47. **Audit Logging Service**  
    🔹 This is a backend in Java using **AOP (Aspect Oriented Programming)**, automatically logging method calls and changes.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests triggering service methods and verifying audit records are created in the database/log.  
    🔹 [Project directory](intermediate/47-audit-logging-service)

48. **Distributed Lock Manager**  
    🔹 This is a backend in Java using **Redis** or **Database** locks, ensuring exclusive access to a resource.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Concurrency tests trying to acquire the lock from multiple threads/instances and verifying only one succeeds.  
    🔹 [Project directory](intermediate/48-distributed-lock-manager)

49. **Simple Wiki API**  
    🔹 This is a backend in Java, storing pages with version history (simple version control).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests updating a page multiple times and retrieving past versions to verify history integrity.  
    🔹 [Project directory](intermediate/49-simple-wiki-api)

50. **WebHook Delivery System**  
    🔹 This is a backend in Java, accepting events and delivering payloads to registered callback URLs.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **WireMock** as a receiver. Trigger an event and verify the system calls the webhook URL.  
    🔹 [Project directory](intermediate/50-webhook-delivery-system)

## Advanced
1. **Microservices with Consul**  
    🔹 This is a microservices backend in Java using **Spring Cloud Consul**, service discovery, and health checks.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (Consul). Verify services register themselves and can discover peers.  
    🔹 [Project directory](advanced/01-microservices-with-consul)

2. **Event Sourcing with CQRS**  
    🔹 This is a microservices backend in Java using **Axon Framework**, implementing CQRS pattern and event sourcing.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests using **Axon Test Fixtures** to verify events are published given commands.  
    🔹 [Project directory](advanced/02-event-sourcing-with-cqrs)

3. **Distributed Tracing with Zipkin**  
    🔹 This is a microservices backend in Java using **Spring Cloud Sleuth/Micrometer**, spanning multiple services.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **Testcontainers** (Zipkin) to verify spans are reported across service calls.  
    🔹 [Project directory](advanced/03-distributed-tracing-with-zipkin)

4. **Service Mesh with Istio**  
    🔹 This is a microservices backend in Java, deployed in Istio mesh with traffic routing.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Focus on **Contract Tests** (e.g., using **Pact**) to ensure services communicate correctly regardless of the mesh layer.  
    🔹 [Project directory](advanced/04-service-mesh-with-istio)

5. **Reverse Proxy with Custom Logic**  
    🔹 This is a backend in Java, implementing custom routing rules (e.g. using Zuul or Gateway).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **WireMock** as upstream services to verify proxy routing rules and header modifications.  
    🔹 [Project directory](advanced/05-reverse-proxy-with-custom-logic)

6. **Video Streaming Server**  
    🔹 This is a backend in Java, handling RTMP/HLS real-time streaming (e.g. using Red5 or Ant Media).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Load tests using **JMeter** or similar to verify stream stability. Integration tests checking stream manifest availability.  
    🔹 [Project directory](advanced/06-video-streaming-server)

7. **Machine Learning Inference**  
    🔹 This is a backend in Java using **DJL (Deep Java Library)**, serving ML models.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests with dummy models or inputs to verify the inference pipeline and API response format.  
    🔹 [Project directory](advanced/07-machine-learning-inference)

8. **Blockchain Integration**  
    🔹 This is a backend in Java using **Web3j**, interacting with Ethereum compatible blockchains.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Geth or Ganache) to run smart contract interactions against a local chain.  
    🔹 [Project directory](advanced/08-blockchain-integration)

9. **Serverless Function Handler**  
    🔹 This is a backend in Java using **Spring Cloud Function**, deployable to AWS Lambda.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests invoking the functions directly. Integration tests using **LocalStack** to simulate AWS Lambda execution.  
    🔹 [Project directory](advanced/09-serverless-function-handler)

10. **Distributed Cache with Redis Cluster**  
    🔹 This is a backend in Java using Lettuce/Jedis, using Redis Cluster and sharding.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Redis Cluster). Verify data distribution and failover behavior.  
    🔹 [Project directory](advanced/10-distributed-cache-with-redis-cluster)

11. **Saga Pattern for Transactions**  
    🔹 This is a microservices backend in Java using **Axon** or **Temporal**, orchestrating distributed transactions.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests verifying the happy path and compensation logic (rollback) in case of failures.  
    🔹 [Project directory](advanced/11-saga-pattern-for-transactions)

12. **Zero Trust Security**  
    🔹 This is a backend in Java, implementing identity-based access.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Security integration tests attempting to access resources with various valid/invalid identities and tokens.  
    🔹 [Project directory](advanced/12-zero-trust-security)

13. **High Concurrency with Virtual Threads**  
    🔹 This is a backend in Java 21+, handling high concurrency using Virtual Threads (Project Loom).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Load tests using **Gatling** or **k6** to spawn thousands of concurrent requests and verify throughput/latency.  
    🔹 [Project directory](advanced/13-high-concurrency-with-virtual-threads)

14. **Custom Protocol Server**  
    🔹 This is a backend in Java using **Netty**, defining a binary protocol over TCP.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using a Netty client to send binary payloads and verify correct encoding/decoding and responses.  
    🔹 [Project directory](advanced/14-custom-protocol-server)

15. **Federated GraphQL**  
    🔹 This is a microservices backend in Java using **Apollo Federation** with Spring GraphQL.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests verifying that the gateway correctly stitches schemas and resolves queries across subgraphs.  
    🔹 [Project directory](advanced/15-federated-graphql)

16. **AI Chatbot Backend**  
    🔹 This is a backend in Java using **LangChain4j**, integrating with LLMs.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **WireMock** to simulate the LLM provider API (OpenAI, etc.) and verify prompt construction.  
    🔹 [Project directory](advanced/16-ai-chatbot-backend)

17. **Dynamic Plugin System**  
    🔹 This is a backend in Java using **PF4J** or custom class loading, allowing runtime plugin loading/unloading.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests loading a dummy plugin jar and executing its extension point.  
    🔹 [Project directory](advanced/17-dynamic-plugin-system)

18. **Peer-to-Peer File Sharing**  
    🔹 This is a backend in Java using **JXTA** or raw UDP/TCP with NAT traversal, for decentralized file sharing.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests spawning multiple "peer" instances locally and transferring a file between them.  
    🔹 [Project directory](advanced/18-peer-to-peer-file-sharing)

19. **Distributed Task Scheduler**  
    🔹 This is a backend in Java using **Quartz** with JDBC job store or **db-scheduler**, coordinating tasks across nodes.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests with multiple instances connected to the same DB, verifying a task runs only once.  
    🔹 [Project directory](advanced/19-distributed-task-scheduler)

20. **Real-Time Collaboration Tool**  
    🔹 This is a backend in Java using **Operational Transformation (OT)** or **CRDTs** (e.g., Yjs port) over WebSockets.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Simulation tests applying concurrent edits from multiple clients and verifying eventual consistency.  
    🔹 [Project directory](advanced/20-real-time-collaboration-tool)

21. **Custom Database Engine**  
    🔹 This is a backend in Java, implementing a simple append-only log or B-Tree based storage engine.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests for the storage format. Integration tests performing heavy read/write operations and verifying persistence.  
    🔹 [Project directory](advanced/21-custom-database-engine)

22. **IoT Device Gateway**  
    🔹 This is a backend in Java using **Eclipse Milo** (OPC UA) or **MQTT** (HiveMQ client), bridging device data to cloud.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests using **Testcontainers** (Mosquitto) to simulate device messages and verify processing.  
    🔹 [Project directory](advanced/22-iot-device-gateway)

23. **Rule Engine Service**  
    🔹 This is a backend in Java using **Drools**, executing complex business rules dynamically.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests defining DRL files and asserting that facts match the expected rule outcomes.  
    🔹 [Project directory](advanced/23-rule-engine-service)

24. **Custom Service Discovery**  
    🔹 This is a backend in Java using **Gossip Protocol** or a central registry with heartbeats (like Eureka basics).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests starting multiple service nodes and verifying the registry detects failures/joins.  
    🔹 [Project directory](advanced/24-custom-service-discovery)

25. **Multi-Tenant SaaS Platform**  
    🔹 This is a backend in Java using **Hibernate** with schema-per-tenant or discriminator column strategy.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests switching tenant context (e.g., header-based) and ensuring data isolation between tenants.  
    🔹 [Project directory](advanced/25-multi-tenant-saas-platform)

26. **Custom Load Balancer**  
    🔹 This is a backend in Java using **Netty**, implementing Round-Robin or Least-Connections algorithms.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests with backend mock servers, verifying request distribution matches the algorithm.  
    🔹 [Project directory](advanced/26-custom-load-balancer)

27. **Change Data Capture Service**  
    🔹 This is a backend in Java using **Debezium** (embedded) or tailing MySQL binlogs directly.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests making DB changes and asserting the capture service emits the corresponding events.  
    🔹 [Project directory](advanced/27-change-data-capture-service)

28. **Distributed Counter**  
    🔹 This is a backend in Java using **CRDTs** or **Redis HyperLogLog** for approximate counting at scale.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Concurrent load tests incrementing the counter and verifying the final count (or error margin).  
    🔹 [Project directory](advanced/28-distributed-counter)

29. **Video Transcoding Service**  
    🔹 This is a backend in Java using **Jave2** or FFmpeg, managing a queue of video conversion jobs.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests uploading a video and waiting for the transcoded output to appear in storage.  
    🔹 [Project directory](advanced/29-video-transcoding-service)

30. **Recommendation Engine**  
    🔹 This is a backend in Java using **Apache Mahout** or simple Collaborative Filtering.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests with a known dataset matrix, verifying that the top N recommendations match expected items.  
    🔹 [Project directory](advanced/30-recommendation-engine)

31. **High Frequency Trading Bot**  
    🔹 This is a backend in Java using **LMAX Disruptor** for ultra-low latency event processing.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Benchmark tests measuring the time from "tick" ingestion to "order" generation (latency in microseconds).  
    🔹 [Project directory](advanced/31-high-frequency-trading-bot)

32. **Geospatial Indexing Service**  
    🔹 This is a backend in Java using **R-Tree** or **Quadtree** implementation (or PostGIS integration).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests adding points and querying for "points within radius", verifying spatial accuracy.  
    🔹 [Project directory](advanced/32-geospatial-indexing-service)

33. **Distributed Key-Value Store**  
    🔹 This is a backend in Java, implementing consistent hashing and replication (like DynamoDB basics).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests killing a node and verifying data can still be retrieved from replicas.  
    🔹 [Project directory](advanced/33-distributed-key-value-store)

34. **Log Aggregator Agent**  
    🔹 This is a backend in Java, tailing local files and shipping logs to a central server (like Filebeat).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests writing to a monitored file and verifying the agent forwards the lines.  
    🔹 [Project directory](advanced/34-log-aggregator-agent)

35. **Voice Recognition API**  
    🔹 This is a backend in Java using **Vosk** or **Sphinx** for offline speech-to-text.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests uploading an audio sample and checking the returned text transcript.  
    🔹 [Project directory](advanced/35-voice-recognition-api)

36. **Code Execution Sandbox**  
    🔹 This is a backend in Java using **Docker Java API** to run untrusted code safely in isolated containers.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests submitting infinite loops or malicious code and verifying container limits/timeout.  
    🔹 [Project directory](advanced/36-code-execution-sandbox)

37. **Distributed File System**  
    🔹 This is a backend in Java, implementing chunking and distribution of files across nodes (like HDFS mini).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests uploading a large file, verifying chunks are spread across mock nodes.  
    🔹 [Project directory](advanced/37-distributed-file-system)

38. **API Analytics Dashboard**  
    🔹 This is a backend in Java, aggregating API usage metrics (latency, error rates) into time-series data.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Load tests generating traffic and verifying the aggregation logic (e.g., p95 latency calculation).  
    🔹 [Project directory](advanced/38-api-analytics-dashboard)

39. **Graph Database Implementation**  
    🔹 This is a backend in Java, implementing a graph structure (nodes/edges) with traversal algorithms.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests creating a graph and running BFS/DFS or shortest path algorithms.  
    🔹 [Project directory](advanced/39-graph-database-implementation)

40. **Workflow Orchestration Engine**  
    🔹 This is a backend in Java, parsing and executing DAGs (Directed Acyclic Graphs) of tasks.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests defining a DAG with dependencies and verifying the execution order.  
    🔹 [Project directory](advanced/40-workflow-orchestration-engine)

41. **Custom Identity Provider**  
    🔹 This is a backend in Java using **Spring Authorization Server**, implementing OIDC (OpenID Connect).  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using an OIDC client to perform the full login flow and validate ID tokens.  
    🔹 [Project directory](advanced/41-custom-identity-provider)

42. **Distributed Web Crawler**  
    🔹 This is a backend in Java using **Storm** or **Hazelcast** to coordinate crawl frontiers across nodes.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests pointing the crawler at a local test site and verifying page visitation coverage.  
    🔹 [Project directory](advanced/42-distributed-web-crawler)

43. **Payment Reconciliation System**  
    🔹 This is a backend in Java, processing large datasets to match internal records with bank statements.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Unit tests with mismatched datasets to verify discrepancy detection logic.  
    🔹 [Project directory](advanced/43-payment-reconciliation-system)

44. **Distributed Rate Limiter**  
    🔹 This is a backend in Java using **Redis Lua Scripts** (Sliding Window Log) shared across instances.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Distributed load test verifying that the global limit is respected across multiple app instances.  
    🔹 [Project directory](advanced/44-distributed-rate-limiter)

45. **E2E Encrypted Chat**  
    🔹 This is a backend in Java, handling key exchange (Diffie-Hellman) and relaying encrypted messages.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests where the server cannot decrypt the message, but the recipient can.  
    🔹 [Project directory](advanced/45-e2e-encrypted-chat)

46. **Anomaly Detection System**  
    🔹 This is a backend in Java using statistical methods (e.g., Z-score) or isolation forests on stream data.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests feeding a stream of normal data followed by an outlier and checking for alert generation.  
    🔹 [Project directory](advanced/46-anomaly-detection-system)

47. **Digital Asset Management**  
    🔹 This is a backend in Java, handling metadata extraction (EXIF, XMP) and versioning of assets.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests uploading media and searching by extracted metadata.  
    🔹 [Project directory](advanced/47-digital-asset-management)

48. **WebAssembly Host**  
    🔹 This is a backend in Java using **Chicory** or **GraalVM**, executing WASM modules server-side.  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Unit tests loading a compiled WASM function (e.g., Rust/C++) and invoking it from Java.  
    🔹 [Project directory](advanced/48-webassembly-host)

49. **Reactive Microservices**  
    🔹 This is a microservices backend in Java using **Spring WebFlux** and **RSocket** for backpressure support.  
    📦 **Dependency Manager**: Maven  
    🧪 **Testing**: Integration tests using **StepVerifier** to check reactive streams and backpressure signals.  
    🔹 [Project directory](advanced/49-reactive-microservices)

50. **Chaos Engineering Tool**  
    🔹 This is a backend in Java, injecting failures (latency, exceptions) into running JVMs (agent-based).  
    📦 **Dependency Manager**: Gradle  
    🧪 **Testing**: Integration tests attaching the agent to a dummy app and verifying that configured exceptions are thrown.  
    🔹 [Project directory](advanced/50-chaos-engineering-tool)
