package com.empresa.mantenimiento.domain.model.user.gateway;

/**
 * Driven port for password hashing and verification. Implemented in the
 * infrastructure layer (delegating to Spring Security's PasswordEncoder), which
 * keeps the domain use cases free of framework dependencies.
 */
public interface PasswordEncoderPort {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
