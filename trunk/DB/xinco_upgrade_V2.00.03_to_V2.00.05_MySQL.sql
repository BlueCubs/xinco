UPDATE xinco_core_data_typeSET DESIGNATION = 'general.data.type.file' WHERE ID =1;

UPDATE xinco_core_data_typeSET DESIGNATION = 'general.data.type.text' WHERE ID =2;

UPDATE xinco_core_data_typeSET DESIGNATION = 'general.data.type.URL' WHERE ID =3;

UPDATE xinco_core_data_typeSET DESIGNATION = 'general.data.type.contact' WHERE ID =4;

UPDATE xinco_core_data_typeSET description = 'general.data.type.file.description' WHERE ID =1;

UPDATE xinco_core_data_typeSET description = 'general.data.type.text.description' WHERE ID =2;

UPDATE xinco_core_data_typeSET description = 'general.data.type.URL.description' WHERE ID =3;

UPDATE xinco_core_data_typeSET description = 'general.data.type.contact.description' WHERE ID =4;

UPDATE xinco_core_groupSET DESIGNATION = 'general.group.admin' WHERE ID =1;

UPDATE xinco_core_groupSET DESIGNATION = 'general.group.allusers' WHERE ID =2;

UPDATE xinco_core_groupSET DESIGNATION = 'general.group.public' WHERE ID =3;

alter table xinco_core_user add unique (username);

alter table xinco_core_groupadd unique (designation);

ALTER TABLE xinco_core_data_type_attribute CHANGE COLUMN `size` `attr_size` INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE `xinco`.`xinco_core_data_type_attribute_t` CHANGE COLUMN `size` `attr_size` INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE `xinco`.`xinco_id` ADD COLUMN `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT AFTER `last_id`,
 DROP PRIMARY KEY,
 ADD PRIMARY KEY  USING BTREE(`id`),
 ADD UNIQUE INDEX `Unique`(`tablename`);

/*For some reason the xinco_core_user_modified_record table is not linked with xinco_core_user*/
ALTER TABLE `xinco`.`xinco_core_user_modified_record` ADD CONSTRAINT `FK_xinco_core_user` FOREIGN KEY `FK_xinco_core_user` (`id`)
    REFERENCES `xinco_core_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;