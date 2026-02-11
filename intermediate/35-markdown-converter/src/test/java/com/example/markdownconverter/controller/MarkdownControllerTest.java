package com.example.markdownconverter.controller;

import com.example.markdownconverter.service.MarkdownService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarkdownController.class)
class MarkdownControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkdownService markdownService;

    @Test
    void testConvertEndpoint() throws Exception {
        String markdown = "# Hello";
        String expectedHtml = "<h1>Hello</h1>";
        
        when(markdownService.convertToHtml(anyString())).thenReturn(expectedHtml);

        mockMvc.perform(post("/api/markdown/convert")
                .contentType(MediaType.TEXT_PLAIN)
                .content(markdown))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.html").value(expectedHtml));
    }
}
