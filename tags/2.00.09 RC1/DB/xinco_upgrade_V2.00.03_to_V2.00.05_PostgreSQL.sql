UPDATE xinco_core_data_type SET DESIGNATION = 'general.data.type.file' WHERE ID =1;

UPDATE xinco_core_data_type SET DESIGNATION = 'general.data.type.text' WHERE ID =2;

UPDATE xinco_core_data_type SET DESIGNATION = 'general.data.type.URL' WHERE ID =3;

UPDATE xinco_core_data_type SET DESIGNATION = 'general.data.type.contact' WHERE ID =4;

UPDATE xinco_core_data_type SET description = 'general.data.type.file.description' WHERE ID =1;

UPDATE xinco_core_data_type SET description = 'general.data.type.text.description' WHERE ID =2;

UPDATE xinco_core_data_type SET description = 'general.data.type.URL.description' WHERE ID =3;

UPDATE xinco_core_data_type SET description = 'general.data.type.contact.description' WHERE ID =4;

UPDATE xinco_core_group SET DESIGNATION = 'general.group.admin' WHERE ID =1;

UPDATE xinco_core_group SET DESIGNATION = 'general.group.allusers' WHERE ID =2;

UPDATE xinco_core_group SET DESIGNATION = 'general.group.public' WHERE ID =3;

alter table xinco_core_user add unique (username);

alter table xinco_core_group add unique (designation);

ALTER TABLE xinco_core_data_type_attribute RENAME COLUMN size TO attr_size;

ALTER TABLE xinco_core_data_type_attribute_t RENAME COLUMN size TO attr_size;

CREATE SEQUENCE xinco_id_id_seq;
ALTER TABLE xinco_id ADD COLUMN id INTEGER NOT NULL DEFAULT nextval('xinco_id_id_seq');
ALTER TABLE xinco_id DROP CONSTRAINT xinco_id_pkey;
ALTER TABLE xinco_id ADD CONSTRAINT xinco_id_pkey PRIMARY KEY (id);
ALTER TABLE xinco_id ADD CONSTRAINT xinco_id_tablename_unique UNIQUE(tablename);

/*For some reason the xinco_core_user_modified_record table is not linked with xinco_core_user*/
ALTER TABLE xinco_core_user_modified_record ADD CONSTRAINT FK_xinco_core_user FOREIGN KEY (id)
    REFERENCES xinco_core_user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
