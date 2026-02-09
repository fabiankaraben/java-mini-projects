# REST API with SQLite

This mini-project implements a simple REST API using Spring Boot and SQLite as the database. It demonstrates how to perform CRUD operations on a `User` entity.

## Requirements

*   Java 17 or higher
*   Maven

## Project Structure

*   **Spring Boot**: Backend framework.
*   **SQLite**: Embedded relational database.
*   **Hibernate/JPA**: ORM for database interaction.
*   **Maven**: Dependency management.

## Setup and Run

1.  **Clone the repository** (if not already done).
2.  **Navigate to the project directory**:
    ```bash
    cd intermediate/01-rest-api-with-sqlite
    ```
3.  **Run the application**:
    ```bash
    ./mvnw spring-boot:run
    ```
    (Ensure you have the Maven wrapper script `mvnw`. If not, you can use `mvn spring-boot:run` if Maven is installed globally).

The application will start on `http://localhost:8080`.
The SQLite database file `users.db` will be created in the project root directory.

## API Endpoints

### 1. Create a User

*   **URL**: `/api/users`
*   **Method**: `POST`
*   **Body**: JSON
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@example.com"
    }
    ```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{"name": "John Doe", "email": "john.doe@example.com"}'
```

### 2. Get All Users

*   **URL**: `/api/users`
*   **Method**: `GET`

**cURL Example**:
```bash
curl http://localhost:8080/api/users
```

### 3. Get User by ID

*   **URL**: `/api/users/{id}`
*   **Method**: `GET`

**cURL Example**:
```bash
curl http://localhost:8080/api/users/1
```

### 4. Update User

*   **URL**: `/api/users/{id}`
*   **Method**: `PUT`
*   **Body**: JSON
    ```json
    {
      "name": "Jane Doe",
      "email": "jane.doe@example.com"
    }
    ```

**cURL Example**:
```bash
curl -X PUT http://localhost:8080/api/users/1 \
-H "Content-Type: application/json" \
-d '{"name": "Jane Doe", "email": "jane.doe@example.com"}'
```

### 5. Delete User

*   **URL**: `/api/users/{id}`
*   **Method**: `DELETE`

**cURL Example**:
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Testing

Integration tests are included to verify the API functionality. The tests use an in-memory SQLite database to ensure isolation and not affect the persistent `users.db` file.

To run the tests:

```bash
./mvnw clean test
```

Or using global Maven:

```bash
mvn clean test
```
