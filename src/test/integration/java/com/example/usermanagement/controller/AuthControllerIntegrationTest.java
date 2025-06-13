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

        // AddressDto and GeoDto
        GeoDto geoDto = GeoDto.builder()
            .lat("40.7128")
            .lng("-74.0060")
            .build();
        AddressDto addressDto = AddressDto.builder()
            .street("123 Main St")
            .suite("Apt 1")
            .city("Testville")
            .zipcode("12345")
            .geo(geoDto)
            .build();
        request.setAddress(addressDto);

        // CompanyDto
        CompanyDto companyDto = CompanyDto.builder()
            .name("TestCorp")
            .catchPhrase("Innovate your world")
            .bs("synergy")
            .build();
        request.setCompany(companyDto);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLogin() throws Exception {
        // 1. Register the user first
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Integration Test");
        registerRequest.setUsername("integrationuser" + System.currentTimeMillis());
        String email = "integration" + System.currentTimeMillis() + "@test.com";
        registerRequest.setEmail(email);
        registerRequest.setPassword("TestPassword123!");
        registerRequest.setPhone("123-456-7890");
        registerRequest.setWebsite("https://example.com");

        // AddressDto and GeoDto
        GeoDto geoDto = GeoDto.builder()
            .lat("40.7128")
            .lng("-74.0060")
            .build();
        AddressDto addressDto = AddressDto.builder()
            .street("123 Main St")
            .suite("Apt 1")
            .city("Testville")
            .zipcode("12345")
            .geo(geoDto)
            .build();
        registerRequest.setAddress(addressDto);

        // CompanyDto
        CompanyDto companyDto = CompanyDto.builder()
            .name("TestCorp")
            .catchPhrase("Innovate your world")
            .bs("synergy")
            .build();
        registerRequest.setCompany(companyDto);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // 2. Now login with the same credentials
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("TestPassword123!");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
} 