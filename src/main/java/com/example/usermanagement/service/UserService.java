package com.example.usermanagement.service;

import com.example.usermanagement.dto.AddressDto;
import com.example.usermanagement.dto.CompanyDto;
import com.example.usermanagement.dto.GeoDto;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setWebsite(userDto.getWebsite());

        if (userDto.getAddress() != null) {
            user.getAddress().setStreet(userDto.getAddress().getStreet());
            user.getAddress().setSuite(userDto.getAddress().getSuite());
            user.getAddress().setCity(userDto.getAddress().getCity());
            user.getAddress().setZipcode(userDto.getAddress().getZipcode());
            
            if (userDto.getAddress().getGeo() != null) {
                user.getAddress().getGeo().setLat(userDto.getAddress().getGeo().getLat());
                user.getAddress().getGeo().setLng(userDto.getAddress().getGeo().getLng());
            }
        }

        if (userDto.getCompany() != null) {
            user.getCompany().setName(userDto.getCompany().getName());
            user.getCompany().setCatchPhrase(userDto.getCompany().getCatchPhrase());
            user.getCompany().setBs(userDto.getCompany().getBs());
        }

        user = userRepository.save(user);
        return mapToDto(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Delete the auth record first
        authRepository.deleteByUserId(id);
        
        // Then delete the user
        userRepository.delete(user);
    }

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .website(user.getWebsite())
                .address(user.getAddress() != null ? mapAddressToDto(user.getAddress()) : null)
                .company(user.getCompany() != null ? mapCompanyToDto(user.getCompany()) : null)
                .build();
    }

    private AddressDto mapAddressToDto(com.example.usermanagement.model.Address address) {
        return AddressDto.builder()
                .street(address.getStreet())
                .suite(address.getSuite())
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .geo(address.getGeo() != null ? mapGeoToDto(address.getGeo()) : null)
                .build();
    }

    private GeoDto mapGeoToDto(com.example.usermanagement.model.Geo geo) {
        return GeoDto.builder()
                .lat(geo.getLat())
                .lng(geo.getLng())
                .build();
    }

    private CompanyDto mapCompanyToDto(com.example.usermanagement.model.Company company) {
        return CompanyDto.builder()
                .name(company.getName())
                .catchPhrase(company.getCatchPhrase())
                .bs(company.getBs())
                .build();
    }
} 