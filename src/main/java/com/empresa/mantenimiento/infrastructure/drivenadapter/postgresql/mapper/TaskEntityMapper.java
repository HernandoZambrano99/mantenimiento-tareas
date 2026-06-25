package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.mapper;

import com.empresa.mantenimiento.domain.model.task.Task;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskEntityMapper {
    Task toDomain(TaskEntity entity);
    List<Task> toDomainList(List<TaskEntity> entities);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaskEntity toEntity(Task domain);
}
