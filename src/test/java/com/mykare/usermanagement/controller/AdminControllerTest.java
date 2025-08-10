package com.mykare.usermanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AdminControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@mykare.com", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "admin@mykare.com", roles = {"ADMIN"})
    void testDeleteUserCannotDeleteSelf() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(delete("/admin/users/admin@mykare.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot delete the currently authenticated admin"));
    }

    @Test
    @WithMockUser(username = "admin@mykare.com", roles = {"ADMIN"})
    void testUpdateUserRoleCannotChangeSelf() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        String json = "{\"role\": \"USER\"}";

        mockMvc.perform(put("/admin/users/admin@mykare.com/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot change your own role"));
    }

}
