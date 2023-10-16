CREATE TABLE IF NOT EXISTS user_info(
    id         INTEGER PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password       VARCHAR(100) NOT NULL,
    roles      VARCHAR(100) NOT NULL
);