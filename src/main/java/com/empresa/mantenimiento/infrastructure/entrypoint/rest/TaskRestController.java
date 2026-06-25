package com.empresa.mantenimiento.infrastructure.entrypoint.rest;

import com.empresa.mantenimiento.application.handler.TaskHandler;
import com.empresa.mantenimiento.domain.model.common.PageResult;
import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.domain.model.task.TaskActor;
import com.empresa.mantenimiento.domain.model.task.TaskQuery;
import com.empresa.mantenimiento.domain.model.task.TaskStatus;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.TaskRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.TaskStatusRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.response.PageResponse;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.response.TaskResponse;
import com.empresa.mantenimiento.infrastructure.entrypoint.mapper.TaskRestMapper;
import com.empresa.mantenimiento.infrastructure.entrypoint.rest.security.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskRestController {
    private final TaskHandler taskHandler;
    private final TaskRestMapper mapper;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        TaskActor actor = currentUserProvider.currentActor();
        Task created = taskHandler.create(actor, mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
        TaskActor actor = currentUserProvider.currentActor();
        return ResponseEntity.ok(mapper.toResponse(taskHandler.findById(actor, id)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TaskResponse>> search(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long assignedToId,
            @RequestParam(required = false) Long createdById,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateTo,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        TaskActor actor = currentUserProvider.currentActor();
        TaskQuery query = TaskQuery.builder()
                .status(status)
                .assignedToId(assignedToId)
                .createdById(createdById)
                .dueDateFrom(dueDateFrom)
                .dueDateTo(dueDateTo)
                .search(search)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .page(page)
                .size(size)
                .build();
        PageResult<Task> result = taskHandler.search(actor, query);
        return ResponseEntity.ok(toPageResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        TaskActor actor = currentUserProvider.currentActor();
        Task updated = taskHandler.update(actor, id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> changeStatus(@PathVariable Long id,
                                                     @Valid @RequestBody TaskStatusRequest request) {
        TaskActor actor = currentUserProvider.currentActor();
        Task updated = taskHandler.changeStatus(actor, id, request.status());
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        TaskActor actor = currentUserProvider.currentActor();
        taskHandler.delete(actor, id);
        return ResponseEntity.noContent().build();
    }

    private PageResponse<TaskResponse> toPageResponse(PageResult<Task> result) {
        return new PageResponse<>(
                mapper.toResponseList(result.content()),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages());
    }
}
