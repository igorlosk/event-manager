CREATE TABLE notification_event_payloads
(
    id            BIGSERIAL PRIMARY KEY,
    message_id    UUID         NOT NULL UNIQUE,
    event_type    VARCHAR(255) NOT NULL,
    event_id      BIGINT       NOT NULL,
    occurred_at   TIMESTAMP    NOT NULL,
    owner_id      BIGINT,
    changed_by_id BIGINT,
    payload_json  JSONB        NOT NULL
);

CREATE TABLE notifications
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT,
    is_read    BOOLEAN   NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    read_at    TIMESTAMP,
    payload_id BIGINT    NOT NULL
);