package com.empresa.mantenimiento.infrastructure.entrypoint.dto.request;

import com.empresa.mantenimiento.domain.constant.TaskMessages;
import com.empresa.mantenimiento.domain.model.task.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank(message = TaskMessages.TITLE_REQUIRED)
        @Size(max = TaskMessages.TITLE_MAX_LENGTH, message = TaskMessages.TITLE_MAX_LENGTH_MESSAGE)
        String title,

        @Size(max = TaskMessages.DESCRIPTION_MAX_LENGTH, message = TaskMessages.DESCRIPTION_MAX_LENGTH_MESSAGE)
        String description,

        @NotNull(message = TaskMessages.DUE_DATE_REQUIRED)
        @FutureOrPresent(message = TaskMessages.DUE_DATE_NOT_PAST)
        LocalDate dueDate,

        @Positive(message = TaskMessages.ASSIGNEE_REQUIRED_POSITIVE)
        Long assignedToId,

        TaskStatus status
) {
}
