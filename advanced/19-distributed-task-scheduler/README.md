# Distributed Task Scheduler

A distributed task scheduler implementation using **Java**, **Spring Boot**, and **Quartz Scheduler** with a **PostgreSQL** JDBC job store. This project demonstrates how to coordinate tasks across multiple application nodes, ensuring that a task runs only once even when multiple instances of the application are running (Quartz Clustering).

## Requirements

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for running the database and application)

## Project Structure

- `src/main/java`: Source code for the Spring Boot application.
- `src/test/java`: Integration tests using Testcontainers to verify distributed execution.
- `docker-compose.yml`: Docker Compose configuration for PostgreSQL and the application.
- `Dockerfile`: Docker image definition for the application.

## How to Run

### Using Docker Compose

The easiest way to run the application is using Docker Compose, which sets up the PostgreSQL database and the application container.

1.  Build the application:
    ```bash
    mvn clean package -DskipTests
    ```

2.  Start the services:
    ```bash
    docker compose up --build
    ```

    This will start the PostgreSQL database and one instance of the scheduler application. You can scale the application to test distributed scheduling:
    
    ```bash
    docker compose up -d --scale app=2
    ```

### Local Development

1.  Start the PostgreSQL database:
    ```bash
    docker compose up postgres -d
    ```

2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

## Usage

Once the application is running (default port 8080), you can schedule jobs using the REST API.

### Schedule a Job

Schedule a simple job to run after a specified delay (in seconds).

```bash
curl -X POST "http://localhost:8080/api/jobs/schedule?name=my-job&delaySeconds=5"
```

**Response:**
```
Job scheduled: my-job-c4c0f2a1-... to run in 5 seconds
```

Check the application logs to see the job execution. If running multiple instances, only one instance should execute the job.

```
INFO ... [main] com.example.scheduler.job.SimpleJob : Executing job: my-job-... on instance: instanceId...
```

## Testing

The project includes integration tests that use **Testcontainers** to spin up a PostgreSQL container and verify that Quartz clustering works correctly. The test starts two application contexts within the same test process and asserts that a scheduled job is executed exactly once across the cluster.

To run the tests:

```bash
mvn clean test
```

## Implementation Details

- **Quartz Clustering**: Configured in `application.properties` with `spring.quartz.jobStore.isClustered=true`.
- **JDBC Job Store**: Uses PostgreSQL to store job and trigger details, allowing multiple nodes to share the scheduling state.
- **Testcontainers**: Used for reliable integration testing against a real PostgreSQL database.
