-- Initial schema: users table backing the JWT auth module.
-- Add follow-up migrations as V2__*, V3__*, ... in this same folder.

CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(20)  NOT NULL,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    locked_until    TIMESTAMP,
    failed_attempts INTEGER      NOT NULL DEFAULT 0,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);

CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email    ON users (email);
