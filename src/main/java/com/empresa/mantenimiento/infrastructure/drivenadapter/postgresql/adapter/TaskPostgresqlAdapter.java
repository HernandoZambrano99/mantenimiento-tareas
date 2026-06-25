package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.adapter;

import com.empresa.mantenimiento.domain.constant.TaskMessages;
import com.empresa.mantenimiento.domain.model.common.PageResult;
import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.domain.model.task.TaskQuery;
import com.empresa.mantenimiento.domain.model.task.gateway.TaskOutputPort;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.TaskEntity;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.mapper.TaskEntityMapper;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.repository.TaskRepository;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.specification.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TaskPostgresqlAdapter implements TaskOutputPort {
    private static final Set<String> SORTABLE_FIELDS = Set.of("id", "title", "status", "dueDate", "createdAt", "updatedAt");
    private static final String DEFAULT_SORT_FIELD = "dueDate";

    private final TaskRepository repository;
    private final TaskEntityMapper mapper;

    @Override
    public Task save(Task task) {
        return mapper.toDomain(repository.save(mapper.toEntity(task)));
    }

    @Override
    public Optional<Task> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResult<Task> search(TaskQuery query) {
        Pageable pageable = toPageable(query);
        Page<TaskEntity> page = repository.findAll(TaskSpecifications.fromQuery(query), pageable);
        return new PageResult<>(
                mapper.toDomainList(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Pageable toPageable(TaskQuery query) {
        int page = query.page() != null && query.page() >= 0 ? query.page() : 0;
        int size = normalizeSize(query.size());
        return PageRequest.of(page, size, toSort(query));
    }

    private int normalizeSize(Integer size) {
        if (size == null || size <= 0) {
            return TaskMessages.DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, TaskMessages.MAX_PAGE_SIZE);
    }

    private Sort toSort(TaskQuery query) {
        String property = query.sortBy() != null && SORTABLE_FIELDS.contains(query.sortBy())
                ? query.sortBy()
                : DEFAULT_SORT_FIELD;
        Sort.Direction direction = "DESC".equalsIgnoreCase(query.sortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(direction, property);
    }
}
