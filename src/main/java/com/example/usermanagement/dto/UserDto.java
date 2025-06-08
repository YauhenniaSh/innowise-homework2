package com.example.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private AddressDto address;
    private CompanyDto company;
} 