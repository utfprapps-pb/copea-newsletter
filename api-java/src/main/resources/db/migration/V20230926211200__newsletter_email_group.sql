-- ${public_schema}.newsletter_email_group definition

-- Drop table

-- DROP TABLE ${public_schema}.newsletter_email_group;

CREATE TABLE ${public_schema}.newsletter_email_group (
                                                            id int8 NOT NULL,
                                                            newsletter_id int8 NULL,
                                                            email_group_id int8 NULL,
                                                            CONSTRAINT newsletter_email_group_pkey PRIMARY KEY (id),
                                                            CONSTRAINT unique_newsletter_email_group UNIQUE (newsletter_id, email_group_id),
                                                            CONSTRAINT fk_newsletter FOREIGN KEY (newsletter_id) REFERENCES ${public_schema}.newsletter(id),
                                                            CONSTRAINT fk_email_group FOREIGN KEY (email_group_id) REFERENCES ${public_schema}.email_group(id)
);

-- ${public_schema}.newsletter_email_group_id_sequence definition

-- DROP SEQUENCE ${public_schema}.newsletter_email_group_id_sequence;

CREATE SEQUENCE ${public_schema}.newsletter_email_group_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;