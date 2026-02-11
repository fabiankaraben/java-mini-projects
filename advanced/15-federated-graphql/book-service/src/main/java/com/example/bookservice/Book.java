package com.example.bookservice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record Book(String id, String title, String authorId) {
    private static final List<Book> BOOKS = Arrays.asList(
            new Book("book-1", "Effective Java", "author-1"),
            new Book("book-2", "JavaScript: The Good Parts", "author-2"),
            new Book("book-3", "Refactoring", "author-3"),
            new Book("book-4", "Clean Code", "author-4") // author-4 doesn't exist in author-service, just for fun or maybe consistent data
    );

    public static List<Book> getAllBooks() {
        return BOOKS;
    }

    public static Optional<Book> getBookById(String id) {
        return BOOKS.stream().filter(book -> book.id().equals(id)).findFirst();
    }
    
    public static List<Book> getBooksByAuthor(String authorId) {
        return BOOKS.stream().filter(book -> book.authorId().equals(authorId)).toList();
    }
}
