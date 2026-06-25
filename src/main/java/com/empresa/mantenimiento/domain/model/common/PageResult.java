package com.empresa.mantenimiento.domain.model.common;

import java.util.List;

/**
 * Framework-agnostic paginated result so the domain never depends on
 * Spring Data's {@code Page}.
 */
public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
