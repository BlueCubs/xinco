CREATE TABLE xinco_add_attribute_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_data_id INTEGER(10) UNSIGNED NOT NULL,
  attribute_id INTEGER(10) UNSIGNED NOT NULL,
  attrib_int INTEGER(11) NOT NULL,
  attrib_unsignedint INTEGER(10) UNSIGNED NOT NULL,
  attrib_double DOUBLE NOT NULL,
  attrib_varchar VARCHAR(255) NOT NULL,
  attrib_text TEXT NOT NULL,
  attrib_datetime DATETIME NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_audit_has_xinco_core_group_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_group_id INTEGER(10) UNSIGNED NULL,
  xinco_audit_type_id INTEGER(10) UNSIGNED NULL,
  xinco_audit_id INTEGER(10) UNSIGNED NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_audit_has_xinco_core_user_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_user_id INTEGER(10) UNSIGNED NULL,
  xinco_audit_type_id INTEGER(10) UNSIGNED NULL,
  xinco_audit_id INTEGER(10) UNSIGNED NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_audit_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NULL,
  xinco_audit_type_id INTEGER(10) UNSIGNED NULL,
  xinco_core_user_id INTEGER(10) UNSIGNED NULL,
  xinco_core_data_id INTEGER(10) UNSIGNED NULL,
  scheduled_date DATE NULL,
  completion_date DATE NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_audit_type_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NULL,
  days INTEGER(10) UNSIGNED NULL,
  weeks INTEGER(10) UNSIGNED NULL,
  months INTEGER(10) UNSIGNED NULL,
  years INTEGER(10) UNSIGNED NULL,
  description VARCHAR(45) NULL,
  due_same_day BOOL NULL,
  due_same_week BOOL NULL,
  due_same_month BOOL NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_ace_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NULL,
  xinco_core_user_id INTEGER(10) UNSIGNED NULL,
  xinco_core_group_id INTEGER(10) UNSIGNED NULL,
  xinco_core_node_id INTEGER(10) UNSIGNED NULL,
  xinco_core_data_id INTEGER(10) UNSIGNED NULL,
  read_permission BOOL NOT NULL,
  write_permission BOOL NOT NULL,
  execute_permission BOOL NOT NULL,
  admin_permission BOOL NOT NULL,
  audit_permission BOOL NOT NULL,
  owner BOOL NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_data_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_node_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_language_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_data_type_id INTEGER(10) UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_data_type_attribute_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_data_type_id INTEGER(10) UNSIGNED NOT NULL,
  attribute_id INTEGER(10) UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  data_type VARCHAR(255) NOT NULL,
  size INTEGER(10) UNSIGNED NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_data_type_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_group_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_language_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  sign VARCHAR(255) NOT NULL,
  designation VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_node_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_node_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_language_id INTEGER(10) UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_user (
  id INTEGER(10) UNSIGNED NOT NULL,
  username VARCHAR(255) NOT NULL,
  userpassword VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
  attempts INTEGER(10) UNSIGNED NOT NULL,
  last_modified DATE NOT NULL,
  PRIMARY KEY(id),
  INDEX xinco_core_user_index_username(username),
  INDEX xinco_core_user_index_status(status_number)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_user_has_xinco_core_group_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_user_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_group_id INTEGER(10) UNSIGNED NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_user_modified_record (
  id INTEGER(10) UNSIGNED NOT NULL,
  record_id INTEGER(10) UNSIGNED NOT NULL,
  mod_Time TIMESTAMP NOT NULL,
  mod_Reason VARCHAR(255) NULL,
  PRIMARY KEY(id, record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_user_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  username VARCHAR(255) NOT NULL,
  userpassword VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
  attempts INTEGER(10) UNSIGNED NOT NULL,
  last_modified DATE NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_setting_t (
  record_id INTEGER(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  id INTEGER(10) UNSIGNED NULL,
  description VARCHAR(45) NULL,
  int_value INTEGER(10) UNSIGNED NULL,
  string_value VARCHAR(255) NULL,
  bool_value BOOL NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

