package com.empresa.mantenimiento.application.handler.adapter;

import com.empresa.mantenimiento.application.handler.TaskHandler;
import com.empresa.mantenimiento.domain.model.common.PageResult;
import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.domain.model.task.TaskActor;
import com.empresa.mantenimiento.domain.model.task.TaskQuery;
import com.empresa.mantenimiento.domain.model.task.TaskStatus;
import com.empresa.mantenimiento.domain.usecase.input.TaskInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskHandlerAdapter implements TaskHandler {
    private final TaskInputPort taskInputPort;

    @Override
    @Transactional
    public Task create(TaskActor actor, Task task) {
        return taskInputPort.create(actor, task);
    }

    @Override
    @Transactional
    public Task update(TaskActor actor, Long id, Task task) {
        return taskInputPort.update(actor, id, task);
    }

    @Override
    @Transactional
    public Task changeStatus(TaskActor actor, Long id, TaskStatus status) {
        return taskInputPort.changeStatus(actor, id, status);
    }

    @Override
    public Task findById(TaskActor actor, Long id) {
        return taskInputPort.findById(actor, id);
    }

    @Override
    public PageResult<Task> search(TaskActor actor, TaskQuery query) {
        return taskInputPort.search(actor, query);
    }

    @Override
    @Transactional
    public void delete(TaskActor actor, Long id) {
        taskInputPort.delete(actor, id);
    }
}
