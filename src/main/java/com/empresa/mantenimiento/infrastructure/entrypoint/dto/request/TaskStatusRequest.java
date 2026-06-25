package com.empresa.mantenimiento.infrastructure.entrypoint.dto.request;

import com.empresa.mantenimiento.domain.constant.TaskMessages;
import com.empresa.mantenimiento.domain.model.task.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusRequest(
        @NotNull(message = TaskMessages.STATUS_REQUIRED)
        TaskStatus status
) {
}
