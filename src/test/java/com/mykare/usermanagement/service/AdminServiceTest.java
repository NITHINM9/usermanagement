package com.mykare.usermanagement.service;

import com.mykare.usermanagement.model.Role;
import com.mykare.usermanagement.model.User;
import com.mykare.usermanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;


    @Test
    void testGetAllUsers() {
        User user = User.builder()
                .name("M Nithin")
                .email("manikkannithin@gmail.com")
                .gender("Male")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        List<?> users = adminService.getAllUsers();
        assertFalse(users.isEmpty());
    }

    @Test
    void testDeleteUserByEmail() {
        User user = User.builder()
                .name("Delete Me")
                .email("manikkannithin@gmail.com")
                .gender("Male")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        adminService.deleteUserByEmail("manikkannithin@gmail.com");
        assertFalse(userRepository.existsByEmail("manikkannithin@gmail.com"));
    }

    @Test
    void testGetUserById() {
        User user = User.builder()
                .name("ID User")
                .email("manikkannithin@gmail.com")
                .gender("Male")
                .role(Role.USER)
                .build();
        User saved = userRepository.save(user);

        var result = adminService.getUserById(saved.getId());
        assertEquals("manikkannithin@gmail.com", result.getEmail());
    }

    @Test
    void testGetUserByEmail() {
        User user = User.builder()
                .name("mykare")
                .email("mnithinsavi@gmail.com")
                .gender("Male")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        var result = adminService.getUserByEmail("mnithinsavi@gmail.com");
        assertEquals("mykare", result.getName());
    }

    @Test
    void testUpdateUserRole() {
        User user = User.builder()
                .name("Admin Mykare")
                .email("admin@gmail.com")
                .gender("Male")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        adminService.updateUserRole("admin@gmail.com", Role.ADMIN);

        assertEquals(Role.ADMIN, userRepository.findByEmail("admin@gmail.com").get().getRole());
    }
}
