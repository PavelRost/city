--liquibase formatted sql

--changeset PavelRost:create-car-table

CREATE TABLE car (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30),
	model VARCHAR(30),
	citizen_id BIGINT,
    deleted BOOLEAN
);

--rollback drop table car;