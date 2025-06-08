package com.example.usermanagement.service;

import com.example.usermanagement.dto.AuthResponse;
import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.dto.RegisterRequest;
import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.model.Address;
import com.example.usermanagement.model.Auth;
import com.example.usermanagement.model.Company;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.AuthRepository;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .address(mapAddress(request))
                .company(mapCompany(request))
                .build();

        user = userRepository.save(user);

        Auth auth = Auth.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .user(user)
                .build();

        authRepository.save(auth);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);
        UserDto userDto = userService.mapToDto(user);

        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto userDto = userService.mapToDto(user);

        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }

    private Address mapAddress(RegisterRequest request) {
        if (request.getAddress() == null) {
            return null;
        }

        return Address.builder()
                .street(request.getAddress().getStreet())
                .suite(request.getAddress().getSuite())
                .city(request.getAddress().getCity())
                .zipcode(request.getAddress().getZipcode())
                .geo(request.getAddress().getGeo() != null ? 
                        com.example.usermanagement.model.Geo.builder()
                                .lat(request.getAddress().getGeo().getLat())
                                .lng(request.getAddress().getGeo().getLng())
                                .build() : null)
                .build();
    }

    private Company mapCompany(RegisterRequest request) {
        if (request.getCompany() == null) {
            return null;
        }

        return Company.builder()
                .name(request.getCompany().getName())
                .catchPhrase(request.getCompany().getCatchPhrase())
                .bs(request.getCompany().getBs())
                .build();
    }
} 