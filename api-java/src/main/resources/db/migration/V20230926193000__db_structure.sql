-- ${public_schema}.email definition

-- Drop table

-- DROP TABLE ${public_schema}.email;

CREATE TABLE ${public_schema}.email (
                                        id int8 NOT NULL,
                                        created_at timestamp NULL,
                                        email varchar(255) NOT NULL,
                                        last_email_unsubscribed_date timestamp NULL,
                                        last_email_unsubscribed_message_id varchar(255) NULL,
                                        last_unsubscribed_date timestamp NULL,
                                        subscribed varchar(255) NULL,
                                        CONSTRAINT email_pkey PRIMARY KEY (id),
                                        CONSTRAINT unique_email UNIQUE (email)
);


-- ${public_schema}.email_group definition

-- Drop table

-- DROP TABLE ${public_schema}.email_group;

CREATE TABLE ${public_schema}.email_group (
                                              id int8 NOT NULL,
                                              "name" varchar(255) NOT NULL,
                                              uuid_to_self_registration varchar(255) NULL,
                                              CONSTRAINT email_group_pkey PRIMARY KEY (id),
                                              CONSTRAINT unique_email_group UNIQUE (name)
);


-- ${public_schema}.qrtz_tasks definition

-- Drop table

-- DROP TABLE ${public_schema}.qrtz_tasks;

CREATE TABLE ${public_schema}.qrtz_tasks (
                                             id int8 NOT NULL,
                                             canceled bool NULL,
                                             canceled_at timestamp NULL,
                                             created_at timestamp NULL,
                                             day_range int4 NULL,
                                             end_at timestamp NULL,
                                             job_group varchar(255) NULL,
                                             job_name varchar(255) NULL,
                                             recurrent bool NOT NULL,
                                             start_at timestamp NULL,
                                             trigger_group varchar(255) NULL,
                                             trigger_name varchar(255) NULL,
                                             CONSTRAINT qrtz_tasks_pkey PRIMARY KEY (id),
                                             CONSTRAINT unique_qrtz_tasks_job UNIQUE (job_name, job_group),
                                             CONSTRAINT unique_qrtz_tasks_trigger UNIQUE (trigger_name, trigger_group)
);


-- ${public_schema}.send_email_log definition

-- Drop table

-- DROP TABLE ${public_schema}.send_email_log;

CREATE TABLE ${public_schema}.send_email_log (
                                                 id int8 NOT NULL,
                                                 email_from varchar(255) NULL,
                                                 error text NULL,
                                                 log_date timestamp NULL,
                                                 message_id varchar(255) NULL,
                                                 qrtz_tasks_id int8 NULL,
                                                 sent_emails text NULL,
                                                 sent_host varchar(255) NULL,
                                                 sent_message text NULL,
                                                 sent_port int4 NULL,
                                                 sent_status varchar(255) NULL,
                                                 sent_subject varchar(255) NULL,
                                                 CONSTRAINT send_email_log_pkey PRIMARY KEY (id)
);


-- ${public_schema}.users definition

-- Drop table

-- DROP TABLE ${public_schema}.users;

CREATE TABLE ${public_schema}.users (
                                        id int8 NOT NULL,
                                        email varchar(255) NULL,
                                        full_name varchar(255) NULL,
                                        "password" varchar(255) NULL,
                                        username varchar(255) NULL,
                                        CONSTRAINT unique_user_email UNIQUE (email),
                                        CONSTRAINT unique_user_username UNIQUE (username),
                                        CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- ${public_schema}.config_email definition

-- Drop table

-- DROP TABLE ${public_schema}.config_email;

CREATE TABLE ${public_schema}.config_email (
                                               id int8 NOT NULL,
                                               email_from varchar(255) NULL,
                                               password_email_from varchar(255) NULL,
                                               send_host varchar(255) NULL,
                                               send_port int4 NULL,
                                               user_id int8 NOT NULL,
                                               CONSTRAINT config_email_pkey PRIMARY KEY (id),
                                               CONSTRAINT unique_config_email_user UNIQUE (user_id),
                                               CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES ${public_schema}.users(id)
);


-- ${public_schema}.email_group_relation definition

-- Drop table

-- DROP TABLE ${public_schema}.email_group_relation;

CREATE TABLE ${public_schema}.email_group_relation (
                                                       id int8 NOT NULL,
                                                       uuid_was_self_registration varchar(255) NULL,
                                                       email_id int8 NULL,
                                                       email_group_id int8 NULL,
                                                       CONSTRAINT email_group_relation_pkey PRIMARY KEY (id),
                                                       CONSTRAINT unique_email_group_relation UNIQUE (email_id, email_group_id),
                                                       CONSTRAINT fk_email FOREIGN KEY (email_id) REFERENCES ${public_schema}.email(id),
                                                       CONSTRAINT fk_email_group FOREIGN KEY (email_group_id) REFERENCES ${public_schema}.email_group(id)
);


-- ${public_schema}.newsletter definition

-- Drop table

-- DROP TABLE ${public_schema}.newsletter;

CREATE TABLE ${public_schema}.newsletter (
                                             id int8 NOT NULL,
                                             alteration_date timestamp NULL,
                                             description varchar(2000) NOT NULL,
                                             inclusion_date timestamp NULL,
                                             newsletter text NOT NULL,
                                             newsletter_template bool NULL,
                                             subject varchar(2000) NOT NULL,
                                             user_id int8 NOT NULL,
                                             CONSTRAINT newsletter_pkey PRIMARY KEY (id),
                                             CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES ${public_schema}.users(id)
);


-- ${public_schema}.newsletter_email definition

-- Drop table

-- DROP TABLE ${public_schema}.newsletter_email;

CREATE TABLE ${public_schema}.newsletter_email (
                                                   newsletter_id int8 NOT NULL,
                                                   email_id int8 NOT NULL,
                                                   CONSTRAINT newsletter_email_pkey PRIMARY KEY (newsletter_id, email_id),
                                                   CONSTRAINT fk_email FOREIGN KEY (email_id) REFERENCES ${public_schema}.email(id),
                                                   CONSTRAINT fk_newsketter FOREIGN KEY (newsletter_id) REFERENCES ${public_schema}.newsletter(id)
);


-- ${public_schema}.newsletter_qrtz_tasks definition

-- Drop table

-- DROP TABLE ${public_schema}.newsletter_qrtz_tasks;

CREATE TABLE ${public_schema}.newsletter_qrtz_tasks (
                                                        id int8 NOT NULL,
                                                        newsletter_id int8 NULL,
                                                        qrtz_tasks_id int8 NULL,
                                                        CONSTRAINT newsletter_qrtz_tasks_pkey PRIMARY KEY (id),
                                                        CONSTRAINT fk_newsletter FOREIGN KEY (newsletter_id) REFERENCES ${public_schema}.newsletter(id),
                                                        CONSTRAINT fk_qrtz_tasks FOREIGN KEY (qrtz_tasks_id) REFERENCES ${public_schema}.qrtz_tasks(id)
);


-- ${public_schema}.newsletter_send_email_log definition

-- Drop table

-- DROP TABLE ${public_schema}.newsletter_send_email_log;

CREATE TABLE ${public_schema}.newsletter_send_email_log (
                                                            newsletter_id int8 NOT NULL,
                                                            send_email_log_id int8 NOT NULL,
                                                            CONSTRAINT newsletter_send_email_log_pkey PRIMARY KEY (newsletter_id, send_email_log_id),
                                                            CONSTRAINT fk_newsletter FOREIGN KEY (newsletter_id) REFERENCES ${public_schema}.newsletter(id),
                                                            CONSTRAINT fk_send_email_log FOREIGN KEY (send_email_log_id) REFERENCES ${public_schema}.send_email_log(id)
);

-- ${public_schema}.configemail_id_sequence definition

-- DROP SEQUENCE ${public_schema}.configemail_id_sequence;

CREATE SEQUENCE ${public_schema}.configemail_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.email_group_id_sequence definition

-- DROP SEQUENCE ${public_schema}.email_group_id_sequence;

CREATE SEQUENCE ${public_schema}.email_group_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.email_group_relation_id_sequence definition

-- DROP SEQUENCE ${public_schema}.email_group_relation_id_sequence;

CREATE SEQUENCE ${public_schema}.email_group_relation_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.email_id_sequence definition

-- DROP SEQUENCE ${public_schema}.email_id_sequence;

CREATE SEQUENCE ${public_schema}.email_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.newsletter_id_sequence definition

-- DROP SEQUENCE ${public_schema}.newsletter_id_sequence;

CREATE SEQUENCE ${public_schema}.newsletter_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.newsletter_qrtz_tasks_id_sequence definition

-- DROP SEQUENCE ${public_schema}.newsletter_qrtz_tasks_id_sequence;

CREATE SEQUENCE ${public_schema}.newsletter_qrtz_tasks_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.qrtz_tasks_id_sequence definition

-- DROP SEQUENCE ${public_schema}.qrtz_tasks_id_sequence;

CREATE SEQUENCE ${public_schema}.qrtz_tasks_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.send_email_log_id_sequence definition

-- DROP SEQUENCE ${public_schema}.send_email_log_id_sequence;

CREATE SEQUENCE ${public_schema}.send_email_log_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;


-- ${public_schema}.users_id_sequence definition

-- DROP SEQUENCE ${public_schema}.users_id_sequence;

CREATE SEQUENCE ${public_schema}.users_id_sequence
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;