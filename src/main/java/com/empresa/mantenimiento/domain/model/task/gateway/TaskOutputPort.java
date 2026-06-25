package com.empresa.mantenimiento.domain.model.task.gateway;

import com.empresa.mantenimiento.domain.model.common.PageResult;
import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.domain.model.task.TaskQuery;

import java.util.Optional;

public interface TaskOutputPort {
    Task save(Task task);
    Optional<Task> findById(Long id);
    PageResult<Task> search(TaskQuery query);
    boolean existsById(Long id);
    void deleteById(Long id);
}
