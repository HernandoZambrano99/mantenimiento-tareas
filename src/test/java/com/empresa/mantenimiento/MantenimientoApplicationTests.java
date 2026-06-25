package com.empresa.mantenimiento;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class MantenimientoApplicationTests {

    @Test
    void contextLoads() {
        // Smoke test: verifies the Spring application context starts.
    }
}
