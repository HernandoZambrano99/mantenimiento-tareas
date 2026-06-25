package com.empresa.mantenimiento.infrastructure.entrypoint.rest.security;

import com.empresa.mantenimiento.domain.constant.UserMessages;
import com.empresa.mantenimiento.domain.exception.DomainUnauthorizedException;
import com.empresa.mantenimiento.domain.model.task.TaskActor;
import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Bridges Spring Security and the domain: resolves the authenticated principal
 * (the JWT username) into a {@link TaskActor} carrying the user's id and role.
 * Keeps controllers and the domain free of Spring Security types.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserProvider {
    private final UserOutputPort userOutputPort;

    public TaskActor currentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new DomainUnauthorizedException(UserMessages.TOKEN_INVALID);
        }
        User user = userOutputPort.findByUsername(authentication.getName())
                .orElseThrow(() -> new DomainUnauthorizedException(UserMessages.TOKEN_INVALID));
        return new TaskActor(user.getId(), user.getRole());
    }
}
