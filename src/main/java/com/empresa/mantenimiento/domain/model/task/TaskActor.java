package com.empresa.mantenimiento.domain.model.task;

import com.empresa.mantenimiento.domain.model.user.Role;

/**
 * The authenticated caller executing a task operation. Carries just what the
 * domain needs to make authorization decisions, keeping the use case free of
 * any Spring Security dependency.
 */
public record TaskActor(Long userId, Role role) {
}
