SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `xinco` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
CREATE SCHEMA IF NOT EXISTS `xinco_workflow` DEFAULT CHARACTER SET utf8 ;
USE `xinco` ;

-- -----------------------------------------------------
-- Table `xinco`.`xinco_add_attribute_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_add_attribute_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_ace_t` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_ace_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT(10) UNSIGNED NULL ,
  `xinco_core_group_id` INT(10) UNSIGNED NULL ,
  `xinco_core_node_id` INT(10) UNSIGNED NULL ,
  `xinco_core_data_id` INT(10) UNSIGNED NULL ,
  `read_permission` TINYINT(1)  NOT NULL ,
  `write_permission` TINYINT(1)  NOT NULL ,
  `execute_permission` TINYINT(1)  NOT NULL ,
  `admin_permission` TINYINT(1)  NOT NULL ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_type_attribute_t` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_type_attribute_t` (
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_data_type_id` INT(10) UNSIGNED NOT NULL ,
  `attribute_id` INT(10) UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `data_type` VARCHAR(255) NOT NULL ,
  `attr_size` INT(10) UNSIGNED NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`record_id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_type_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_group_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_language_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_node_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_user` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user` (
  `id` INT UNSIGNED NOT NULL ,
  `username` VARCHAR(255) NOT NULL ,
  `userpassword` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `firstname` VARCHAR(255) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  `attempts` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `last_modified` DATE NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_user_index_username` (`username` ASC) ,
  INDEX `xinco_core_user_index_status` (`status_number` ASC) ,
  UNIQUE INDEX `unique id` (`username` ASC) )
COMMENT = '\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user_has_xinco_core_group_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_user_has_xinco_core_group_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_user_t` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_user_modified_record` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user_modified_record` (
  `id` INT(10) UNSIGNED NOT NULL ,
  `record_id` INT(10) UNSIGNED NOT NULL ,
  `mod_Time` TIMESTAMP NOT NULL ,
  `mod_Reason` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`record_id`, `id`) ,
  INDEX `fk_{66203500-79C5-4ABB-AF2B-546B0D7CD657}` (`id` ASC) ,
  CONSTRAINT `fk_{66203500-79C5-4ABB-AF2B-546B0D7CD657}`
    FOREIGN KEY (`id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_language`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_language` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_language` (
  `id` INT UNSIGNED NOT NULL ,
  `sign` VARCHAR(255) NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`) )
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_node`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_node` ;

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
  CONSTRAINT `fk_{52D94EF1-ED6A-43AD-9C52-7E883915CF11}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `xinco`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{E427E1BE-0EC7-40B2-9822-8756CE5FFF5C}`
    FOREIGN KEY (`xinco_core_language_id` )
    REFERENCES `xinco`.`xinco_core_language` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
COMMENT = '\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_type` ;

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
DROP TABLE IF EXISTS `xinco`.`xinco_core_data` ;

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
  CONSTRAINT `fk_{0FE42428-0C82-42FC-923F-7314A740D04F}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `xinco`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{249C3090-F2CF-4C6B-A528-C9DDF697E702}`
    FOREIGN KEY (`xinco_core_language_id` )
    REFERENCES `xinco`.`xinco_core_language` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{592A6D87-0049-4758-9D7F-D4F421983CDB}`
    FOREIGN KEY (`xinco_core_data_type_id` )
    REFERENCES `xinco`.`xinco_core_data_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_group` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_group` (
  `id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_group_index_status` (`status_number` ASC) ,
  UNIQUE INDEX `unique name` (`designation` ASC) )
COMMENT = 'Status:  \nopen = 1  \nlocked = 2  \n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_user` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user` (
  `id` INT UNSIGNED NOT NULL ,
  `username` VARCHAR(255) NOT NULL ,
  `userpassword` VARCHAR(255) NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `firstname` VARCHAR(255) NOT NULL ,
  `email` VARCHAR(255) NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  `attempts` INT UNSIGNED NOT NULL DEFAULT 0 ,
  `last_modified` DATE NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_user_index_username` (`username` ASC) ,
  INDEX `xinco_core_user_index_status` (`status_number` ASC) ,
  UNIQUE INDEX `unique id` (`username` ASC) )
COMMENT = '\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_ace`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_ace` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_ace` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT UNSIGNED NULL ,
  `xinco_core_group_id` INT UNSIGNED NULL ,
  `xinco_core_node_id` INT UNSIGNED NULL ,
  `xinco_core_data_id` INT UNSIGNED NULL ,
  `read_permission` TINYINT(1)  NOT NULL ,
  `write_permission` TINYINT(1)  NOT NULL ,
  `execute_permission` TINYINT(1)  NOT NULL ,
  `admin_permission` TINYINT(1)  NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_ace_FKIndex2` (`xinco_core_group_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex3` (`xinco_core_node_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex4` (`xinco_core_data_id` ASC) ,
  INDEX `fk_xinco_core_ace_xinco_core_user1` (`xinco_core_user_id` ASC) ,
  CONSTRAINT `fk_{BE5919E0-4B49-4754-861D-E7834EB59238}`
    FOREIGN KEY (`xinco_core_group_id` )
    REFERENCES `xinco`.`xinco_core_group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{8CCF024C-2E44-468D-B847-EF00897639E3}`
    FOREIGN KEY (`xinco_core_node_id` )
    REFERENCES `xinco`.`xinco_core_node` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{4F26567D-38D8-4210-86D6-524AE418D6EB}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_xinco_core_ace_xinco_core_user1`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_user_has_xinco_core_group` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_user_has_xinco_core_group` (
  `xinco_core_user_id` INT UNSIGNED NOT NULL ,
  `xinco_core_group_id` INT UNSIGNED NOT NULL ,
  `status_number` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`xinco_core_user_id`, `xinco_core_group_id`) ,
  INDEX `xinco_core_user_has_xinco_core_group_FKIndex2` (`xinco_core_group_id` ASC) ,
  INDEX `xinco_core_user_has_xinco_core_group_index_status` (`status_number` ASC) ,
  INDEX `fk_xinco_core_user_has_xinco_core_group_xinco_core_user1` (`xinco_core_user_id` ASC) ,
  CONSTRAINT `fk_{BB4D6DB4-7E25-4F88-801D-A715A8A28E33}`
    FOREIGN KEY (`xinco_core_group_id` )
    REFERENCES `xinco`.`xinco_core_group` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_xinco_core_user_has_xinco_core_group_xinco_core_user1`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = '\n'
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_add_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_add_attribute` ;

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
  CONSTRAINT `fk_{DEA334D0-7F8F-4E6B-81EA-671A6C493FCC}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_type_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_type_attribute` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_type_attribute` (
  `xinco_core_data_type_id` INT UNSIGNED NOT NULL ,
  `attribute_id` INT UNSIGNED NOT NULL ,
  `designation` VARCHAR(255) NOT NULL ,
  `data_type` VARCHAR(255) NOT NULL ,
  `attr_size` INT UNSIGNED NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`xinco_core_data_type_id`, `attribute_id`) ,
  INDEX `xinco_core_data_type_attribute_FKIndex1` (`xinco_core_data_type_id` ASC) ,
  CONSTRAINT `fk_{E75392C0-42A1-47BE-8007-549B123B7775}`
    FOREIGN KEY (`xinco_core_data_type_id` )
    REFERENCES `xinco`.`xinco_core_data_type` (`id` )
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_log` ;

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
  INDEX `fk_xinco_core_log_xinco_core_user1` (`xinco_core_user_id` ASC) ,
  CONSTRAINT `fk_{E4C7D7F8-39B7-48D1-B9EE-BD9DE0D2CB4A}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_xinco_core_log_xinco_core_user1`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
PACK_KEYS = 0
ROW_FORMAT = DEFAULT;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_setting`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_setting` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_setting` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(45) NOT NULL ,
  `int_value` INT UNSIGNED NULL DEFAULT NULL ,
  `string_value` TEXT NULL DEFAULT NULL ,
  `bool_value` TINYINT(1)  NULL ,
  `long_value` BIGINT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique` (`description` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_setting_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_setting_t` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_setting_t` (
  `record_id` INT(10) NOT NULL ,
  `id` INT NOT NULL ,
  `description` VARCHAR(45) NOT NULL ,
  `int_value` INT UNSIGNED NULL DEFAULT NULL ,
  `string_value` TEXT NULL DEFAULT NULL ,
  `bool_value` TINYINT(1)  NULL ,
  `long_value` BIGINT NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_dependency_behavior`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_dependency_behavior` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_behavior` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `Unique` (`designation` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_dependency_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_dependency_type` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_type` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `xinco_dependency_behavior_id` INT NOT NULL ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `designation_UNIQUE` (`designation` ASC) ,
  INDEX `fk_xinco_dependency_type_xinco_dependency_behavior1` (`xinco_dependency_behavior_id` ASC) ,
  CONSTRAINT `fk_xinco_dependency_type_xinco_dependency_behavior1`
    FOREIGN KEY (`xinco_dependency_behavior_id` )
    REFERENCES `xinco`.`xinco_dependency_behavior` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_has_dependency`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_has_dependency` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_has_dependency` (
  `xinco_core_data_parent_id` INT UNSIGNED NOT NULL ,
  `xinco_core_data_children_id` INT UNSIGNED NOT NULL ,
  `dependency_type_id` INT NOT NULL ,
  PRIMARY KEY (`xinco_core_data_parent_id`, `xinco_core_data_children_id`, `dependency_type_id`) ,
  INDEX `fk_xinco_core_data_has_xinco_core_data_xinco_core_data1` (`xinco_core_data_parent_id` ASC) ,
  INDEX `fk_xinco_core_data_has_xinco_core_data_xinco_core_data2` (`xinco_core_data_children_id` ASC) ,
  INDEX `fk_xinco_core_data_has_dependency_dependency_type1` (`dependency_type_id` ASC) ,
  CONSTRAINT `fk_xinco_core_data_has_xinco_core_data_xinco_core_data1`
    FOREIGN KEY (`xinco_core_data_parent_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_core_data_has_xinco_core_data_xinco_core_data2`
    FOREIGN KEY (`xinco_core_data_children_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_core_data_has_dependency_dependency_type1`
    FOREIGN KEY (`dependency_type_id` )
    REFERENCES `xinco`.`xinco_dependency_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `xinco`.`xinco_dependency_type_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_dependency_type_t` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_type_t` (
  `record_id` INT(10) NOT NULL ,
  `id` INT NOT NULL ,
  `xinco_dependency_behavior_id` INT NOT NULL ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_core_data_has_dependency_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_data_has_dependency_t` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_has_dependency_t` (
  `record_id` INT(10) NOT NULL ,
  `xinco_core_data_parent_id` INT UNSIGNED NOT NULL ,
  `xinco_core_data_children_id` INT UNSIGNED NOT NULL ,
  `dependency_type_id` INT NOT NULL ,
  PRIMARY KEY (`record_id`) );


-- -----------------------------------------------------
-- Table `xinco`.`xinco_id`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_id` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_id` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `tablename` VARCHAR(255) NOT NULL ,
  `last_id` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 72
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci
PACK_KEYS = 0;


-- -----------------------------------------------------
-- Table `xinco`.`xinco_dependency_behavior_t`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_dependency_behavior_t` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_behavior_t` (
  `record_id` INT(10) NOT NULL ,
  `id` INT NOT NULL ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB;

USE `xinco_workflow` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_language`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('1', 'n/a', 'unknown');
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('2', 'en', 'English');
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('3', 'de', 'German');
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('4', 'fr', 'French');
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('5', 'it', 'Italian');
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('6', 'es', 'Spanish');
INSERT INTO `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) VALUES ('7', 'ru', 'Russian');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_node`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('1', NULL, '1', 'xincoRoot', '1');
INSERT INTO `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('2', '1', '1', 'Trash', '1');
INSERT INTO `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('3', '1', '1', 'Temp', '1');
INSERT INTO `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('4', '1', '1', 'News', '1');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_data_type`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('1', 'general.data.type.file', 'general.data.type.file.description');
INSERT INTO `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('2', 'general.data.type.text', 'general.data.type.text.description');
INSERT INTO `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('3', 'general.data.type.URL', 'general.data.type.URL.description');
INSERT INTO `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('4', 'general.data.type.contact', 'general.data.type.contact.description');
INSERT INTO `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('5', 'general.data.type.rendering', 'general.data.type.rendering.description');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_data`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_data` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) VALUES ('1', '1', '2', '3', 'Apache License 2.0', '5');
INSERT INTO `xinco`.`xinco_core_data` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) VALUES ('2', '1', '2', '3', 'xinco.org', '1');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_group`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_group` (`id`, `designation`, `status_number`) VALUES ('1', 'general.group.admin', '1');
INSERT INTO `xinco`.`xinco_core_group` (`id`, `designation`, `status_number`) VALUES ('2', 'general.group.allusers', '1');
INSERT INTO `xinco`.`xinco_core_group` (`id`, `designation`, `status_number`) VALUES ('3', 'general.group.public', '1');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_user`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES ('1', 'admin', MD5('admin'), 'Administrator', 'Xinco', 'admin@xinco.org', '1', '0', now());
INSERT INTO `xinco`.`xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES ('2', 'user', MD5('user'), 'User', 'Default', 'user@xinco.org', '1', '0', now());
INSERT INTO `xinco`.`xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES ('3', 'bluecubs', MD5('system'), 'System', 'User', 'info@bluecubs.com', '1', '0', now());

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_ace`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('1', '1', NULL, '1', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('2', '1', NULL, '2', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('3', '1', NULL, '3', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('4', NULL, '1', '1', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('5', NULL, '1', '2', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('6', NULL, '1', '3', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('7', NULL, '2', '1', NULL, 1, 1, 1, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('8', NULL, '2', '2', NULL, 1, 1, 1, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('9', NULL, '2', '3', NULL, 1, 1, 1, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('10', '1', NULL, NULL, '1', 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('11', '1', NULL, NULL, '2', 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('12', NULL, '1', NULL, '1', 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('13', NULL, '1', NULL, '2', 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('14', NULL, '2', NULL, '1', 1, 0, 0, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('15', NULL, '2', NULL, '2', 1, 0, 0, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('16', '1', NULL, '4', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('17', NULL, '1', '4', NULL, 1, 1, 1, 1);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('18', NULL, '2', '4', NULL, 1, 0, 0, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('19', NULL, '3', '1', NULL, 1, 0, 0, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('20', NULL, '3', '4', NULL, 1, 0, 0, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('21', NULL, '3', NULL, '1', 1, 0, 0, 0);
INSERT INTO `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('22', NULL, '3', NULL, '2', 1, 0, 0, 0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('1', '1', '1');
INSERT INTO `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('1', '2', '1');
INSERT INTO `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('2', '2', '1');
INSERT INTO `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('3', '2', '1');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_add_attribute`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_add_attribute` (`xinco_core_data_id`, `attribute_id`, `attrib_int`, `attrib_unsignedint`, `attrib_double`, `attrib_varchar`, `attrib_text`, `attrib_datetime`) VALUES ('2', '1', '0', '0', '0', 'http://www.xinco.org', '', now());
INSERT INTO `xinco`.`xinco_add_attribute` (`xinco_core_data_id`, `attribute_id`, `attrib_int`, `attrib_unsignedint`, `attrib_double`, `attrib_varchar`, `attrib_text`, `attrib_datetime`) VALUES ('1', '1', '0', '0', '0', 'http://www.apache.org/licenses/LICENSE-2.0.html', '', now());

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_data_type_attribute`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '1', 'general.filename', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '2', 'general.size', 'unsignedint', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '3', 'general.checksum', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '4', 'general.revision.model', 'unsignedint', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '5', 'general.archive.model', 'unsignedint', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '6', 'general.archive.date', 'datetime', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '7', 'general.archive.days', 'unsignedint', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '8', 'general.archive.location', 'text', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '9', 'general.description', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '10', 'Keyword_1', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '11', 'Keyword_2', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '12', 'Keyword_3', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('2', '1', 'general.data.type.text', 'text', '0');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('3', '1', 'general.data.type.URL', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '1', 'general.salutation', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('5', '1', 'general.format', 'varchar', '45');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '2', 'general.name', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '3', 'general.middle_name', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '4', 'general.last_name', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '5', 'general.name_affix', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '6', 'general.phone_business', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '7', 'general.phone_private', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '8', 'general.phone_mobile', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '9', 'general.fax', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '10', 'general.email', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '11', 'general.website', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '12', 'general.steet_address', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '13', 'general.postal_code', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '14', 'general.city', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '15', 'general.state_province', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '16', 'general.country', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '17', 'general.company_name', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '18', 'general.position', 'varchar', '255');
INSERT INTO `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '19', 'general.notes', 'text', '0');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_log`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) VALUES ('1', '1', '1', '1', now(), 'Creation!', '1', '0', '0', '');
INSERT INTO `xinco`.`xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) VALUES ('2', '2', '1', '1', now(), 'Creation!', '1', '0', '0', '');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_setting`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('1', 'password.aging', '120', NULL, 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('2', 'password.attempts', '3', NULL, 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('3', 'password.unusable_period', '365', NULL, 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('4', 'general.copyright.date', '0', '2004-2009', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('5', 'setting.enable.savepassword', NULL, NULL, 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('6', 'system.password', NULL, 'system', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('7', 'xinco/FileRepositoryPath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('8', 'xinco/FileIndexPath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\index\\\\', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('9', 'xinco/FileArchivePath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\archive\\\\', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('10', 'xinco/FileArchivePeriod', NULL, NULL, 0, '14400000');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('11', 'xinco/FileIndexer_1_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('12', 'xinco/FileIndexer_1_Ext', NULL, 'pdf', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('13', 'xinco/FileIndexer_2_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftWord', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('14', 'xinco/FileIndexer_2_Ext', NULL, 'doc', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('15', 'xinco/FileIndexer_3_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftExcel', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('16', 'xinco/FileIndexer_3_Ext', NULL, 'xls', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('17', 'xinco/FileIndexer_4_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftPowerpoint', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('18', 'xinco/FileIndexer_4_Ext', NULL, 'ppt', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('19', 'xinco/FileIndexer_5_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexHTML', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('20', 'xinco/FileIndexer_5_Ext', NULL, 'asp;htm;html;jsf;jsp;php;php3;php4', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('21', 'xinco/IndexNoIndex', NULL, ';aac;ac3;ace;ade;adp;aif;aifc;aiff;amf;arc;arj;asx;au;avi;b64;bh;bmp;bz;bz2;cab;cda;chm;class;com;div;divx;ear;exe;far;fla;gif;gz;hlp;ico;;iso;jar;jpe;jpeg;jpg;lha;lzh;mda;mdb;mde;mdn;mdt;mdw;mid;midi;mim;mod;mov;mp1;mp2;mp2v;mp3;mp4;mpa;mpe;mpeg;mpg;mpg4;mpv2;msi;ntx;ocx;ogg;ogm;okt;pae;pcx;pk3;png;pot;ppa;pps;ppt;pwz;qwk;ra;ram;rar;raw;rep;rm;rmi;snd;swf;swt;tar;taz;tbz;tbz2;tgz;tif;tiff;uu;uue;vxd;war;wav;wbm;wbmp;wma;wmd;wmf;wmv;xpi;xxe;z;zip;zoo;;;', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('22', 'xinco/MaxSearchResult', '100', NULL, 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('23', 'setting.email.host', NULL, 'smtp.bluecubs.com', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('24', 'setting.email.user', NULL, 'info@bluecubs.com', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('25', 'setting.email.password', NULL, 'password', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('26', 'setting.email.port', '25', NULL, 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('27', 'setting.allowoutsidelinks', NULL, NULL, 1, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('28', 'setting.backup.path', NULL, 'C:\\\\Temp\\\\xinco\\\\backup\\\\', 0, '0');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('29', 'xinco/FileIndexOptimizerPeriod', NULL, NULL, 0, '14400000');
INSERT INTO `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('30', 'setting.allowpublisherlist', NULL, NULL, 1, '0');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_dependency_behavior`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_dependency_behavior` (`id`, `designation`, `description`) VALUES ('1', 'dependency.behavior.one-way', 'dependency.behavior.one-way.desc');
INSERT INTO `xinco`.`xinco_dependency_behavior` (`id`, `designation`, `description`) VALUES ('2', 'dependency.behavior.two-way', 'dependency.behavior.two-way.desc');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_dependency_type`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco`;
INSERT INTO `xinco`.`xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('1', '2', 'dependency.related', 'dependency.related.desc');
INSERT INTO `xinco`.`xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('2', '1', 'dependency.component', 'dependency.component.desc');
INSERT INTO `xinco`.`xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('3', '2', 'dependency.group', 'dependency.group.desc');
INSERT INTO `xinco`.`xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('4', '2', 'dependency.package', 'dependency.package.desc');
INSERT INTO `xinco`.`xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('5', '1', 'dependency.rendering', 'dependency.rendering.desc');
INSERT INTO `xinco`.`xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('6', '1', 'dependency.supporting', 'dependency.supporting.desc');

COMMIT;
