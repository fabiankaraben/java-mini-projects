package com.example.simplewiki.service;

import com.example.simplewiki.model.PageVersion;
import com.example.simplewiki.model.WikiPage;
import com.example.simplewiki.repository.PageVersionRepository;
import com.example.simplewiki.repository.WikiPageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WikiService {

    private final WikiPageRepository wikiPageRepository;
    private final PageVersionRepository pageVersionRepository;

    public WikiService(WikiPageRepository wikiPageRepository, PageVersionRepository pageVersionRepository) {
        this.wikiPageRepository = wikiPageRepository;
        this.pageVersionRepository = pageVersionRepository;
    }

    @Transactional
    public WikiPage createPage(String slug, String title, String content) {
        if (wikiPageRepository.findBySlug(slug).isPresent()) {
            throw new IllegalArgumentException("Page with slug " + slug + " already exists");
        }

        WikiPage page = new WikiPage(slug, title);
        page = wikiPageRepository.save(page);

        PageVersion version = new PageVersion(page, 1, content);
        pageVersionRepository.save(version);
        page.getVersions().add(version);

        return page;
    }

    @Transactional
    public WikiPage updatePage(String slug, String content) {
        WikiPage page = wikiPageRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));

        // Get latest version number
        List<PageVersion> versions = pageVersionRepository.findByWikiPageIdOrderByVersionNumberDesc(page.getId());
        int nextVersion = versions.isEmpty() ? 1 : versions.get(0).getVersionNumber() + 1;

        PageVersion newVersion = new PageVersion(page, nextVersion, content);
        pageVersionRepository.save(newVersion);
        page.getVersions().add(newVersion);

        return page;
    }

    public Optional<WikiPage> getPageMetadata(String slug) {
        return wikiPageRepository.findBySlug(slug);
    }

    public Optional<String> getLatestContent(String slug) {
        return wikiPageRepository.findBySlug(slug)
                .flatMap(page -> pageVersionRepository.findByWikiPageIdOrderByVersionNumberDesc(page.getId())
                        .stream().findFirst())
                .map(PageVersion::getContent);
    }

    public Optional<String> getVersionContent(String slug, Integer version) {
        return wikiPageRepository.findBySlug(slug)
                .flatMap(page -> pageVersionRepository.findByWikiPageIdAndVersionNumber(page.getId(), version))
                .map(PageVersion::getContent);
    }

    public List<PageVersion> getHistory(String slug) {
        WikiPage page = wikiPageRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));
        return pageVersionRepository.findByWikiPageIdOrderByVersionNumberDesc(page.getId());
    }
}
