package com.empresa.mantenimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MantenimientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MantenimientoApplication.class, args);
    }
}
