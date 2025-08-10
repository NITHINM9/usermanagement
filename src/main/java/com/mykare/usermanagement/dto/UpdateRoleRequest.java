package com.mykare.usermanagement.dto;

import com.mykare.usermanagement.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    private Role role;
}
