package com.example.csvparser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CsvControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uploadCsv_ShouldReturnJson_WhenFileIsValid() throws Exception {
        String csvContent = "product,price,quantity\nLaptop,1000,5\nMouse,20,50";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "products.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/csv/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].product", is("Laptop")))
                .andExpect(jsonPath("$[0].price", is("1000")))
                .andExpect(jsonPath("$[0].quantity", is("5")))
                .andExpect(jsonPath("$[1].product", is("Mouse")));
    }

    @Test
    void uploadCsv_ShouldReturnBadRequest_WhenFileIsEmpty() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0]
        );

        mockMvc.perform(multipart("/api/csv/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Please upload a CSV file!")));
    }
    
    @Test
    void uploadCsv_ShouldReturnBadRequest_WhenNotCsv() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                "fake-content".getBytes()
        );

        mockMvc.perform(multipart("/api/csv/upload").file(file))
                .andExpect(status().isBadRequest());
    }
}
