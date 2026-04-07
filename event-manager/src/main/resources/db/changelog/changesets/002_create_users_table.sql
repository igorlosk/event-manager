CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    login         VARCHAR(255) NOT NULL UNIQUE,
    age           INTEGER,
    password_hash VARCHAR(255),
    role          VARCHAR(50)
);

CREATE INDEX idx_users_role ON users (role);