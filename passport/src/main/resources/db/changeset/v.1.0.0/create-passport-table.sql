--liquibase formatted sql

--changeset PavelRost:create-passport-table

CREATE TABLE passport (
	id BIGSERIAL PRIMARY KEY,
	value TEXT,
	citizen_id BIGINT UNIQUE,
    deleted BOOLEAN
);

--rollback drop table passport;