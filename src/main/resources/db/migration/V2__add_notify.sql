CREATE TABLE IF NOT EXISTS notifications
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    type_event varchar not null,
    message    jsonb   not null,
    status     int,
    PRIMARY KEY (id)
    );