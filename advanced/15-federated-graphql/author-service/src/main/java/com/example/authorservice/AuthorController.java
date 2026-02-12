package com.example.authorservice;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class AuthorController {

    @QueryMapping
    public List<Author> authors() {
        return Author.getAllAuthors();
    }

    @QueryMapping
    public Optional<Author> authorById(@Argument String id) {
        return Author.getAuthorById(id);
    }
}
