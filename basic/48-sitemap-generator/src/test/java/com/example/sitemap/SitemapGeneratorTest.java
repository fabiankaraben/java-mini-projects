package com.example.sitemap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.Input;
import org.xmlunit.validation.Languages;
import org.xmlunit.validation.ValidationProblem;
import org.xmlunit.validation.ValidationResult;
import org.xmlunit.validation.Validator;

import javax.xml.transform.Source;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SitemapGeneratorTest {
    
    private SitemapGenerator generator;
    
    private static final String SITEMAP_XSD = """
        <?xml version="1.0" encoding="UTF-8"?>
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
                   targetNamespace="http://www.sitemaps.org/schemas/sitemap/0.9"
                   xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
                   elementFormDefault="qualified">
          <xs:element name="urlset">
            <xs:complexType>
              <xs:sequence>
                <xs:element name="url" maxOccurs="unbounded">
                  <xs:complexType>
                    <xs:sequence>
                      <xs:element name="loc" type="xs:anyURI"/>
                      <xs:element name="lastmod" type="xs:string" minOccurs="0"/>
                      <xs:element name="changefreq" minOccurs="0">
                        <xs:simpleType>
                          <xs:restriction base="xs:string">
                            <xs:enumeration value="always"/>
                            <xs:enumeration value="hourly"/>
                            <xs:enumeration value="daily"/>
                            <xs:enumeration value="weekly"/>
                            <xs:enumeration value="monthly"/>
                            <xs:enumeration value="yearly"/>
                            <xs:enumeration value="never"/>
                          </xs:restriction>
                        </xs:simpleType>
                      </xs:element>
                      <xs:element name="priority" minOccurs="0">
                        <xs:simpleType>
                          <xs:restriction base="xs:decimal">
                            <xs:minInclusive value="0.0"/>
                            <xs:maxInclusive value="1.0"/>
                          </xs:restriction>
                        </xs:simpleType>
                      </xs:element>
                    </xs:sequence>
                  </xs:complexType>
                </xs:element>
              </xs:sequence>
            </xs:complexType>
          </xs:element>
        </xs:schema>
        """;
    
    @BeforeEach
    void setUp() {
        generator = new SitemapGenerator();
    }
    
    @Test
    void testGenerateDefaultSitemap() {
        String sitemap = generator.generateDefaultSitemap();
        
        assertNotNull(sitemap);
        assertTrue(sitemap.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(sitemap.contains("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"));
        assertTrue(sitemap.contains("</urlset>"));
        assertTrue(sitemap.contains("<loc>https://example.com/</loc>"));
    }
    
    @Test
    void testGenerateSitemapWithSingleUrl() {
        List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
            new SitemapGenerator.UrlEntry("https://test.com/", "2024-01-01", "daily", "1.0")
        );
        
        String sitemap = generator.generateSitemap(urls);
        
        assertNotNull(sitemap);
        assertTrue(sitemap.contains("<loc>https://test.com/</loc>"));
        assertTrue(sitemap.contains("<lastmod>2024-01-01</lastmod>"));
        assertTrue(sitemap.contains("<changefreq>daily</changefreq>"));
        assertTrue(sitemap.contains("<priority>1.0</priority>"));
    }
    
    @Test
    void testGenerateSitemapWithMultipleUrls() {
        List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
            new SitemapGenerator.UrlEntry("https://test.com/page1", "2024-01-01", "daily", "1.0"),
            new SitemapGenerator.UrlEntry("https://test.com/page2", "2024-01-02", "weekly", "0.8"),
            new SitemapGenerator.UrlEntry("https://test.com/page3", "2024-01-03", "monthly", "0.5")
        );
        
        String sitemap = generator.generateSitemap(urls);
        
        assertNotNull(sitemap);
        assertTrue(sitemap.contains("<loc>https://test.com/page1</loc>"));
        assertTrue(sitemap.contains("<loc>https://test.com/page2</loc>"));
        assertTrue(sitemap.contains("<loc>https://test.com/page3</loc>"));
    }
    
    @Test
    void testGenerateSitemapWithOptionalFields() {
        List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
            new SitemapGenerator.UrlEntry("https://test.com/", null, null, null)
        );
        
        String sitemap = generator.generateSitemap(urls);
        
        assertNotNull(sitemap);
        assertTrue(sitemap.contains("<loc>https://test.com/</loc>"));
        assertFalse(sitemap.contains("<lastmod>"));
        assertFalse(sitemap.contains("<changefreq>"));
        assertFalse(sitemap.contains("<priority>"));
    }
    
    @Test
    void testXmlEscaping() {
        List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
            new SitemapGenerator.UrlEntry("https://test.com/page?param=value&other=123", "2024-01-01", "daily", "1.0")
        );
        
        String sitemap = generator.generateSitemap(urls);
        
        assertNotNull(sitemap);
        assertTrue(sitemap.contains("&amp;"));
        assertFalse(sitemap.contains("&other"));
    }
    
    @Test
    void testDefaultSitemapValidatesAgainstSchema() {
        String sitemap = generator.generateDefaultSitemap();
        
        Source sitemapSource = Input.fromString(sitemap).build();
        Source schemaSource = Input.fromString(SITEMAP_XSD).build();
        
        Validator validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
        validator.setSchemaSource(schemaSource);
        ValidationResult result = validator.validateInstance(sitemapSource);
        
        if (!result.isValid()) {
            StringBuilder errors = new StringBuilder("Sitemap validation failed:\n");
            for (ValidationProblem problem : result.getProblems()) {
                errors.append("- ").append(problem.getMessage()).append("\n");
            }
            fail(errors.toString());
        }
        
        assertTrue(result.isValid(), "Default sitemap should validate against sitemap schema");
    }
    
    @Test
    void testCustomSitemapValidatesAgainstSchema() {
        List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
            new SitemapGenerator.UrlEntry("https://example.com/", "2024-01-01", "daily", "1.0"),
            new SitemapGenerator.UrlEntry("https://example.com/about", "2024-01-02", "weekly", "0.8"),
            new SitemapGenerator.UrlEntry("https://example.com/contact", null, null, null)
        );
        
        String sitemap = generator.generateSitemap(urls);
        
        Source sitemapSource = Input.fromString(sitemap).build();
        Source schemaSource = Input.fromString(SITEMAP_XSD).build();
        
        Validator validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
        validator.setSchemaSource(schemaSource);
        ValidationResult result = validator.validateInstance(sitemapSource);
        
        if (!result.isValid()) {
            StringBuilder errors = new StringBuilder("Sitemap validation failed:\n");
            for (ValidationProblem problem : result.getProblems()) {
                errors.append("- ").append(problem.getMessage()).append("\n");
            }
            fail(errors.toString());
        }
        
        assertTrue(result.isValid(), "Custom sitemap should validate against sitemap schema");
    }
    
    @Test
    void testEmptySitemapValidatesAgainstSchema() {
        List<SitemapGenerator.UrlEntry> urls = Arrays.asList();
        
        String sitemap = generator.generateSitemap(urls);
        
        Source sitemapSource = Input.fromString(sitemap).build();
        Source schemaSource = Input.fromString(SITEMAP_XSD).build();
        
        Validator validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
        validator.setSchemaSource(schemaSource);
        ValidationResult result = validator.validateInstance(sitemapSource);
        
        assertFalse(result.isValid(), "Empty sitemap should not validate (requires at least one URL)");
    }
    
    @Test
    void testAllChangefreqValues() {
        String[] changefreqs = {"always", "hourly", "daily", "weekly", "monthly", "yearly", "never"};
        
        for (String changefreq : changefreqs) {
            List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
                new SitemapGenerator.UrlEntry("https://test.com/", "2024-01-01", changefreq, "0.5")
            );
            
            String sitemap = generator.generateSitemap(urls);
            
            Source sitemapSource = Input.fromString(sitemap).build();
            Source schemaSource = Input.fromString(SITEMAP_XSD).build();
            
            Validator validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
            validator.setSchemaSource(schemaSource);
            ValidationResult result = validator.validateInstance(sitemapSource);
            
            assertTrue(result.isValid(), "Sitemap with changefreq '" + changefreq + "' should validate");
        }
    }
    
    @Test
    void testPriorityBoundaries() {
        String[] priorities = {"0.0", "0.5", "1.0"};
        
        for (String priority : priorities) {
            List<SitemapGenerator.UrlEntry> urls = Arrays.asList(
                new SitemapGenerator.UrlEntry("https://test.com/", "2024-01-01", "daily", priority)
            );
            
            String sitemap = generator.generateSitemap(urls);
            
            Source sitemapSource = Input.fromString(sitemap).build();
            Source schemaSource = Input.fromString(SITEMAP_XSD).build();
            
            Validator validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
            validator.setSchemaSource(schemaSource);
            ValidationResult result = validator.validateInstance(sitemapSource);
            
            assertTrue(result.isValid(), "Sitemap with priority '" + priority + "' should validate");
        }
    }
}
