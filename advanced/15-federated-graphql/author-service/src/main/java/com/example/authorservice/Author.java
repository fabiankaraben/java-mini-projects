package com.example.authorservice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record Author(String id, String name) {
    private static final List<Author> AUTHORS = Arrays.asList(
            new Author("author-1", "Joshua Bloch"),
            new Author("author-2", "Douglas Crockford"),
            new Author("author-3", "Martin Fowler")
    );

    public static List<Author> getAllAuthors() {
        return AUTHORS;
    }

    public static Optional<Author> getAuthorById(String id) {
        return AUTHORS.stream().filter(author -> author.id().equals(id)).findFirst();
    }
}
