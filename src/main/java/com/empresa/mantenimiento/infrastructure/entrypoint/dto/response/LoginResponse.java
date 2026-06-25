package com.empresa.mantenimiento.infrastructure.entrypoint.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String role
) {
}
