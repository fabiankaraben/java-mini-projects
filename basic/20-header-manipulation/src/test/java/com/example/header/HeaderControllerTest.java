package com.example.header;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class HeaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultHeaderWhenNoneProvided() throws Exception {
        mockMvc.perform(get("/api/headers"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Custom-Output", "Processed: default"))
                .andExpect(header().exists("X-Server-Timestamp"))
                .andExpect(jsonPath("$.receivedCustomInput").value("default"));
    }

    @Test
    public void shouldReturnModifiedHeaderWhenProvided() throws Exception {
        String inputHeader = "HelloWorld";
        mockMvc.perform(get("/api/headers")
                .header("X-Custom-Input", inputHeader))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Custom-Output", "Processed: " + inputHeader))
                .andExpect(jsonPath("$.receivedCustomInput").value(inputHeader));
    }
}
