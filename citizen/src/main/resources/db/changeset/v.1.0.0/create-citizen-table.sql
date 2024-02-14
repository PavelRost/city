--liquibase formatted sql

--changeset PavelRost:create-citizen-table

CREATE TABLE citizen (
	id BIGSERIAL PRIMARY KEY,
	first_name VARCHAR(30),
	last_name VARCHAR(30),
	gender VARCHAR(10),
	job VARCHAR(30),
	passport_id BIGINT UNIQUE,
	account_bank_id BIGINT UNIQUE,
	driver_license_id BIGINT UNIQUE,
    deleted BOOLEAN
);

--rollback drop table citizen;