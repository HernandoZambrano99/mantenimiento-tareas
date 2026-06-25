package com.empresa.mantenimiento.infrastructure.entrypoint.dto.request;

import com.empresa.mantenimiento.domain.constant.UserMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = UserMessages.PASSWORD_REQUIRED)
        @Size(min = UserMessages.PASSWORD_MIN_LENGTH, message = UserMessages.PASSWORD_MIN_LENGTH_MESSAGE)
        @Size(max = UserMessages.PASSWORD_MAX_LENGTH, message = UserMessages.PASSWORD_MAX_LENGTH_MESSAGE)
        String newPassword
) {
}
