--liquibase formatted sql

--changeset PavelRost:create-police-table

CREATE TABLE police (
	id BIGSERIAL PRIMARY KEY,
	citizen_id BIGINT UNIQUE,
	license_id BIGINT UNIQUE,
    deleted BOOLEAN
);

--rollback drop table police;