--liquibase formatted sql

--changeset PavelRost:create-house-table

CREATE TABLE house (
	id BIGSERIAL PRIMARY KEY,
	address VARCHAR(30),
    citizen_id BIGINT,
    deleted BOOLEAN
);

--rollback drop table house;