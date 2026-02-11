package com.example.sentiment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SentimentController.class)
class SentimentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void analyzeSentiment_ShouldReturnResult() throws Exception {
        SentimentResult mockResult = new SentimentResult(2, "POSITIVE", 0.5);
        when(sentimentAnalysisService.analyze(anyString())).thenReturn(mockResult);

        SentimentRequest request = new SentimentRequest();
        request.setText("I am happy");

        mockMvc.perform(post("/api/sentiment/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sentiment").value("POSITIVE"))
                .andExpect(jsonPath("$.score").value(2))
                .andExpect(jsonPath("$.confidence").value(0.5));
    }
}
