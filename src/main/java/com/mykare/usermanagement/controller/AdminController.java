package com.mykare.usermanagement.controller;

import com.mykare.usermanagement.dto.UserResponse;
import com.mykare.usermanagement.dto.UpdateRoleRequest;
import com.mykare.usermanagement.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for administrative operations such as managing user accounts.
 * All endpoints in this controller require ADMIN role access.
 */

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "APIs for managing users - accessible only to Admin users")
public class AdminController {

    private final AdminService adminService;

    /**
     * Retrieve a list of all registered users.
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Fetches a list of all registered users. Accessible only to admins.")
    public ResponseEntity<?> getAllUsers() {
        try {
            log.info("Fetching all users...");
            List<UserResponse> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            return ResponseEntity.internalServerError().body("Failed to fetch users");
        }
    }


    /**
     * Delete a user by email address.
     *
     * @param email Email address of the user to delete.
     * @return Success or error message.
     */
    @DeleteMapping("/users/{email}")
    @Operation(summary = "Delete a user by email", description = "Deletes the user with the given email. Admins cannot delete themselves.")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && email.equalsIgnoreCase(auth.getName())) {
                log.warn("Attempt to delete own admin account: {}", email);
                return ResponseEntity.badRequest().body("Cannot delete the currently authenticated admin");
            }

            adminService.deleteUserByEmail(email);
            log.info("User deleted successfully: {}", email);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting user with email {}", email, e);
            return ResponseEntity.internalServerError().body("Failed to delete user");
        }
    }

    /**
     * Retrieve user details by ID.
     *
     * @param id User's unique ID.
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by ID", description = "Fetches the details of a user by their unique ID.")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            log.info("Fetching user by ID: {}", id);
            UserResponse user = adminService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error fetching user with ID {}", id, e);
            return ResponseEntity.internalServerError().body("Failed to fetch user");
        }
    }

    /**
     * Update a user's role.
     *
     * @param email   Email address of the user whose role is to be updated.
     * @param request Request body containing the new role.
     * @return Success or error message.
     */
    @PutMapping("/users/{email}/role")
    @Operation(summary = "Update user role", description = "Updates the role of the specified user. Admins cannot change their own role.")
    public ResponseEntity<?> updateUserRole(
            @PathVariable String email,
            @RequestBody UpdateRoleRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && email.equalsIgnoreCase(auth.getName())) {
                log.warn("Attempt to change own admin role: {}", email);
                return ResponseEntity.badRequest().body("Cannot change your own role");
            }

            adminService.updateUserRole(email, request.getRole());
            log.info("Role updated successfully for user: {}", email);
            return ResponseEntity.ok("Role updated successfully");
        } catch (Exception e) {
            log.error("Error updating role for user {}", email, e);
            return ResponseEntity.internalServerError().body("Failed to update role");
        }
    }

    /**
     * Retrieve user details by email address.
     *
     * @param email Email address of the user.
     * @return {@link UserResponse} containing user details.
     */
    @GetMapping("/users/email/{email}")
    @Operation(summary = "Get user by email", description = "Fetches the details of a user by their email address.")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            log.info("Fetching user by email: {}", email);
            UserResponse user = adminService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error fetching user with email {}", email, e);
            return ResponseEntity.internalServerError().body("Failed to fetch user");
        }
    }
}
