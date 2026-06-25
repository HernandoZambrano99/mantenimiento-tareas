package com.empresa.mantenimiento.infrastructure.config;

import com.empresa.mantenimiento.domain.model.user.Role;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.UserEntity;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Creates a bootstrap admin user on first startup. If ADMIN_PASSWORD is not
 * provided, a random one is generated and logged once so the operator can
 * complete the first login. Configured passwords are never logged.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.username:admin}")
    private String adminUsername;

    @Value("${app.bootstrap.admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${app.bootstrap.admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(adminUsername)) {
            log.info("Admin user already exists, skipping initialization");
            return;
        }

        boolean generated = !StringUtils.hasText(adminPassword);
        String rawPassword = generated ? generateRandomPassword() : adminPassword;

        UserEntity admin = new UserEntity();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail(adminEmail);
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        admin.setFailedAttempts(0);

        userRepository.save(admin);

        if (generated) {
            log.warn("Bootstrap admin user created (username: '{}'). Generated temporary password: '{}'. "
                            + "Change it immediately and set ADMIN_PASSWORD for future runs.",
                    adminUsername, rawPassword);
        } else {
            log.info("Bootstrap admin user created (username: '{}') using the configured password.", adminUsername);
        }
    }

    private String generateRandomPassword() {
        byte[] bytes = new byte[24];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
