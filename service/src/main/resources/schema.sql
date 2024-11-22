DROP TABLE if exists categories CASCADE;
DROP TABLE if exists locations CASCADE;
DROP TABLE if exists users CASCADE;
DROP TABLE if exists events CASCADE;
DROP TABLE if exists compilations CASCADE;
DROP TABLE if exists compilation_events CASCADE;
DROP TABLE if exists requests CASCADE;

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id                 INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title              VARCHAR(120) NOT NULL,
    annotation         VARCHAR(2000) NOT NULL,
    category_id        INTEGER   NOT NULL,
    confirmed_requests INTEGER   NOT NULL,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP NOT NULL,
    created_on         TIMESTAMP NOT NULL,
    initiator_id       INTEGER   NOT NULL,
    location_id        INTEGER   NOT NULL,
    paid               BOOLEAN   NOT NULL,
    participant_limit  INTEGER   NOT NULL,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN   NOT NULL,
    state              VARCHAR   NOT NULL,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_user FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned  BOOLEAN      NOT NULL,
    title   VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id INTEGER,
    event_id       INTEGER,
    PRIMARY KEY (compilation_id, event_id),
    FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created_time TIMESTAMP NOT NULL,
    event_id     INTEGER   NOT NULL,
    requester_id INTEGER   NOT NULL,
    status       VARCHAR   NOT NULL,
    CONSTRAINT fk_event_request FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_requester_request FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id                INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id          INTEGER   NOT NULL,
    creator_id        INTEGER   NOT NULL,
    text              VARCHAR   NOT NULL,
    moderation_status VARCHAR   NOT NULL,
    created_on     TIMESTAMP NOT NULL,
    CONSTRAINT fk_event_comment FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_requester_comment FOREIGN KEY (creator_id) REFERENCES users (id)
);

CREATE INDEX events_id ON events (id);
CREATE INDEX events_initiator ON events (initiator_id);
CREATE INDEX events_category ON events (category_id);
CREATE INDEX events_published_on ON events (published_on);
CREATE INDEX events_event_date ON events (event_date);
CREATE INDEX events_created_on ON events (created_on);
CREATE INDEX requests_id ON requests (id);
