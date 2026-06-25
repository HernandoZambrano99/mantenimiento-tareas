package com.empresa.mantenimiento.application.handler;

import com.empresa.mantenimiento.domain.model.user.User;

public interface AuthHandler {
    User authenticate(String username, String rawPassword);

    User validateActiveUser(String username);
}
