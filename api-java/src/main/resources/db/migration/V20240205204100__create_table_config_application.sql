CREATE TABLE ${public_schema}.config_application (
    id int8 NOT NULL,
    url_web varchar(300) NULL,
    CONSTRAINT config_application_pkey PRIMARY KEY (id)
);
CREATE SEQUENCE ${public_schema}.config_application_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;
insert into ${public_schema}.config_application values (nextval('${public_schema}.config_application_id_sequence'), null);