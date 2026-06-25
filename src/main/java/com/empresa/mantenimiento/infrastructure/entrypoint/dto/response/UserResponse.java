package com.empresa.mantenimiento.infrastructure.entrypoint.dto.response;

import com.empresa.mantenimiento.domain.model.user.Role;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        Role role,
        Boolean active
) {
}
