SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `xinco_core_language`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_language` (
  `id` INT UNSIGNED NOT NULL ,
  `sign` VARCHAR(255) NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE UNIQUE INDEX `xinco_core_language_unique` ON `xinco_core_language` (`designation` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_node`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_node` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT UNSIGNED NULL ,
  `xinco_core_language_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_{B2DFE416-00E4-48DA-AD6B-A5D66F5408AF}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `mydb`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{F908E003-6E78-44CB-B768-4B3F28246F5B}`
    FOREIGN KEY (`xinco_core_language_id` )
    REFERENCES `mydb`.`xinco_core_language` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
COMMENT = 'Status:\nopen = 1\nlocked = 2\narchived = 3\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_node_FKIndex1` ON `xinco_core_node` (`xinco_core_node_id` ASC) ;

CREATE INDEX `xinco_core_node_FKIndex2` ON `xinco_core_node` (`xinco_core_language_id` ASC) ;

CREATE INDEX `xinco_core_node_index_designation` ON `xinco_core_node` (`designation` ASC) ;

CREATE INDEX `xinco_core_node_index_status` ON `xinco_core_node` (`status_number` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_data_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_data_type` (
  `id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `description` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco_core_data`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_data` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT UNSIGNED NOT NULL ,
  `xinco_core_language_id` INT UNSIGNED NOT NULL ,
  `xinco_core_data_type_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_{09F06A38-7806-4E99-9AC1-43A0E2BC36C6}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `mydb`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{0ABD1A6C-7BAC-4AD9-B623-8B20AF80AD64}`
    FOREIGN KEY (`xinco_core_language_id` )
    REFERENCES `mydb`.`xinco_core_language` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{AAA05116-1FE8-4580-A7DF-4D0766C45F96}`
    FOREIGN KEY (`xinco_core_data_type_id` )
    REFERENCES `mydb`.`xinco_core_data_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
COMMENT = 'Status: \nopen = 1 \nlocked = 2 \narchived = 3 \nchecked-out = 4\npublished = 5\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_data_FKIndex1` ON `xinco_core_data` (`xinco_core_node_id` ASC) ;

CREATE INDEX `xinco_core_data_FKIndex2` ON `xinco_core_data` (`xinco_core_language_id` ASC) ;

CREATE INDEX `xinco_core_data_FKIndex3` ON `xinco_core_data` (`xinco_core_data_type_id` ASC) ;

CREATE INDEX `xinco_core_data_index_designation` ON `xinco_core_data` (`designation` ASC) ;

CREATE INDEX `xinco_core_data_index_status` ON `xinco_core_data` (`status_number` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_user` (
  `id` INT UNSIGNED NOT NULL ,
  `username` VARCHAR(255) NOT NULL ,
  `userpassword` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `firstname` VARCHAR(255) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  `attempts` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `last_modified` DATE NOT NULL ,
  PRIMARY KEY (`id`) )
COMMENT = 'Status: \nopen = 1 \nlocked = 2 \n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_user_index_username` ON `xinco_core_user` (`username` ASC) ;

CREATE INDEX `xinco_core_user_index_status` ON `xinco_core_user` (`status_number` ASC) ;

CREATE UNIQUE INDEX `xinco_core_user_index_unique` ON `xinco_core_user` (`username` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_group` (
  `id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) )
COMMENT = 'Status:  \nopen = 1  \nlocked = 2  \n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_group_index_status` ON `xinco_core_group` (`status_number` ASC) ;

CREATE UNIQUE INDEX `xinco_core_group_unique` ON `xinco_core_group` (`designation` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_ace`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_ace` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT UNSIGNED NOT NULL ,
  `xinco_core_group_id` INT UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT UNSIGNED NOT NULL ,
  `xinco_core_data_id` INT UNSIGNED NOT NULL ,
  `read_permission` BOOLEAN NOT NULL ,
  `write_permission` BOOLEAN NOT NULL ,
  `execute_permission` BOOLEAN NOT NULL ,
  `admin_permission` BOOLEAN NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_{7C74BFC1-9994-4DC0-BFDB-5C32A4EAEDA3}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `mydb`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{AE0C1C19-02EB-43E4-A579-4EF09F49970A}`
    FOREIGN KEY (`xinco_core_group_id` )
    REFERENCES `mydb`.`xinco_core_group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{77E66257-5C8F-4DC2-8D78-A22DAD16C211}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `mydb`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{1C74E27D-3F2B-4484-BC84-B10C46630610}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `mydb`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_ace_FKIndex1` ON `xinco_core_ace` (`xinco_core_user_id` ASC) ;

CREATE INDEX `xinco_core_ace_FKIndex2` ON `xinco_core_ace` (`xinco_core_group_id` ASC) ;

CREATE INDEX `xinco_core_ace_FKIndex3` ON `xinco_core_ace` (`xinco_core_node_id` ASC) ;

CREATE INDEX `xinco_core_ace_FKIndex4` ON `xinco_core_ace` (`xinco_core_data_id` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_user_has_xinco_core_group` (
  `xinco_core_user_id` INT UNSIGNED NOT NULL ,
  `xinco_core_group_id` INT UNSIGNED NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`xinco_core_user_id`, `xinco_core_group_id`) ,
  CONSTRAINT `fk_{68C1E694-70C6-45E9-A32E-50666CD7E818}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `mydb`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{C096D53C-EEC8-4091-8BD4-CBE7C22E3D42}`
    FOREIGN KEY (`xinco_core_group_id` )
    REFERENCES `mydb`.`xinco_core_group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
COMMENT = 'Status:  \nopen = 1  \nlocked = 2  \n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_user_has_xinco_core_group_FKIndex1` ON `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id` ASC) ;

CREATE INDEX `xinco_core_user_has_xinco_core_group_FKIndex2` ON `xinco_core_user_has_xinco_core_group` (`xinco_core_group_id` ASC) ;

CREATE INDEX `xinco_core_user_has_xinco_core_group_index_status` ON `xinco_core_user_has_xinco_core_group` (`status_number` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_id`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_id` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `tablename` VARCHAR(255) NOT NULL ,
  `last_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE UNIQUE INDEX `Unique` ON `xinco_id` (`tablename` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_add_attribute`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_add_attribute` (
  `xinco_core_data_id` INT UNSIGNED NOT NULL ,
  `attribute_id` INT UNSIGNED NOT NULL ,
  `attrib_int` INT NULL ,
  `attrib_unsignedint` INT UNSIGNED NULL ,
  `attrib_double` DOUBLE NULL ,
  `attrib_varchar` VARCHAR(255) NULL ,
  `attrib_text` TEXT NULL ,
  `attrib_datetime` DATETIME NULL ,
  PRIMARY KEY (`xinco_core_data_id`, `attribute_id`) ,
  CONSTRAINT `fk_{2AC90DB6-18F8-4FA2-8D27-2E0192554C88}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `mydb`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_add_attribute_FKIndex1` ON `xinco_add_attribute` (`xinco_core_data_id` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_data_type_attribute`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_data_type_attribute` (
  `xinco_core_data_type_id` INT UNSIGNED NOT NULL ,
  `attribute_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `data_type` VARCHAR(255) NOT NULL ,
  `size` INT UNSIGNED NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`xinco_core_data_type_id`, `attribute_id`) ,
  CONSTRAINT `fk_{D244DF80-85E1-4D84-A926-DC14A3D0B3BA}`
    FOREIGN KEY (`xinco_core_data_type_id` )
    REFERENCES `mydb`.`xinco_core_data_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_data_type_attribute_FKIndex1` ON `xinco_core_data_type_attribute` (`xinco_core_data_type_id` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_core_log`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_core_log` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_data_id` INT UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT UNSIGNED NOT NULL ,
  `op_code` INT UNSIGNED NOT NULL ,
  `op_datetime` DATETIME NOT NULL ,
  `op_description` VARCHAR(255) NOT NULL ,
  `version_high` INT UNSIGNED NULL ,
  `version_mid` INT UNSIGNED NULL ,
  `version_low` INT UNSIGNED NULL ,
  `version_postfix` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_{C63E31F0-F24E-4E4A-B876-5BB14BA71B33}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `mydb`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{2306293F-EDF8-4EBB-A4CF-C41B8F0BF9A3}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `mydb`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
COMMENT = 'Op-Codes: \ncreation = 1 \nmodification = 2 \ncheckout = 3 \ncheckoutundone = 4\ncheckin = 5\npublish = 6\nlock = 7\narchive = 8\ncomment = 9\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;

CREATE INDEX `xinco_core_log_FKIndex1` ON `xinco_core_log` (`xinco_core_data_id` ASC) ;

CREATE INDEX `xinco_core_log_FKIndex2` ON `xinco_core_log` (`xinco_core_user_id` ASC) ;


-- -----------------------------------------------------
-- Table `xinco_setting`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco_setting` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(45) NOT NULL ,
  `int_value` INT(10) UNSIGNED NULL DEFAULT NULL ,
  `string_value` VARCHAR(500) NULL DEFAULT NULL ,
  `bool_value` BOOLEAN NULL DEFAULT NULL ,
  `long_value` BIGINT NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Data for table `xinco_core_node`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_node`(1, NULL, 1, 'xincoRoot', 1);
INSERT INTO `xinco_core_node`(2, 1, 1, 'Trash', 1);
INSERT INTO `xinco_core_node`(3, 1, 1, 'Temp', 1);
INSERT INTO `xinco_core_node`(4, 1, 1, 'News', 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_data`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_data`(1, 1, 2, 2, 'Apache License 2.0', 5);
INSERT INTO `xinco_core_data`(2, 1, 2, 3, 'xinco.org', 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_language`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_language`(1, 'n/a', 'unknown');
INSERT INTO `xinco_core_language`(2, 'en', 'English');
INSERT INTO `xinco_core_language`(3, 'de', 'German');
INSERT INTO `xinco_core_language`(4, 'fr', 'French');
INSERT INTO `xinco_core_language`(5, 'it', 'Italian');
INSERT INTO `xinco_core_language`(6, 'es', 'Spanish');
INSERT INTO `xinco_core_language`(7, 'ru', 'Russian');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_ace`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_ace`(1, 1, -1, 1, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(2, 1, -1, 2, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(3, 1, -1, 3, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(4, -1, 1, 1, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(5, -1, 1, 2, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(6, -1, 1, 3, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(7, -1, 2, 1, -1, 1, 1, 1, 0);
INSERT INTO `xinco_core_ace`(8, -1, 2, 2, -1, 1, 1, 1, 0);
INSERT INTO `xinco_core_ace`(9, -1, 2, 3, -1, 1, 1, 1, 0);
INSERT INTO `xinco_core_ace`(10, 1, -1, -1, 1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(11, 1, -1, -1, 2, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(12, -1, 1, -1, 1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(13, -1, 1, -1, 2, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(14, -1, 2, -1, 1, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace`(15, -1, 2, -1, 2, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace`(16, 1, -1, 4, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(17, -1, 1, 4, -1, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace`(18, -1, 2, 4, -1, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace`(19, -1, 3, 1, -1, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace`(20, -1, 3, 4, -1, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace`(21, -1, 3, -1, 1, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace`(22, -1, 3, -1, 2, 1, 0, 0, 0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_user`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_user`(1, 'admin', MD5('admin'), 'Administrator', 'Xinco', 'admin@xinco.org', 1, 0, now());
INSERT INTO `xinco_core_user`(2, 'user', MD5('user'), 'User', 'Default', 'user@xinco.org', 1, 0, now());
INSERT INTO `xinco_core_user`(3, 'bluecubs', MD5('bluecubs'), 'System', 'User', 'info@bluecubs.com', 1, 0, now());

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_user_has_xinco_core_group`(1, 1, 1);
INSERT INTO `xinco_core_user_has_xinco_core_group`(1, 2, 1);
INSERT INTO `xinco_core_user_has_xinco_core_group`(2, 2, 1);
INSERT INTO `xinco_core_user_has_xinco_core_group`(3, 2, 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_id`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_language', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_data_type', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_user', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_group', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_node', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_data', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_ace', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_log', 1000);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_core_user_modified_record', 0);
INSERT INTO `xinco_id` (tablename, last_id) VALUES ('xinco_setting',1000);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_data_type`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_data_type`(1, 'general.data.type.file', 'general.data.type.file.description');
INSERT INTO `xinco_core_data_type`(2, 'general.data.type.text', 'general.data.type.text.description');
INSERT INTO `xinco_core_data_type`(3, 'general.data.type.URL', 'general.data.type.URL.description');
INSERT INTO `xinco_core_data_type`(4, 'general.data.type.contact', 'general.data.type.contact.description');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_data_type_attribute`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_data_type_attribute`(1, 1, 'File_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(1, 2, 'Size', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute`(1, 3, 'Checksum', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(1, 4, 'Revision_Model', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute`(1, 5, 'Archiving_Model', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute`(1, 6, 'Archiving_Date', 'datetime', 0);
INSERT INTO `xinco_core_data_type_attribute`(1, 7, 'Archiving_Days', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute`(1, 8, 'Archiving_Location', 'text', 0);
INSERT INTO `xinco_core_data_type_attribute`(1, 9, 'Description', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(1, 10, 'Keyword_1', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(1, 11, 'Keyword_2', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(1, 12, 'Keyword_3', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(2, 1, 'Text', 'text', 0);
INSERT INTO `xinco_core_data_type_attribute`(3, 1, 'URL', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 1, 'Salutation', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 2, 'First_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 3, 'Middle_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 4, 'Last_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 5, 'Name_Affix', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 6, 'Phone_business', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 7, 'Phone_private', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 8, 'Phone_mobile', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 9, 'Fax', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 10, 'Email', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 11, 'Website', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 12, 'Street_Address', 'text', 0);
INSERT INTO `xinco_core_data_type_attribute`(4, 13, 'Postal_Code', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 14, 'City', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 15, 'State_Province', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 16, 'Country', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 17, 'Company_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 18, 'Position', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute`(4, 19, 'Notes', 'text', 0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_core_log`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
INSERT INTO `xinco_core_log`(1, 1, 1, 1, now(), 'audit.general.create', 1, 0, 0, '');
INSERT INTO `xinco_core_log`(2, 2, 1, 1, now(), 'audit.general.create', 1, 0, 0, '');

COMMIT;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
