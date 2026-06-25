-- Maintenance tasks: created by a user and assigned to a user (referenced by id,
-- keeping the aggregate boundary). Status: TODO / IN_PROGRESS / DONE.

CREATE TABLE tasks (
    id             BIGSERIAL PRIMARY KEY,
    title          VARCHAR(150) NOT NULL,
    description    VARCHAR(1000),
    status         VARCHAR(20)  NOT NULL,
    due_date       DATE         NOT NULL,
    created_by_id  BIGINT       NOT NULL REFERENCES users (id),
    assigned_to_id BIGINT       NOT NULL REFERENCES users (id),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP
);

CREATE INDEX idx_tasks_created_by  ON tasks (created_by_id);
CREATE INDEX idx_tasks_assigned_to ON tasks (assigned_to_id);
CREATE INDEX idx_tasks_status      ON tasks (status);
CREATE INDEX idx_tasks_due_date    ON tasks (due_date);
