--liquibase formatted sql

--changeset PavelRost:create-bank-table

CREATE TABLE bank (
	id BIGSERIAL PRIMARY KEY,
	money BIGINT,
    citizen_id BIGINT UNIQUE,
    deleted BOOLEAN
);

--rollback drop table bank;