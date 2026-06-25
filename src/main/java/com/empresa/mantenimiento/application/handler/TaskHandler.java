package com.empresa.mantenimiento.application.handler;

import com.empresa.mantenimiento.domain.model.common.PageResult;
import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.domain.model.task.TaskActor;
import com.empresa.mantenimiento.domain.model.task.TaskQuery;
import com.empresa.mantenimiento.domain.model.task.TaskStatus;

public interface TaskHandler {
    Task create(TaskActor actor, Task task);
    Task update(TaskActor actor, Long id, Task task);
    Task changeStatus(TaskActor actor, Long id, TaskStatus status);
    Task findById(TaskActor actor, Long id);
    PageResult<Task> search(TaskActor actor, TaskQuery query);
    void delete(TaskActor actor, Long id);
}
