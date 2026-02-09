# GraphQL Server

This mini-project demonstrates a GraphQL server implementation in Java using **Spring for GraphQL**. It uses an in-memory data source and a defined GraphQL schema to serve queries and mutations.

## Requirements

- Java 17 or higher
- Maven

## How to Run

1. Navigate to the project directory:
   ```bash
   cd intermediate/05-graphql-server
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   (Note: If `mvnw` is not present, you can use `mvn spring-boot:run` if Maven is installed globally, or I will generate the wrapper shortly).

   The server will start at `http://localhost:8080/graphql`.
   GraphiQL (in-browser IDE) is available at `http://localhost:8080/graphiql` if enabled in properties (default for dev).

## Usage Examples

You can interact with the API using `curl` or a GraphQL client like GraphiQL.

### Query: Get book by ID

```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{"query": "query { bookById(id: \"book-1\") { id title pageCount author { firstName lastName } } }"}' \
     http://localhost:8080/graphql
```

### Query: Get all books

```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{"query": "query { allBooks { id title author { firstName } } }"}' \
     http://localhost:8080/graphql
```

### Mutation: Create a book

```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{"query": "mutation { createBook(title: \"New Book\", pageCount: 100, authorId: \"author-1\") { id title } }"}' \
     http://localhost:8080/graphql
```

## Testing

The project includes tests using `GraphQlTester` to verify the API responses.

To run the tests:

```bash
mvn test
```

The tests use `spring-graphql-test` to execute queries against the controller and verify the JSON response.
