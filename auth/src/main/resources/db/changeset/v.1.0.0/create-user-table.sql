--liquibase formatted sql

--changeset PavelRost:create-users-table

CREATE TABLE users (
	id BIGSERIAL PRIMARY KEY,
	login VARCHAR(30) UNIQUE,
    password TEXT,
    role VARCHAR(30)
);

--rollback drop table users;