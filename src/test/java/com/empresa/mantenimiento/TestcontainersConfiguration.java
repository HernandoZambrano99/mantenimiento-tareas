package com.empresa.mantenimiento;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Spins up an ephemeral PostgreSQL container for integration tests.
 *
 * <p>The {@link ServiceConnection} annotation makes Spring Boot auto-wire the
 * datasource (URL, username, password) from the running container, overriding
 * the {@code spring.datasource.*} defaults in {@code application-test.properties}.
 * Flyway then migrates against this throwaway database, so {@code mvn verify}
 * works on any machine with Docker — no external PostgreSQL required.
 */
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));
    }
}
