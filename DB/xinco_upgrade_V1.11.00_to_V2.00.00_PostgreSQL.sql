ALTER TABLE xinco_core_user ADD attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE xinco_core_user ADD last_modified DATE;

--
-- Table structure for audit tables 
--

CREATE TABLE xinco_core_node_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  xinco_core_node_id INTEGER NOT NULL,
  xinco_core_language_id INTEGER NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_language_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  sign VARCHAR(255) NOT NULL,
  designation VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_user_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  username VARCHAR(255) NOT NULL,
  userpassword VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  status_number INTEGER NOT NULL,
  attempts INTEGER NOT NULL,
  last_modified DATE NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_user_has_xinco_core_group_t (
  record_id INTEGER NOT NULL,
  xinco_core_user_id INTEGER NOT NULL,
  xinco_core_group_id INTEGER NOT NULL,
  status_number INTEGER NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_group_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_ace_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  xinco_core_user_id INTEGER,
  xinco_core_group_id INTEGER,
  xinco_core_node_id INTEGER,
  xinco_core_data_id INTEGER,
  read_permission INTEGER NOT NULL,
  write_permission INTEGER NOT NULL,
  execute_permission INTEGER NOT NULL,
  admin_permission INTEGER NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_add_attribute_t (
  record_id INTEGER NOT NULL,
  xinco_core_data_id INTEGER NOT NULL,
  attribute_id INTEGER NOT NULL,
  attrib_int INTEGER NOT NULL,
  attrib_unsignedint INTEGER NOT NULL,
  attrib_double FLOAT NOT NULL,
  attrib_varchar VARCHAR(255) NOT NULL,
  attrib_text TEXT NOT NULL,
  attrib_datetime TIMESTAMP NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_data_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  xinco_core_node_id INTEGER NOT NULL,
  xinco_core_language_id INTEGER NOT NULL,
  xinco_core_data_type_id INTEGER NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_data_type_t (
  record_id INTEGER NOT NULL,
  id INTEGER NOT NULL,
  designation VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_data_type_attribute_t (
  record_id INTEGER NOT NULL,
  xinco_core_data_type_id INTEGER NOT NULL,
  attribute_id INTEGER NOT NULL,
  designation VARCHAR(255) NOT NULL,
  data_type VARCHAR(255) NOT NULL,
  size INTEGER NOT NULL,
  PRIMARY KEY(record_id)
)
;

CREATE TABLE xinco_core_user_modified_record (
  id INTEGER NOT NULL,
  record_id INTEGER NOT NULL,
  mod_Time TIMESTAMP NOT NULL,
  mod_Reason VARCHAR(255) NULL,
  PRIMARY KEY(id, record_id),
  FOREIGN KEY(id)
    REFERENCES xinco_core_user(id)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
;

UPDATE xinco_core_user SET last_modified = now();
ALTER TABLE xinco_core_user ALTER COLUMN last_modified SET NOT NULL;

INSERT INTO xinco_id VALUES ('xinco_core_user_modified_record', 0);  
