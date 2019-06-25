CREATE TABLE Auth
        (
        id NUMBER(10) NOT NULL,
        token CHAR(200) NOT NULL,
        android_id CHAR(100),
        user_agent VARCHAR,
        );