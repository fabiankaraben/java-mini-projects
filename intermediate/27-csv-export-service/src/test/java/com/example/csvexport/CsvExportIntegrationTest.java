package com.example.csvexport;

import com.example.csvexport.model.User;
import com.example.csvexport.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class CsvExportIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(new User("john_doe", "john@example.com", "USER", true));
        userRepository.save(new User("jane_admin", "jane@example.com", "ADMIN", true));
    }

    @Test
    void shouldExportUsersToCsv() throws Exception {
        mockMvc.perform(get("/api/export/users"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv; charset=UTF-8"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"users.csv\""))
                .andExpect(content().string(containsString("ID,Username,Email,Role,Active")))
                .andExpect(content().string(containsString("john_doe,john@example.com,USER,true")))
                .andExpect(content().string(containsString("jane_admin,jane@example.com,ADMIN,true")));
    }
}
