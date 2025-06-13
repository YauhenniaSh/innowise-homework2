package com.example.usermanagement.controller;

import com.example.usermanagement.dto.AddressDto;
import com.example.usermanagement.dto.CompanyDto;
import com.example.usermanagement.dto.GeoDto;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto testUserDto;
    private List<UserDto> testUserList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create test data
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

        CompanyDto companyDto = CompanyDto.builder()
                .name("TestCorp")
                .catchPhrase("Innovate your world")
                .bs("synergy")
                .build();

        testUserDto = UserDto.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .email("test@example.com")
                .phone("123-456-7890")
                .website("https://example.com")
                .address(addressDto)
                .company(companyDto)
                .build();

        testUserList = Arrays.asList(testUserDto);
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(testUserList);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUserList, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(testUserDto);

        ResponseEntity<UserDto> response = userController.getUserById(1L);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUserDto, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(1L, testUserDto)).thenReturn(testUserDto);

        ResponseEntity<UserDto> response = userController.updateUser(1L, testUserDto);
        
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testUserDto, response.getBody());
        verify(userService, times(1)).updateUser(1L, testUserDto);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);
        
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }
} 