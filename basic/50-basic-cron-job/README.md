# Basic Cron Job

This mini-project demonstrates a basic backend in Java that schedules a simple task using `ScheduledExecutorService`.

## Requirements

-   Java 17+
-   Gradle

## How to Use

The main class `com.example.cron.CronJob` schedules a task to print a message to the console every 2 seconds.

To run the application:

```bash
./gradlew run
```

You should see output similar to:

```
Starting cron job...
Task executed at: 1678886400000
Task executed at: 1678886402000
...
Stopping cron job...
```

The application runs for 10 seconds and then shuts down.

## Testing

This project uses JUnit 5 for testing. The tests verify that the scheduled task runs multiple times within a short period using `CountDownLatch`.

To run the tests:

```bash
./gradlew clean test
```

The `build.gradle` is configured to show test events:

```groovy
test {
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed"
    }
}
```

You should see output indicating the tests passed:

```
> Task :test

com.example.cron.CronJobTest > testTaskExecution() PASSED
```
