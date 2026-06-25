package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.mapper;

import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    User toDomain(UserEntity entity);
    List<User> toDomainList(List<UserEntity> entities);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity toEntity(User domain);
}
