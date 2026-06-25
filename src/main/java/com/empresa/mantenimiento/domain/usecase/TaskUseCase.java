package com.empresa.mantenimiento.domain.usecase;

import com.empresa.mantenimiento.domain.constant.TaskMessages;
import com.empresa.mantenimiento.domain.exception.DomainForbiddenException;
import com.empresa.mantenimiento.domain.exception.DomainNotFoundException;
import com.empresa.mantenimiento.domain.exception.DomainValidationException;
import com.empresa.mantenimiento.domain.model.common.PageResult;
import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.domain.model.task.TaskActor;
import com.empresa.mantenimiento.domain.model.task.TaskQuery;
import com.empresa.mantenimiento.domain.model.task.TaskStatus;
import com.empresa.mantenimiento.domain.model.task.gateway.TaskOutputPort;
import com.empresa.mantenimiento.domain.model.user.Role;
import com.empresa.mantenimiento.domain.model.user.gateway.UserOutputPort;
import com.empresa.mantenimiento.domain.usecase.input.TaskInputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class TaskUseCase implements TaskInputPort {
    private final TaskOutputPort outputPort;
    private final UserOutputPort userOutputPort;

    @Override
    public Task create(TaskActor actor, Task task) {
        validateRequired(task);
        Long assignee = resolveAssignee(task.getAssignedToId(), actor);

        Task toCreate = Task.builder()
                .title(task.getTitle().trim())
                .description(task.getDescription() != null ? task.getDescription().trim() : null)
                .status(task.getStatus() != null ? task.getStatus() : TaskStatus.TODO)
                .dueDate(task.getDueDate())
                .createdById(actor.userId())
                .assignedToId(assignee)
                .build();
        return outputPort.save(toCreate);
    }

    @Override
    public Task update(TaskActor actor, Long id, Task task) {
        Long taskId = requireId(id);
        Task existing = outputPort.findById(taskId)
                .orElseThrow(() -> new DomainNotFoundException(TaskMessages.NOT_FOUND));
        assertCanModify(actor, existing);
        validateRequired(task);

        Long assignee = task.getAssignedToId() != null
                ? resolveAssignee(task.getAssignedToId(), actor)
                : existing.getAssignedToId();

        Task toUpdate = existing.toBuilder()
                .title(task.getTitle().trim())
                .description(task.getDescription() != null ? task.getDescription().trim() : null)
                .status(task.getStatus() != null ? task.getStatus() : existing.getStatus())
                .dueDate(task.getDueDate())
                .assignedToId(assignee)
                .build();
        return outputPort.save(toUpdate);
    }

    @Override
    public Task changeStatus(TaskActor actor, Long id, TaskStatus status) {
        Long taskId = requireId(id);
        if (status == null) {
            throw new DomainValidationException(TaskMessages.STATUS_REQUIRED);
        }
        Task existing = outputPort.findById(taskId)
                .orElseThrow(() -> new DomainNotFoundException(TaskMessages.NOT_FOUND));
        assertCanModify(actor, existing);
        return outputPort.save(existing.toBuilder().status(status).build());
    }

    @Override
    public Task findById(TaskActor actor, Long id) {
        Long taskId = requireId(id);
        Task existing = outputPort.findById(taskId)
                .orElseThrow(() -> new DomainNotFoundException(TaskMessages.NOT_FOUND));
        assertCanModify(actor, existing);
        return existing;
    }

    @Override
    public PageResult<Task> search(TaskActor actor, TaskQuery query) {
        TaskQuery scoped = actor.role() == Role.USER
                ? query.toBuilder().restrictToUserId(actor.userId()).build()
                : query;
        return outputPort.search(scoped);
    }

    @Override
    public void delete(TaskActor actor, Long id) {
        Long taskId = requireId(id);
        Task existing = outputPort.findById(taskId)
                .orElseThrow(() -> new DomainNotFoundException(TaskMessages.NOT_FOUND));
        assertCanDelete(actor, existing);
        outputPort.deleteById(taskId);
    }

    private Long resolveAssignee(Long assignedToId, TaskActor actor) {
        if (assignedToId == null) {
            return actor.userId();
        }
        if (!userOutputPort.existsById(assignedToId)) {
            throw new DomainValidationException(TaskMessages.ASSIGNEE_NOT_FOUND);
        }
        return assignedToId;
    }

    private void validateRequired(Task task) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new DomainValidationException(TaskMessages.TITLE_REQUIRED);
        }
        if (task.getDueDate() == null) {
            throw new DomainValidationException(TaskMessages.DUE_DATE_REQUIRED);
        }
        if (task.getDueDate().isBefore(LocalDate.now())) {
            throw new DomainValidationException(TaskMessages.DUE_DATE_PAST);
        }
    }

    private void assertCanModify(TaskActor actor, Task task) {
        if (actor.role() == Role.ADMIN) {
            return;
        }
        boolean owner = actor.userId().equals(task.getCreatedById());
        boolean assignee = actor.userId().equals(task.getAssignedToId());
        if (!owner && !assignee) {
            throw new DomainForbiddenException(TaskMessages.FORBIDDEN);
        }
    }

    private void assertCanDelete(TaskActor actor, Task task) {
        if (actor.role() == Role.ADMIN) {
            return;
        }
        if (!actor.userId().equals(task.getCreatedById())) {
            throw new DomainForbiddenException(TaskMessages.FORBIDDEN);
        }
    }

    private Long requireId(Long id) {
        if (id == null) {
            throw new DomainValidationException(TaskMessages.ID_REQUIRED);
        }
        return id;
    }
}
