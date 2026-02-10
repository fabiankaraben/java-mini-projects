# Zero Trust Security

This mini-project demonstrates a **Zero Trust Security** model implemented in Java using Spring Boot and Spring Security. It enforces identity-based access control where every request must be authenticated and authorized, regardless of origin.

## Features
- **Stateless Authentication**: Uses JWT (JSON Web Tokens) for per-request authentication.
- **Role-Based Access Control (RBAC)**: Fine-grained permissions (ADMIN, USER, GUEST).
- **Secure by Default**: All endpoints are protected by default, except for authentication.
- **In-Memory User Store**: Simulates a user directory for demonstration purposes.

## Requirements
- Java 17+
- Maven

## Project Structure
- `src/main/java`: Source code
  - `controller`: REST endpoints
  - `domain`: Domain models
  - `security`: Security configuration, JWT utilities and filters
  - `service`: User details service
- `src/test/java`: Integration tests

## Getting Started

### 1. Build the Project
```bash
mvn clean package
```

### 2. Run the Application
```bash
mvn spring-boot:run
```
The application will start on port `8080`.

### 3. Usage with Curl

#### Login to get a Token
Authenticate as a user to receive a JWT.
**Users available:**
- `admin` / `admin123` (Roles: ADMIN, USER)
- `user` / `user123` (Roles: USER)
- `guest` / `guest123` (Roles: GUEST)

```bash
# Login as admin
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r .token)

echo $TOKEN
```

#### Access Public Resource (Authenticated)
Accessible by any authenticated user.
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/resource/public
```

#### Access User Resource
Accessible by users with USER or ADMIN role.
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/resource/user
```

#### Access Admin Resource
Accessible only by users with ADMIN role.
```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/resource/admin
```

## Testing
Run the integration tests to verify the security policies.
```bash
mvn clean test
```
The tests cover:
- Successful and failed login attempts.
- Accessing protected resources without a token (Forbidden).
- Accessing resources with valid tokens but insufficient privileges (Forbidden).
- Successful access with correct privileges.

## Docker
You can also run the application using Docker Compose.

### Build and Run
```bash
docker compose up --build
```
The application will be available at `http://localhost:8080`.
