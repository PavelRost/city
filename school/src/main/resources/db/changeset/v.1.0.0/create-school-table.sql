--liquibase formatted sql

--changeset PavelRost:create-school-table

CREATE TABLE school (
	id BIGSERIAL PRIMARY KEY,
	value TEXT,
	citizen_id BIGINT UNIQUE,
    deleted BOOLEAN
);

--rollback drop table school;