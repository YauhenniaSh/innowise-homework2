package com.example.usermanagement.controller;

import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Integration Test");
        request.setUsername("integrationuser" + System.currentTimeMillis());
        request.setEmail("integration" + System.currentTimeMillis() + "@test.com");
        request.setPassword("TestPassword123!");
        request.setPhone("123-456-7890");
        request.setWebsite("https://example.com");
        // Set address and company if required

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("integration@test.com"); // Use a valid email from DB or register first
        request.setPassword("TestPassword123!");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
} 