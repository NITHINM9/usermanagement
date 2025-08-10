package com.mykare.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mykare.usermanagement.dto.LoginRequest;
import com.mykare.usermanagement.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser_Success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("manikkannithin@gmail.com");
        registerRequest.setPassword("Password@123");
        registerRequest.setName("Test User");
        registerRequest.setGender("Male");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("success")));
    }

    @Test
    void testRegisterUser_PasswordValidationFail() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("manikkannithin@gmail.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("Test User");
        registerRequest.setGender("Male");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_DuplicateEntry() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("manikkannithin@gmail.com");
        registerRequest.setPassword("Password@123");
        registerRequest.setName("Test User");
        registerRequest.setGender("Male");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testRegisterUser_InvalidData() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("");
        registerRequest.setPassword("");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("manikkannithin@gmail.com");
        loginRequest.setPassword("Password@123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.email").value("manikkannithin@gmail.com"));
    }

    @Test
    void testLoginUser_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("Wrong@password1");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
