package com.empresa.mantenimiento.infrastructure.entrypoint.mapper;

import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.TaskRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskRestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdById", ignore = true)
    Task toDomain(TaskRequest request);

    TaskResponse toResponse(Task domain);
    List<TaskResponse> toResponseList(List<Task> domains);
}
