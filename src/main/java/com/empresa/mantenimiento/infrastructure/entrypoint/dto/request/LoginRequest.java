package com.empresa.mantenimiento.infrastructure.entrypoint.dto.request;

import com.empresa.mantenimiento.domain.constant.UserMessages;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = UserMessages.USERNAME_REQUIRED)
        String username,

        @NotBlank(message = UserMessages.PASSWORD_REQUIRED)
        String password
) {
}
