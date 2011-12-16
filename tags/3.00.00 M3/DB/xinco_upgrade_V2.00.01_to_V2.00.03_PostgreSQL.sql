CREATE SEQUENCE xinco_setting_id_seq;
CREATE TABLE xinco_setting (
  id INTEGER NOT NULL DEFAULT nextval('xinco_setting_id_seq'),
  description VARCHAR(45) NOT NULL,
  int_value INTEGER NULL DEFAULT null,
  string_value VARCHAR(500) NULL DEFAULT null,
  bool_value BOOL NULL DEFAULT null,
  long_value BIGINT NULL DEFAULT null,
  PRIMARY KEY(id)
)
;

INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(35,'general.setting.allowoutsidelinks',null,null,false,null);
