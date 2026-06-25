package com.empresa.mantenimiento.infrastructure.entrypoint.dto.request;

import com.empresa.mantenimiento.domain.constant.UserMessages;
import com.empresa.mantenimiento.domain.model.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = UserMessages.FIRST_NAME_REQUIRED)
        @Size(max = UserMessages.FIRST_NAME_MAX_LENGTH, message = UserMessages.FIRST_NAME_MAX_LENGTH_MESSAGE)
        String firstName,

        @NotBlank(message = UserMessages.LAST_NAME_REQUIRED)
        @Size(max = UserMessages.LAST_NAME_MAX_LENGTH, message = UserMessages.LAST_NAME_MAX_LENGTH_MESSAGE)
        String lastName,

        @NotBlank(message = UserMessages.EMAIL_REQUIRED)
        @Email(message = UserMessages.EMAIL_INVALID)
        @Size(max = UserMessages.EMAIL_MAX_LENGTH, message = UserMessages.EMAIL_MAX_LENGTH_MESSAGE)
        String email,

        @NotBlank(message = UserMessages.USERNAME_REQUIRED)
        @Size(min = UserMessages.USERNAME_MIN_LENGTH, message = UserMessages.USERNAME_MIN_LENGTH_MESSAGE)
        @Size(max = UserMessages.USERNAME_MAX_LENGTH, message = UserMessages.USERNAME_MAX_LENGTH_MESSAGE)
        String username,

        @NotBlank(message = UserMessages.PASSWORD_REQUIRED)
        @Size(min = UserMessages.PASSWORD_MIN_LENGTH, message = UserMessages.PASSWORD_MIN_LENGTH_MESSAGE)
        @Size(max = UserMessages.PASSWORD_MAX_LENGTH, message = UserMessages.PASSWORD_MAX_LENGTH_MESSAGE)
        String password,

        @NotNull(message = UserMessages.ROLE_REQUIRED)
        Role role
) {
}
