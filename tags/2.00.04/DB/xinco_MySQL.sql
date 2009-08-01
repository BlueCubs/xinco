SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `xinco` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `xinco`;

-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_language`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_language` (
  `id` INT UNSIGNED NOT NULL ,
  `sign` VARCHAR(255) NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `xinco_core_language_index_sign` (`sign` ASC) ,
  UNIQUE INDEX `xinco_core_language_index_designation` (`designation` ASC) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_node`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_node` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT UNSIGNED NULL ,
  `xinco_core_language_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_node_FKIndex1` (`xinco_core_node_id` ASC) ,
  INDEX `xinco_core_node_FKIndex2` (`xinco_core_language_id` ASC) ,
  INDEX `xinco_core_node_index_designation` (`designation` ASC) ,
  INDEX `xinco_core_node_index_status` (`status_number` ASC) ,
  CONSTRAINT `fk_{F80595ED-1580-47B4-9EF1-4520F4FCD6D0}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `xinco`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{BEDABAD8-B676-4B92-A2BD-2D4B0AD92BE1}`
    FOREIGN KEY (`xinco_core_language_id` )
    REFERENCES `xinco`.`xinco_core_language` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
COMMENT = 'Status:\nopen = 1\nlocked = 2\narchived = 3\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_type` (
  `id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `description` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT UNSIGNED NOT NULL ,
  `xinco_core_language_id` INT UNSIGNED NOT NULL ,
  `xinco_core_data_type_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_data_FKIndex1` (`xinco_core_node_id` ASC) ,
  INDEX `xinco_core_data_FKIndex2` (`xinco_core_language_id` ASC) ,
  INDEX `xinco_core_data_FKIndex5` (`xinco_core_data_type_id` ASC) ,
  INDEX `xinco_core_data_index_designation` (`designation` ASC) ,
  INDEX `xinco_core_data_index_status` (`status_number` ASC) ,
  CONSTRAINT `fk_{C8219CB3-85C4-40CF-82C3-9D5280AE3115}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `xinco`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{F49E4ED7-424E-45DF-8A99-A0BE988FA2F8}`
    FOREIGN KEY (`xinco_core_language_id` )
    REFERENCES `xinco`.`xinco_core_language` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{38E3201F-F675-49EA-9A18-4DE13AAB87D1}`
    FOREIGN KEY (`xinco_core_data_type_id` )
    REFERENCES `xinco`.`xinco_core_data_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
COMMENT = 'Status: \nopen = 1 \nlocked = 2 \narchived = 3 \nchecked-out = 4\npublished = 5\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user` (
  `id` INT(10) UNSIGNED NOT NULL ,
  `username` VARCHAR(255) NOT NULL ,
  `userpassword` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `firstname` VARCHAR(255) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  `attempts` INT(10) UNSIGNED NOT NULL ,
  `last_modified` DATE NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_user_index_username` (`username` ASC) ,
  INDEX `xinco_core_user_index_status` (`status_number` ASC) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_group` (
  `id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_group_index_status` (`status_number` ASC) ,
  UNIQUE INDEX `xinco_core_group_index_designation` (`designation` ASC) )
COMMENT = 'Status:  \nopen = 1  \nlocked = 2  \n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_ace`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_ace` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT UNSIGNED NULL ,
  `xinco_core_group_id` INT UNSIGNED NULL ,
  `xinco_core_node_id` INT UNSIGNED NULL ,
  `xinco_core_data_id` INT UNSIGNED NULL ,
  `read_permission` BOOLEAN NOT NULL ,
  `write_permission` BOOLEAN NOT NULL ,
  `execute_permission` BOOLEAN NOT NULL ,
  `admin_permission` BOOLEAN NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_ace_FKIndex1` (`xinco_core_user_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex2` (`xinco_core_group_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex3` (`xinco_core_node_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex4` (`xinco_core_data_id` ASC) ,
  CONSTRAINT `fk_{B77AD310-7F14-441F-B8E1-0E340E872164}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{3EAAFB89-AC6C-439F-8D19-E3C1BD6D049A}`
    FOREIGN KEY (`xinco_core_group_id` )
    REFERENCES `xinco`.`xinco_core_group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{871C2811-5A97-48B6-9B42-7A6E8BBE28E3}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `xinco`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{4A2F84DC-A6C9-4E96-87C3-10EB69934816}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user_has_xinco_core_group` (
  `xinco_core_user_id` INT UNSIGNED NOT NULL ,
  `xinco_core_group_id` INT UNSIGNED NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`xinco_core_user_id`, `xinco_core_group_id`) ,
  INDEX `xinco_core_user_has_xinco_core_group_FKIndex1` (`xinco_core_user_id` ASC) ,
  INDEX `xinco_core_user_has_xinco_core_group_FKIndex2` (`xinco_core_group_id` ASC) ,
  INDEX `xinco_core_user_has_xinco_core_group_index_status` (`status_number` ASC) ,
  CONSTRAINT `fk_{AF1F870B-3BF4-40E9-B9F7-2DB9BC033730}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{3DADC050-43C6-4275-B207-0021CA27C782}`
    FOREIGN KEY (`xinco_core_group_id` )
    REFERENCES `xinco`.`xinco_core_group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
COMMENT = 'Status:  \nopen = 1  \nlocked = 2  \n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_id`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_id` (
  `id` VARCHAR(45) NOT NULL ,
  `tablename` VARCHAR(255) NOT NULL ,
  `last_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `Unique Tablename` (`tablename` ASC) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_add_attribute`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_add_attribute` (
  `xinco_core_data_id` INT UNSIGNED NOT NULL ,
  `attribute_id` INT UNSIGNED NOT NULL ,
  `attrib_int` INT NULL ,
  `attrib_unsignedint` INT UNSIGNED NULL ,
  `attrib_double` DOUBLE NULL ,
  `attrib_varchar` VARCHAR(255) NULL ,
  `attrib_text` TEXT NULL ,
  `attrib_datetime` DATETIME NULL ,
  PRIMARY KEY (`xinco_core_data_id`, `attribute_id`) ,
  INDEX `xinco_add_attribute_FKIndex1` (`xinco_core_data_id` ASC) ,
  CONSTRAINT `fk_{C3001439-BB69-40C1-8677-383CED66F950}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type_attribute`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_type_attribute` (
  `xinco_core_data_type_id` INT UNSIGNED NOT NULL ,
  `attribute_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `data_type` VARCHAR(255) NOT NULL ,
  `size` INT UNSIGNED NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`xinco_core_data_type_id`, `attribute_id`) ,
  INDEX `xinco_core_data_type_attribute_FKIndex1` (`xinco_core_data_type_id` ASC) ,
  CONSTRAINT `fk_{DC890446-CAA3-45E4-9C9F-2B6E859CB64F}`
    FOREIGN KEY (`xinco_core_data_type_id` )
    REFERENCES `xinco`.`xinco_core_data_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_log`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_log` (
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
  INDEX `xinco_core_log_FKIndex1` (`xinco_core_data_id` ASC) ,
  INDEX `xinco_core_log_FKIndex2` (`xinco_core_user_id` ASC) ,
  CONSTRAINT `fk_{0F47D572-8FB9-4973-9204-38EEFDE84ABE}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{48AE6F4C-F6A3-430F-842F-D0CF2C8C8F95}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
COMMENT = 'Op-Codes: \ncreation = 1 \nmodification = 2 \ncheckout = 3 \ncheckoutundone = 4\ncheckin = 5\npublish = 6\nlock = 7\narchive = 8\ncomment = 9\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_add_attribute_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_add_attribute_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_data_id` INT(10) UNSIGNED NOT NULL ,
  `attribute_id` INT(10) UNSIGNED NOT NULL ,
  `attrib_int` INT(11) NOT NULL ,
  `attrib_unsignedint` INT(10) UNSIGNED NOT NULL ,
  `attrib_double` DOUBLE NOT NULL ,
  `attrib_varchar` VARCHAR(255) NOT NULL ,
  `attrib_text` TEXT NOT NULL ,
  `attrib_datetime` DATETIME NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_ace_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_ace_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT(10) UNSIGNED NULL ,
  `xinco_core_group_id` INT(10) UNSIGNED NULL ,
  `xinco_core_node_id` INT(10) UNSIGNED NULL ,
  `xinco_core_data_id` INT(10) UNSIGNED NULL ,
  `read_permission` BOOLEAN NOT NULL ,
  `write_permission` BOOLEAN NOT NULL ,
  `execute_permission` BOOLEAN NOT NULL ,
  `admin_permission` BOOLEAN NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_language_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_data_type_id` INT(10) UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type_attribute_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_type_attribute_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_data_type_id` INT(10) UNSIGNED NOT NULL ,
  `attribute_id` INT(10) UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `data_type` VARCHAR(255) NOT NULL ,
  `size` INT(10) UNSIGNED NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_type_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `description` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_group_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_group_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_language_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_language_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `sign` VARCHAR(255) NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_node_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_node_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_node_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_language_id` INT(10) UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user` (
  `id` INT(10) UNSIGNED NOT NULL ,
  `username` VARCHAR(255) NOT NULL ,
  `userpassword` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `firstname` VARCHAR(255) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  `attempts` INT(10) UNSIGNED NOT NULL ,
  `last_modified` DATE NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_user_index_username` (`username` ASC) ,
  INDEX `xinco_core_user_index_status` (`status_number` ASC) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user_has_xinco_core_group_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user_has_xinco_core_group_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_group_id` INT(10) UNSIGNED NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user_t`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `username` VARCHAR(255) NOT NULL ,
  `userpassword` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `firstname` VARCHAR(255) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `status_number` INT(10) UNSIGNED NOT NULL ,
  `attempts` INT(10) UNSIGNED NOT NULL ,
  `last_modified` DATE NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user_modified_record`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user_modified_record` (
  `id` INT(10) UNSIGNED NOT NULL ,
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `mod_Time` TIMESTAMP NOT NULL ,
  `mod_Reason` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`, `record_id`) ,
  CONSTRAINT `fk_{2A195D19-CA44-43DA-93EE-EBCA70503240}`
    FOREIGN KEY (`id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
