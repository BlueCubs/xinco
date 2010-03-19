DROP INDEX xinco_core_user_index_username;
CREATE UNIQUE INDEX xinco_core_user_index_username ON xinco_core_user (username);

CREATE INDEX xinco_core_group_index_designation ON xinco_core_group (designation);

CREATE INDEX xinco_core_language_index_sign ON xinco_core_language (sign);
CREATE INDEX xinco_core_language_index_designation ON xinco_core_language (designation);
