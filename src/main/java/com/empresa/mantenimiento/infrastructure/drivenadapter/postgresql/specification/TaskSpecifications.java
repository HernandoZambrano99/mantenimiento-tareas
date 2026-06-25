package com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.specification;

import com.empresa.mantenimiento.domain.model.task.TaskQuery;
import com.empresa.mantenimiento.infrastructure.drivenadapter.postgresql.entity.TaskEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a dynamic {@link Specification} from a {@link TaskQuery}. Only the
 * filters present in the query are applied. {@code restrictToUserId}, when set,
 * limits results to tasks the user created or that are assigned to them.
 */
public final class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<TaskEntity> fromQuery(TaskQuery query) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.status() != null) {
                predicates.add(cb.equal(root.get("status"), query.status()));
            }
            if (query.assignedToId() != null) {
                predicates.add(cb.equal(root.get("assignedToId"), query.assignedToId()));
            }
            if (query.createdById() != null) {
                predicates.add(cb.equal(root.get("createdById"), query.createdById()));
            }
            if (query.dueDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), query.dueDateFrom()));
            }
            if (query.dueDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), query.dueDateTo()));
            }
            if (query.search() != null && !query.search().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")),
                        "%" + query.search().trim().toLowerCase() + "%"));
            }
            if (query.restrictToUserId() != null) {
                predicates.add(cb.or(
                        cb.equal(root.get("createdById"), query.restrictToUserId()),
                        cb.equal(root.get("assignedToId"), query.restrictToUserId())
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
