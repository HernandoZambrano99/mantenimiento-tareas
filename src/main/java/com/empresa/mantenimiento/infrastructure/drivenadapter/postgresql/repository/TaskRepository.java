package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.repository;

import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
}
