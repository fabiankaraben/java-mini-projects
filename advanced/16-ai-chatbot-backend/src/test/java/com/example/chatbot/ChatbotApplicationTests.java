package com.example.chatbot;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChatbotApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("langchain4j.open-ai.chat-model.base-url", wireMockServer::baseUrl);
        registry.add("langchain4j.open-ai.chat-model.api-key", () -> "test-api-key");
    }

    @Test
    void testChatEndpoint() throws Exception {
        // Mock OpenAI API response
        wireMockServer.stubFor(WireMock.post(urlPathEqualTo("/chat/completions"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "id": "chatcmpl-123",
                                  "object": "chat.completion",
                                  "created": 1677652288,
                                  "model": "gpt-3.5-turbo-0613",
                                  "choices": [{
                                    "index": 0,
                                    "message": {
                                      "role": "assistant",
                                      "content": "Hello! How can I help you today?"
                                    },
                                    "finish_reason": "stop"
                                  }],
                                  "usage": {
                                    "prompt_tokens": 9,
                                    "completion_tokens": 12,
                                    "total_tokens": 21
                                  }
                                }
                                """)));

        // Call the chat endpoint
        mockMvc.perform(post("/api/chat")
                        .content("Hello")
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello! How can I help you today?"));

        // Verify that the request sent to the LLM provider contains the user message
        wireMockServer.verify(postRequestedFor(urlPathEqualTo("/chat/completions"))
                .withRequestBody(containing("Hello")));
    }
}
