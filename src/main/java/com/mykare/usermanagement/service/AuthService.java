package com.mykare.usermanagement.service;

import com.mykare.usermanagement.dto.*;
import com.mykare.usermanagement.model.*;
import com.mykare.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for handling user authentication and registration.
 * This includes user registration, fetching IP and country information,
 * and user login functionalities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;

    /**
     * Registers a new user in the system.
     * This method encrypts the user's password, fetches their IP address and country,
     * and saves the user details to the database with a default role of USER.
     *
     * @param request The {@link RegisterRequest} object containing user registration details.
     * @return A success message indicating that the user was registered.
     * @throws RuntimeException if the email is already registered.
     *
     */
    public String registerUser(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already registered: {}", request.getEmail());
            throw new RuntimeException("Email already registered");
        }

        String ipAddress = "UNKNOWN";
        try {
            log.info("Attempting to fetch IP address for user registration.");
            String ipResponse = restTemplate.getForObject("https://api.ipify.org?format=json", String.class);
            if (ipResponse != null && ipResponse.contains("\"ip\"")) {
                ipAddress = ipResponse.replaceAll("[^0-9\\.]", "");
                log.info("Fetched IP address: {}", ipAddress);
            }
        } catch (Exception ex) {
            log.error("Error fetching IP address during registration for email {}: {}", request.getEmail(), ex.getMessage());
            ipAddress = "UNKNOWN";
        }

        String country = "UNKNOWN";
        try {
            log.info("Attempting to fetch country for IP: {}", ipAddress);
            String countryResponse = restTemplate.getForObject("http://ip-api.com/json/" + ipAddress, String.class);
            if (countryResponse != null && countryResponse.contains("\"country\"")) {
                country = countryResponse.split("\"country\"\\s*:\\s*\"")[1].split("\"")[0];
                log.info("Fetched country: {}", country);
            }
            else {
                log.warn("Could not parse country from response for IP {}: {}", ipAddress, countryResponse);
            }
        } catch (Exception ex) {
            log.error("Error fetching country for IP {} during registration for email {}: {}", ipAddress, request.getEmail(), ex.getMessage());
            country = "UNKNOWN";
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(hashedPassword)
                .ipAddress(ipAddress)
                .country(country)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    /**
     * Authenticates a user based on their email and password.
     * If authentication is successful, the user's authentication details are
     * stored in the Spring Security context.
     *
     * @param request The {@link LoginRequest} object containing user login credentials.
     * @return A {@link LoginResponse} indicating successful login, including the user's email.
     * @throws RuntimeException if authentication fails due to bad credentials (invalid email or password)
     *
     */
    public LoginResponse login(LoginRequest request) {
        log.info("Attempting to log in user with email: {}", request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            return LoginResponse.builder()
                    .message("Login successful")
                    .email(request.getEmail())
                    .build();
        }
        catch (BadCredentialsException | DisabledException ex) {
            log.warn("Login failed for user {}: {}", request.getEmail(), ex.getMessage());
            throw new RuntimeException("Invalid email or password");
        }
        catch (Exception ex) {
            log.error("An unexpected error occurred during login for user {}: {}", request.getEmail(), ex.getMessage(), ex);
            throw new RuntimeException("An unexpected error occurred during login");
        }
    }
}
