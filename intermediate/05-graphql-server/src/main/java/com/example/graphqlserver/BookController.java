package com.example.graphqlserver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        // Initialize with some data (simulating a DB)
        books.add(new Book("book-1", "Effective Java", 416, "author-1"));
        books.add(new Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"));
        books.add(new Book("book-3", "Down Under", 436, "author-3"));
    }

    @QueryMapping
    public Book bookById(@Argument String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @QueryMapping
    public List<Book> allBooks() {
        return books;
    }

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.authorId());
    }
    
    @MutationMapping
    public Book createBook(@Argument String title, @Argument int pageCount, @Argument String authorId) {
        String id = "book-" + UUID.randomUUID().toString();
        Book book = new Book(id, title, pageCount, authorId);
        books.add(book);
        return book;
    }
}
