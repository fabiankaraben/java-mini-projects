package com.example.syntaxhighlighter.controller;

import com.example.syntaxhighlighter.service.SyntaxHighlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SyntaxHighlightController.class)
class SyntaxHighlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SyntaxHighlightService syntaxHighlightService;

    @Test
    void testHighlightEndpoint() throws Exception {
        String expectedHtml = "<pre>Highlighted Code</pre>";
        when(syntaxHighlightService.highlight(anyString(), anyString())).thenReturn(expectedHtml);

        String jsonRequest = "{\"code\": \"print('hello')\", \"language\": \"python\"}";

        mockMvc.perform(post("/api/highlight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedHtml));
    }
}
