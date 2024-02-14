--liquibase formatted sql

--changeset PavelRost:create-log-table

CREATE TABLE log (
    id BIGSERIAL PRIMARY KEY,
	type TEXT,
	quantity BIGINT,
	date_time TIMESTAMP
);

--rollback drop table log;