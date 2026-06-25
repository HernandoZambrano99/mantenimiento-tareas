package com.empresa.mantenimiento.infrastructure.config;

import com.empresa.mantenimiento.domain.model.user.gateway.PasswordEncoderPort;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import com.empresa.mantenimiento.domain.usecase.AuthUseCase;
import com.empresa.mantenimiento.domain.usecase.UserUseCase;
import com.empresa.mantenimiento.domain.usecase.input.AuthInputPort;
import com.empresa.mantenimiento.domain.usecase.input.UserInputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Manual wiring of pure-domain use cases as Spring beans.
 *
 * Domain UseCases must stay free of Spring annotations so they remain
 * unit-testable without an application context. Register each new UseCase
 * here with its required ports.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public UserInputPort userInputPort(UserOutputPort userOutputPort,
                                       PasswordEncoderPort passwordEncoderPort) {
        return new UserUseCase(userOutputPort, passwordEncoderPort);
    }

    @Bean
    public AuthInputPort authInputPort(UserOutputPort userOutputPort,
                                       PasswordEncoderPort passwordEncoderPort) {
        return new AuthUseCase(userOutputPort, passwordEncoderPort);
    }
}
