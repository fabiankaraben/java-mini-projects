# CSV Export Service

## Description
This is a Java-based mini-project that provides a REST endpoint to export user data from a database to a CSV file. It uses Spring Boot, Spring Data JPA, H2 Database, and Apache Commons CSV.

## Requirements
- Java 17+
- Maven
- Docker (optional, for running the application in a container)

## Dependencies
- **Spring Boot 3.4.1**: Web, Data JPA
- **H2 Database**: In-memory database
- **Apache Commons CSV**: Library for creating CSV files

## How to Run

### Using Maven
1.  Navigate to the project directory:
    ```bash
    cd intermediate/27-csv-export-service
    ```
2.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
    (Note: You may need to `mvn wrapper:wrapper` first if `mvnw` is missing, or just use `mvn spring-boot:run` if Maven is installed).

### Using Docker
1.  Build the application:
    ```bash
    mvn clean package
    ```
2.  Build the Docker image:
    ```bash
    docker build -t csv-export-service .
    ```
3.  Run the container:
    ```bash
    docker run -p 8080:8080 csv-export-service
    ```

## Usage

### Export Users to CSV
You can download the CSV file using `curl` or a web browser.

**Request:**
```bash
curl -v http://localhost:8080/api/export/users -o users.csv
```

**Response:**
The server will respond with a file download (`users.csv`) containing the user data.

## Testing
The project includes integration tests that seed the H2 database and verify the CSV output.

To run the tests:
```bash
mvn clean test
```
