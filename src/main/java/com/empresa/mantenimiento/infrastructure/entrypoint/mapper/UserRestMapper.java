package com.empresa.mantenimiento.infrastructure.entrypoint.mapper;

import com.empresa.mantenimiento.domain.model.user.User;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.request.UserRequest;
import com.empresa.mantenimiento.infrastructure.entrypoint.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "lockedUntil", ignore = true)
    @Mapping(target = "failedAttempts", ignore = true)
    User toDomain(UserRequest request);

    UserResponse toResponse(User domain);
    List<UserResponse> toResponseList(List<User> domains);
}
