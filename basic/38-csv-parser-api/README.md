# CSV Parser API

This is a basic backend application built with Java and Spring Boot that parses uploaded CSV files and returns the content as a JSON response.

## 📋 Requirements

- Java 17
- Gradle (provided via wrapper, but local installation works too)

## 🚀 How to Use

### 1. Start the Application

Run the application using Gradle:

```bash
./gradlew bootRun
```

The server will start on port `8080`.

### 2. Upload a CSV File

You can upload a CSV file to the `/api/csv/upload` endpoint. The API expects a multipart file with the key `file`.

#### Example CSV file (`data.csv`)
```csv
name,age,city
John,30,New York
Jane,25,London
```

#### Curl Command
```bash
curl -F "file=@data.csv" http://localhost:8080/api/csv/upload
```

#### Expected JSON Response
```json
[
  {
    "name": "John",
    "age": "30",
    "city": "New York"
  },
  {
    "name": "Jane",
    "age": "25",
    "city": "London"
  }
]
```

## 🧪 Running Tests

This project includes unit and integration tests to ensure the CSV parsing logic and API endpoints work correctly.

To run the tests, execute the following command:

```bash
./gradlew clean test
```

The test output will show the details of passed, skipped, and failed tests.
