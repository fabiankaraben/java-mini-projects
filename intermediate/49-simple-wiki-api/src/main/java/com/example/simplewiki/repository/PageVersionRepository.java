package com.example.simplewiki.repository;

import com.example.simplewiki.model.PageVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PageVersionRepository extends JpaRepository<PageVersion, Long> {
    List<PageVersion> findByWikiPageIdOrderByVersionNumberDesc(Long wikiPageId);
    Optional<PageVersion> findByWikiPageIdAndVersionNumber(Long wikiPageId, Integer versionNumber);
}
