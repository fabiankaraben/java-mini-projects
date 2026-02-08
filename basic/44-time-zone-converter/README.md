# Time Zone Converter

This is a basic backend application built with Java and Spring Boot that converts times between different time zones using the `java.time` API.

## Requirements

- Java 17 or higher
- Gradle (Wrapper included)

## Features

- Convert a date-time string from one time zone to another.
- List all available time zone IDs.
- Handles Daylight Saving Time (DST) transitions automatically.

## How to Use

### 1. Run the Application

You can run the application using the Gradle wrapper:

```bash
./gradlew bootRun
```

The application will start on port 8080 (default).

### 2. API Endpoints

#### Convert Time

**Endpoint:** `GET /api/convert`

**Parameters:**
- `time`: The date time to convert in `yyyy-MM-dd HH:mm:ss` format.
- `sourceZone`: The ID of the source time zone (e.g., `UTC`, `America/New_York`).
- `targetZone`: The ID of the target time zone (e.g., `Europe/London`, `Asia/Tokyo`).

**Example (Curl):**

Convert UTC to Eastern Standard Time (EST/EDT):

```bash
curl "http://localhost:8080/api/convert?time=2024-06-01%2012:00:00&sourceZone=UTC&targetZone=America/New_York"
```

**Response:**

```json
{
  "originalTime": "2024-06-01 12:00:00",
  "sourceZone": "UTC",
  "targetZone": "America/New_York",
  "convertedTime": "2024-06-01 08:00:00"
}
```

#### List Available Zones

**Endpoint:** `GET /api/zones`

**Example (Curl):**

```bash
curl "http://localhost:8080/api/zones"
```

## Running Tests

To run the unit tests, use the following command:

```bash
./gradlew clean test
```

This will run all tests, including edge cases for Daylight Saving Time transitions (Spring forward and Fall back).
