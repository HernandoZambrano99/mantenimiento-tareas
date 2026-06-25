package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.repository;

import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
