-- Init script
CREATE TABLE book(
    id bigserial PRIMARY KEY,
    title varchar(64) NOT NULL,
    author varchar(64) NOT NULL
);