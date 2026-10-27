package com.shopsphere.authservice.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        UserResponse user
) {
}
