package com.mykare.usermanagement.dto;

import com.mykare.usermanagement.model.Role;
import com.mykare.usermanagement.model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private String ipAddress;
    private String country;
    private Role role;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .gender(user.getGender())
                .country(user.getCountry())
                .ipAddress(user.getIpAddress())
                .build();
    }
}
