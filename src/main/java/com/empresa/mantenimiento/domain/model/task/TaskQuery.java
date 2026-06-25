package com.empresa.mantenimiento.domain.model.task;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Spring-free description of a task listing request: filters, sorting and
 * pagination. {@code restrictToUserId} is set by the use case to limit a USER
 * to tasks they created or that are assigned to them; it stays null for ADMIN.
 */
@Builder(toBuilder = true)
public record TaskQuery(
        TaskStatus status,
        Long assignedToId,
        Long createdById,
        LocalDate dueDateFrom,
        LocalDate dueDateTo,
        String search,
        String sortBy,
        String sortDirection,
        Integer page,
        Integer size,
        Long restrictToUserId
) {
}
