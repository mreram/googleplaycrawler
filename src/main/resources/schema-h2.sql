CREATE TABLE Auth
(
    id         NUMBER(1) NOT NULL,
    token      VARCHAR  NOT NULL,
    android_id CHAR(100),
    user_agent VARCHAR,
    email      VARCHAR
);