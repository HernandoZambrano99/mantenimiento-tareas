package com.empresa.mantenimiento.application.handler.adapter;

import com.empresa.mantenimiento.application.handler.AuthHandler;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.usecase.input.AuthInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthHandlerAdapter implements AuthHandler {
    private final AuthInputPort authInputPort;

    @Override
    @Transactional
    public User authenticate(String username, String rawPassword) {
        return authInputPort.authenticate(username, rawPassword);
    }

    @Override
    public User validateActiveUser(String username) {
        return authInputPort.validateActiveUser(username);
    }
}
