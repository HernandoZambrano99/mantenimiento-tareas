package com.empresa.mantenimiento.domain.usecase;

import com.empresa.mantenimiento.domain.constant.UserMessages;
import com.empresa.mantenimiento.domain.exception.DomainUnauthorizedException;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.model.user.gateway.PasswordEncoderPort;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import com.empresa.mantenimiento.domain.usecase.input.AuthInputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Authentication business rules: credential verification, failed-attempt
 * tracking and temporary account lockout.
 *
 * <p>Kept free of Spring annotations so it can be unit-tested without an
 * application context. Token issuing (JWT) is deliberately left out — that is an
 * infrastructure concern handled by the entrypoint layer.
 */
@RequiredArgsConstructor
public class AuthUseCase implements AuthInputPort {
    private final UserOutputPort outputPort;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    public User authenticate(String username, String rawPassword) {
        User user = outputPort.findByUsername(normalize(username))
                .orElseThrow(() -> new DomainUnauthorizedException(UserMessages.INVALID_CREDENTIALS));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new DomainUnauthorizedException(UserMessages.ACCOUNT_DISABLED);
        }
        if (isLocked(user)) {
            throw new DomainUnauthorizedException(UserMessages.ACCOUNT_LOCKED);
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            registerFailedAttempt(user);
            throw new DomainUnauthorizedException(UserMessages.INVALID_CREDENTIALS);
        }

        return clearFailedAttempts(user);
    }

    @Override
    public User validateActiveUser(String username) {
        User user = outputPort.findByUsername(normalize(username))
                .orElseThrow(() -> new DomainUnauthorizedException(UserMessages.TOKEN_INVALID));
        if (Boolean.FALSE.equals(user.getActive())) {
            throw new DomainUnauthorizedException(UserMessages.ACCOUNT_DISABLED);
        }
        return user;
    }

    private boolean isLocked(User user) {
        return user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now());
    }

    private void registerFailedAttempt(User user) {
        int attempts = (user.getFailedAttempts() != null ? user.getFailedAttempts() : 0) + 1;
        User.UserBuilder toSave = user.toBuilder().failedAttempts(attempts);
        if (attempts >= UserMessages.MAX_FAILED_ATTEMPTS) {
            toSave.failedAttempts(0)
                    .lockedUntil(LocalDateTime.now().plusMinutes(UserMessages.LOCK_DURATION_MINUTES));
        }
        outputPort.save(toSave.build());
    }

    private User clearFailedAttempts(User user) {
        boolean dirty = (user.getFailedAttempts() != null && user.getFailedAttempts() > 0)
                || user.getLockedUntil() != null;
        if (!dirty) {
            return user;
        }
        return outputPort.save(user.toBuilder()
                .failedAttempts(0)
                .lockedUntil(null)
                .build());
    }

    private String normalize(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }
}
