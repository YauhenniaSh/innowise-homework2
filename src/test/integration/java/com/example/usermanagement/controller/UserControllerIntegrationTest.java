package com.example.usermanagement.controller;

import com.example.usermanagement.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() throws Exception {
        // Register and login to get JWT token
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

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        AuthResponse authResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthResponse.class
        );
        jwtToken = authResponse.getToken();
        testUserDto = authResponse.getUser();
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/" + testUserDto.getId())
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserDto.getId()))
                .andExpect(jsonPath("$.email").value(testUserDto.getEmail()));
    }

    @Test
    void testUpdateUser() throws Exception {
        testUserDto.setName("Updated Name");
        testUserDto.setPhone("987-654-3210");

        mockMvc.perform(put("/api/users/" + testUserDto.getId())
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.phone").value("987-654-3210"));
    }

    @Test
    void testDeleteUser() throws Exception {
        // First delete the user
        mockMvc.perform(delete("/api/users/" + testUserDto.getId())
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // Verify user is deleted by attempting to access it
        mockMvc.perform(get("/api/users/" + testUserDto.getId())
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }
} 