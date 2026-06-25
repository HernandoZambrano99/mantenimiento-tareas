package com.empresa.mantenimiento.infrastructure.entrypoint.dto.response;

import com.empresa.mantenimiento.domain.model.task.TaskStatus;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        Long createdById,
        Long assignedToId
) {
}
