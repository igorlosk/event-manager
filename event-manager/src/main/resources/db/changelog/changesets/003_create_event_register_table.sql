CREATE TABLE events
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255),
    owner_id        INTEGER           NOT NULL,
    max_places      INTEGER           NOT NULL,
    occupied_places INTEGER DEFAULT 0 NOT NULL,
    date            TIMESTAMP         NOT NULL,
    cost            INTEGER,
    duration        INTEGER,
    location_id     INTEGER,
    status          VARCHAR(50)       NOT NULL
);

CREATE TABLE registrations
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT                              NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    event_id   BIGINT                              NOT NULL,
    CONSTRAINT fk_registrations_event
        FOREIGN KEY (event_id)
            REFERENCES events (id)
            ON DELETE CASCADE,
    UNIQUE (user_id, event_id)
);