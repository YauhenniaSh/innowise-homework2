package com.example.usermanagement.controller;

import com.example.usermanagement.dto.AuthResponse;
import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.dto.RegisterRequest;
import com.example.usermanagement.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerUnitTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = new AuthResponse("token", null);
        when(authService.register(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.register(request);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("token", result.getBody().getToken());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        AuthResponse response = new AuthResponse("token", null);
        when(authService.login(request)).thenReturn(response);

        ResponseEntity<AuthResponse> result = authController.login(request);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("token", result.getBody().getToken());
        verify(authService, times(1)).login(request);
    }
} 