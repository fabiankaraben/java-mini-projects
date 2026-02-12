package com.fabiankaraben.dfs;

import com.fabiankaraben.dfs.service.NameNodeService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DfsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NameNodeService nameNodeService;

    @RegisterExtension
    static WireMockExtension wireMockServer1 = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @RegisterExtension
    static WireMockExtension wireMockServer2 = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("dfs.datanodes.urls", () -> 
            wireMockServer1.baseUrl() + "," + wireMockServer2.baseUrl()
        );
        // Set chunk size to small value to force multiple chunks
        registry.add("dfs.namenode.chunk-size", () -> "10");
    }

    @BeforeEach
    void setUp() {
        // Mock upload endpoint on data nodes
        wireMockServer1.stubFor(post(urlPathMatching("/datanode/chunks/.*"))
                .willReturn(aResponse().withStatus(200).withBody("Chunk stored successfully")));
        wireMockServer2.stubFor(post(urlPathMatching("/datanode/chunks/.*"))
                .willReturn(aResponse().withStatus(200).withBody("Chunk stored successfully")));
        
        // Mock download endpoint on data nodes
        wireMockServer1.stubFor(WireMock.get(urlPathMatching("/datanode/chunks/.*"))
                .willReturn(aResponse().withStatus(200).withBody("chunk-data-1".getBytes())));
        wireMockServer2.stubFor(WireMock.get(urlPathMatching("/datanode/chunks/.*"))
                .willReturn(aResponse().withStatus(200).withBody("chunk-data-2".getBytes())));
    }

    @Test
    void testFileUploadAndDistribution() throws Exception {
        // Create a file larger than chunk size (10 bytes)
        // "Hello World! This is a test file for DFS." is > 30 bytes
        String content = "Hello World! This is a test file for DFS.";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );

        mockMvc.perform(multipart("/namenode/files").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename", is("test.txt")))
                .andExpect(jsonPath("$.chunks", hasSize(5))); // 41 bytes / 10 bytes = 5 chunks

        // Verify that data nodes received chunks
        wireMockServer1.verify(postRequestedFor(urlPathMatching("/datanode/chunks/.*")));
        wireMockServer2.verify(postRequestedFor(urlPathMatching("/datanode/chunks/.*")));
    }
}
