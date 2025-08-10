package com.mykare.usermanagement.service;

import com.mykare.usermanagement.dto.LoginRequest;
import com.mykare.usermanagement.dto.RegisterRequest;
import com.mykare.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .gender("Male")
                .password("pass123")
                .build();

        String result = authService.registerUser(request);
        assertEquals("User registered successfully", result);
        assertTrue(userRepository.existsByEmail("test@example.com"));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .email("dup@example.com")
                .gender("Male")
                .password("pass123")
                .build();

        authService.registerUser(request);
        assertThrows(RuntimeException.class, () -> authService.registerUser(request));
    }

    @Test
    void testLogin_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Login User")
                .email("login@example.com")
                .gender("Male")
                .password("secret")
                .build();
        authService.registerUser(request);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("login@example.com")
                .password("secret")
                .build();

        var response = authService.login(loginRequest);
        assertEquals("Login successful", response.getMessage());
        assertEquals("login@example.com", response.getEmail());
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("noone@example.com")
                .password("wrong")
                .build();

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}
