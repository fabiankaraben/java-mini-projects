package com.example.simplewiki.repository;

import com.example.simplewiki.model.WikiPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {
    Optional<WikiPage> findBySlug(String slug);
}
