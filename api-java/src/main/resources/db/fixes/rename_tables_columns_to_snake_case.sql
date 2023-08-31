-- Script criado pois no começo não foi deixado um padrão,
-- por isso esse script deixa todas as tabelas e colunas que
-- existiam até o momento com o padrão snake_case, caso
-- você já tenha rodado no banco a versão antiga,
-- rodar esse script para corrigir

-- configemail - config_email:
ALTER TABLE configemail RENAME TO config_email;
ALTER TABLE config_email RENAME COLUMN emailfrom TO email_from;
ALTER TABLE config_email RENAME COLUMN passwordemailfrom TO password_email_from;
ALTER TABLE config_email RENAME COLUMN sendhost TO send_host;
ALTER TABLE config_email RENAME COLUMN sendport TO send_port;
-- users:
ALTER TABLE users RENAME COLUMN fullname TO full_name;
-- newsletter:
ALTER TABLE newsletter RENAME COLUMN inclusiondate TO inclusion_date;
ALTER TABLE newsletter RENAME COLUMN alterationdate TO alteration_date;
-- send_email_log:
ALTER TABLE send_email_log RENAME COLUMN logdate TO log_date;
ALTER TABLE send_email_log RENAME COLUMN senthost TO sent_host;
ALTER TABLE send_email_log RENAME COLUMN sentport TO sent_port;
ALTER TABLE send_email_log RENAME COLUMN emailfrom TO email_from;
ALTER TABLE send_email_log RENAME COLUMN sentemails TO sent_emails;
ALTER TABLE send_email_log RENAME COLUMN sentsubject TO sent_subject;
ALTER TABLE send_email_log RENAME COLUMN sentmessage TO sent_message;
ALTER TABLE send_email_log RENAME COLUMN sentstatus TO sent_status;
ALTER TABLE send_email_log RENAME COLUMN messageid TO message_id;