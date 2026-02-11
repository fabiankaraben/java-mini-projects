package com.example.bookservice;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    @QueryMapping
    public List<Book> books() {
        return Book.getAllBooks();
    }

    @QueryMapping
    public Optional<Book> bookById(@Argument String id) {
        return Book.getBookById(id);
    }

    @SchemaMapping(typeName = "Book")
    public Author author(Book book) {
        return new Author(book.authorId());
    }

    @SchemaMapping(typeName = "Author")
    public List<Book> books(Author author) {
        return Book.getBooksByAuthor(author.id());
    }
}
