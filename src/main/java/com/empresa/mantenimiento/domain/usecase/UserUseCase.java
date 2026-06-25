package com.empresa.mantenimiento.domain.usecase;

import com.empresa.mantenimiento.domain.constant.UserMessages;
import com.empresa.mantenimiento.domain.exception.DomainConflictException;
import com.empresa.mantenimiento.domain.exception.DomainNotFoundException;
import com.empresa.mantenimiento.domain.exception.DomainValidationException;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.model.user.gateway.PasswordEncoderPort;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import com.empresa.mantenimiento.domain.usecase.input.UserInputPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements UserInputPort {
    private final UserOutputPort outputPort;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    public User create(User user) {
        validateRequired(user);
        validateUniqueness(user.getEmail(), user.getUsername());

        User toCreate = User.builder()
                .firstName(user.getFirstName().trim())
                .lastName(user.getLastName().trim())
                .email(user.getEmail().trim().toLowerCase())
                .username(user.getUsername().trim().toLowerCase())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(user.getRole())
                .active(true)
                .failedAttempts(0)
                .build();
        return outputPort.save(toCreate);
    }

    @Override
    public User update(Long id, User user) {
        Long userId = requireId(id);
        User existing = outputPort.findById(userId)
                .orElseThrow(() -> new DomainNotFoundException(UserMessages.NOT_FOUND));

        if (user.getEmail() != null
                && !user.getEmail().trim().equalsIgnoreCase(existing.getEmail())
                && outputPort.existsByEmail(user.getEmail().trim().toLowerCase())) {
            throw new DomainConflictException(UserMessages.EMAIL_ALREADY_EXISTS);
        }
        if (user.getUsername() != null
                && !user.getUsername().trim().equalsIgnoreCase(existing.getUsername())
                && outputPort.existsByUsername(user.getUsername().trim().toLowerCase())) {
            throw new DomainConflictException(UserMessages.USERNAME_ALREADY_EXISTS);
        }

        User toUpdate = User.builder()
                .id(userId)
                .firstName(user.getFirstName() != null ? user.getFirstName().trim() : existing.getFirstName())
                .lastName(user.getLastName() != null ? user.getLastName().trim() : existing.getLastName())
                .email(user.getEmail() != null ? user.getEmail().trim().toLowerCase() : existing.getEmail())
                .username(user.getUsername() != null ? user.getUsername().trim().toLowerCase() : existing.getUsername())
                .password(existing.getPassword())
                .role(user.getRole() != null ? user.getRole() : existing.getRole())
                .active(existing.getActive())
                .failedAttempts(existing.getFailedAttempts())
                .lockedUntil(existing.getLockedUntil())
                .build();
        return outputPort.save(toUpdate);
    }

    @Override
    public User findById(Long id) {
        Long userId = requireId(id);
        return outputPort.findById(userId)
                .orElseThrow(() -> new DomainNotFoundException(UserMessages.NOT_FOUND));
    }

    @Override
    public List<User> findAll() {
        return outputPort.findAll();
    }

    @Override
    public void toggleActive(Long id) {
        Long userId = requireId(id);
        User existing = outputPort.findById(userId)
                .orElseThrow(() -> new DomainNotFoundException(UserMessages.NOT_FOUND));
        User toggled = User.builder()
                .id(existing.getId())
                .firstName(existing.getFirstName())
                .lastName(existing.getLastName())
                .email(existing.getEmail())
                .username(existing.getUsername())
                .password(existing.getPassword())
                .role(existing.getRole())
                .active(!existing.getActive())
                .failedAttempts(existing.getFailedAttempts())
                .lockedUntil(existing.getLockedUntil())
                .build();
        outputPort.save(toggled);
    }

    @Override
    public void delete(Long id) {
        Long userId = requireId(id);
        if (!outputPort.existsById(userId)) {
            throw new DomainNotFoundException(UserMessages.NOT_FOUND);
        }
        outputPort.deleteById(userId);
    }

    @Override
    public void changePassword(Long id, String currentPassword, String newPassword) {
        Long userId = requireId(id);
        User existing = outputPort.findById(userId)
                .orElseThrow(() -> new DomainNotFoundException(UserMessages.NOT_FOUND));

        if (!passwordEncoder.matches(currentPassword, existing.getPassword())) {
            throw new DomainValidationException(UserMessages.CURRENT_PASSWORD_INCORRECT);
        }

        User updated = User.builder()
                .id(existing.getId())
                .firstName(existing.getFirstName())
                .lastName(existing.getLastName())
                .email(existing.getEmail())
                .username(existing.getUsername())
                .password(passwordEncoder.encode(newPassword))
                .role(existing.getRole())
                .active(existing.getActive())
                .failedAttempts(existing.getFailedAttempts())
                .lockedUntil(existing.getLockedUntil())
                .build();
        outputPort.save(updated);
    }

    private void validateRequired(User user) {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new DomainValidationException(UserMessages.FIRST_NAME_REQUIRED);
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new DomainValidationException(UserMessages.LAST_NAME_REQUIRED);
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new DomainValidationException(UserMessages.EMAIL_REQUIRED);
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new DomainValidationException(UserMessages.USERNAME_REQUIRED);
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new DomainValidationException(UserMessages.PASSWORD_REQUIRED);
        }
        if (user.getRole() == null) {
            throw new DomainValidationException(UserMessages.ROLE_REQUIRED);
        }
    }

    private void validateUniqueness(String email, String username) {
        if (outputPort.existsByEmail(email.trim().toLowerCase())) {
            throw new DomainConflictException(UserMessages.EMAIL_ALREADY_EXISTS);
        }
        if (outputPort.existsByUsername(username.trim().toLowerCase())) {
            throw new DomainConflictException(UserMessages.USERNAME_ALREADY_EXISTS);
        }
    }

    private Long requireId(Long id) {
        if (id == null) {
            throw new DomainValidationException(UserMessages.ID_REQUIRED);
        }
        return id;
    }
}
