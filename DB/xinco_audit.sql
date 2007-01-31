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

CREATE TABLE xinco_core_language_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  sign VARCHAR(255) NOT NULL,
  designation VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
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

CREATE TABLE xinco_core_user_has_xinco_core_group_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_user_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_group_id INTEGER(10) UNSIGNED NOT NULL,
  status_number INTEGER(10) UNSIGNED NOT NULL,
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

CREATE TABLE xinco_core_ace_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_user_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_group_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_node_id INTEGER(10) UNSIGNED NOT NULL,
  xinco_core_data_id INTEGER(10) UNSIGNED NOT NULL,
  read_permission BOOL NOT NULL,
  write_permission BOOL NOT NULL,
  execute_permission BOOL NOT NULL,
  admin_permission BOOL NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

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

CREATE TABLE xinco_core_data_type_t (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER(10) UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
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


