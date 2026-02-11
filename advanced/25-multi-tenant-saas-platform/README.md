# Multi-Tenant SaaS Platform

This is a mini-project demonstrating a Multi-Tenant backend in Java using Spring Boot and Hibernate with a **Discriminator Column** strategy.

## Features

- **Multi-Tenancy**: Data isolation using a tenant discriminator column (`tenant_id`) in the database.
- **Hibernate Filters**: Automatically filters data based on the current tenant context.
- **Tenant Context**: Uses `ThreadLocal` to store tenant information per request.
- **Interceptors**: Extracts Tenant ID from HTTP headers (`X-Tenant-ID`).
- **Integration Tests**: Verifies data isolation between tenants using Testcontainers.

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose (for running the app and tests)

## Project Structure

```
src/
├── main/
│   ├── java/com/example/multitenant/
│   │   ├── aspect/          # Hibernate Filter Aspect
│   │   ├── config/          # WebMvc and Context Config
│   │   ├── controller/      # REST Controllers
│   │   ├── interceptor/     # Request Interceptor
│   │   ├── model/           # JPA Entities
│   │   └── repository/      # Spring Data Repositories
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/multitenant/
        └── MultiTenantIntegrationTest.java
```

## How to Run

### Using Docker Compose

1. Build and start the application and database:
   ```bash
   docker compose up --build
   ```

2. The application will be available at `http://localhost:8080`.

### Manual Run

1. Start a PostgreSQL database (or use the one from docker-compose):
   ```bash
   docker compose up db -d
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Usage

You can use `curl` to interact with the API. The `X-Tenant-ID` header is **mandatory**.

### 1. Create a Product for Tenant A
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-a" \
  -d '{"name": "Laptop", "price": 1200.0}'
```

### 2. Create a Product for Tenant B
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-b" \
  -d '{"name": "Smartphone", "price": 800.0}'
```

### 3. List Products for Tenant A
```bash
curl -H "X-Tenant-ID: tenant-a" http://localhost:8080/api/products
```
*Output: Only "Laptop"*

### 4. List Products for Tenant B
```bash
curl -H "X-Tenant-ID: tenant-b" http://localhost:8080/api/products
```
*Output: Only "Smartphone"*

## Testing

Run the integration tests to verify tenant isolation:

```bash
mvn clean test
```

The tests use **Testcontainers** to spin up a PostgreSQL instance and verify that Tenant A cannot see Tenant B's data.
