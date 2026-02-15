CREATE TABLE IF NOT EXISTS vacancies
(
    id          BIGINT NOT NULL,
    is_activity BOOL   NOT NULL,
    data        JSONB,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS users
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY,
    chat_id         BIGINT         NOT NULL UNIQUE,
    login           VARCHAR UNIQUE NOT NULL,
    first_name      VARCHAR,
    last_name       VARCHAR,
    notification_on BOOLEAN,
    data            JSONB,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS users_message
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY,
    chat_id      BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    message_send VARCHAR,
    message_get  VARCHAR,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS notifications
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    type_event varchar not null,
    message    jsonb   not null,
    status     int,
    PRIMARY KEY (id)
    );