package com.shopsphere.authservice.dto;

import com.shopsphere.authservice.entity.Role;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role
) {
}
