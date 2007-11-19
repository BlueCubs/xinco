CREATE TABLE xinco_setting (
  id INTEGER(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  description VARCHAR(45) NOT NULL,
  int_value INTEGER(10) UNSIGNED NULL DEFAULT null,
  string_value VARCHAR(500) NULL DEFAULT null,
  bool_value BOOL NULL DEFAULT null,
  long_value BIGINT NULL DEFAULT null,
  PRIMARY KEY(id)
)
TYPE=InnoDB;

INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(35,'general.setting.allowoutsidelinks',null,null,false,null);