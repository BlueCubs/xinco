SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `xinco` ;
CREATE SCHEMA IF NOT EXISTS `xinco` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
DROP SCHEMA IF EXISTS `xinco_workflow` ;
CREATE SCHEMA IF NOT EXISTS `xinco_workflow` DEFAULT CHARACTER SET utf8 ;

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
  `read_permission` TINYINT(1) NOT NULL ,
  `write_permission` TINYINT(1) NOT NULL ,
  `execute_permission` TINYINT(1) NOT NULL ,
  `admin_permission` TINYINT(1) NOT NULL ,
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
COMMENT = 'Status: \nopen = 1 \nlocked = 2 \n'
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
COMMENT = 'Status:\nopen = 1\nlocked = 2\narchived = 3\n'
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
COMMENT = 'Status: \nopen = 1 \nlocked = 2 \n'
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
-- Table `xinco`.`xinco_core_ace`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco`.`xinco_core_ace` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_ace` (
  `id` INT UNSIGNED NOT NULL ,
  `xinco_core_user_id` INT UNSIGNED NULL ,
  `xinco_core_group_id` INT UNSIGNED NULL ,
  `xinco_core_node_id` INT UNSIGNED NULL ,
  `xinco_core_data_id` INT UNSIGNED NULL ,
  `read_permission` TINYINT(1) NOT NULL ,
  `write_permission` TINYINT(1) NOT NULL ,
  `execute_permission` TINYINT(1) NOT NULL ,
  `admin_permission` TINYINT(1) NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `xinco_core_ace_FKIndex1` (`xinco_core_user_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex2` (`xinco_core_group_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex3` (`xinco_core_node_id` ASC) ,
  INDEX `xinco_core_ace_FKIndex4` (`xinco_core_data_id` ASC) ,
  CONSTRAINT `fk_{A5B19221-5358-4C98-82CC-581CD695C068}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
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
    ON UPDATE CASCADE)
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
  INDEX `xinco_core_user_has_xinco_core_group_FKIndex1` (`xinco_core_user_id` ASC) ,
  INDEX `xinco_core_user_has_xinco_core_group_FKIndex2` (`xinco_core_group_id` ASC) ,
  INDEX `xinco_core_user_has_xinco_core_group_index_status` (`status_number` ASC) ,
  CONSTRAINT `fk_{E9CE4AC9-ECE3-48EF-9C4A-081FB0A9FD96}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{BB4D6DB4-7E25-4F88-801D-A715A8A28E33}`
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
DROP TABLE IF EXISTS `xinco`.`xinco_id` ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_id` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `tablename` VARCHAR(255) NOT NULL ,
  `last_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) )
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
  INDEX `xinco_core_log_FKIndex2` (`xinco_core_user_id` ASC) ,
  CONSTRAINT `fk_{E4C7D7F8-39B7-48D1-B9EE-BD9DE0D2CB4A}`
    FOREIGN KEY (`xinco_core_data_id` )
    REFERENCES `xinco`.`xinco_core_data` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_{1B8A7333-846D-4DA1-A8B4-D730053B6871}`
    FOREIGN KEY (`xinco_core_user_id` )
    REFERENCES `xinco`.`xinco_core_user` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
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
  `bool_value` TINYINT(1) NULL ,
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
  `bool_value` TINYINT(1) NULL ,
  `long_value` BIGINT NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`user_link`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`user_link` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`user_link` (
  `id` INT(11) NOT NULL ,
  `system_id` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_id` (`system_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_workflow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_workflow` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_workflow` (
  `id` INT(11) NOT NULL ,
  `version` INT(11) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `description` TEXT NULL DEFAULT NULL ,
  `user_link_id` INT(11) NOT NULL ,
  PRIMARY KEY (`id`, `version`, `user_link_id`) ,
  UNIQUE INDEX `unique name` (`name` ASC) ,
  INDEX `fk_xinco_workflow_user_link1` (`user_link_id` ASC) ,
  CONSTRAINT `fk_xinco_workflow_user_link1`
    FOREIGN KEY (`user_link_id` )
    REFERENCES `xinco_workflow`.`user_link` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_state_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_state_type` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_state_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique name` (`name` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_workflow_state`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_workflow_state` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_workflow_state` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `xinco_workflow_id` INT(11) NOT NULL ,
  `xinco_workflow_version` INT(11) NOT NULL ,
  `name` VARCHAR(45) NULL DEFAULT NULL ,
  `xinco_state_type_id` INT(11) NOT NULL ,
  PRIMARY KEY (`id`, `xinco_workflow_id`, `xinco_workflow_version`) ,
  INDEX `fk_xinco_state_xinco_workflow1` (`xinco_workflow_id` ASC, `xinco_workflow_version` ASC) ,
  INDEX `fk_xinco_state_xinco_state_type1` (`xinco_state_type_id` ASC) ,
  CONSTRAINT `fk_xinco_state_xinco_workflow1`
    FOREIGN KEY (`xinco_workflow_id` , `xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_workflow` (`id` , `version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_state_xinco_state_type1`
    FOREIGN KEY (`xinco_state_type_id` )
    REFERENCES `xinco_workflow`.`xinco_state_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_state_transitions_to_xinco_state`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_state_transitions_to_xinco_state` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_state_transitions_to_xinco_state` (
  `source_state_id` INT(11) NOT NULL ,
  `source_xinco_state_xinco_workflow_id` INT(11) NOT NULL ,
  `source_xinco_state_xinco_workflow_version` INT(11) NOT NULL ,
  `destination_state_id` INT(11) NOT NULL ,
  `dest_xinco_state_xinco_workflow_id1` INT(11) NOT NULL ,
  `dest_xinco_state_xinco_workflow_version1` INT(11) NOT NULL ,
  PRIMARY KEY (`source_state_id`, `source_xinco_state_xinco_workflow_id`, `source_xinco_state_xinco_workflow_version`, `destination_state_id`, `dest_xinco_state_xinco_workflow_id1`, `dest_xinco_state_xinco_workflow_version1`) ,
  INDEX `fk_xinco_state_has_xinco_state_xinco_state1` (`source_state_id` ASC, `source_xinco_state_xinco_workflow_id` ASC, `source_xinco_state_xinco_workflow_version` ASC) ,
  INDEX `fk_xinco_state_has_xinco_state_xinco_state2` (`destination_state_id` ASC, `dest_xinco_state_xinco_workflow_id1` ASC, `dest_xinco_state_xinco_workflow_version1` ASC) ,
  CONSTRAINT `fk_xinco_state_has_xinco_state_xinco_state1`
    FOREIGN KEY (`source_state_id` , `source_xinco_state_xinco_workflow_id` , `source_xinco_state_xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_workflow_state` (`id` , `xinco_workflow_id` , `xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_state_has_xinco_state_xinco_state2`
    FOREIGN KEY (`destination_state_id` , `dest_xinco_state_xinco_workflow_id1` , `dest_xinco_state_xinco_workflow_version1` )
    REFERENCES `xinco_workflow`.`xinco_workflow_state` (`id` , `xinco_workflow_id` , `xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_action` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_action` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `implementation_class` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique name` (`name` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Configuration of actions to be performed.';


-- -----------------------------------------------------
-- Table `xinco_workflow`.`transition_has_xinco_action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`transition_has_xinco_action` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`transition_has_xinco_action` (
  `source_state_id` INT(11) NOT NULL ,
  `source_xinco_workflow_id` INT(11) NOT NULL ,
  `source_xinco_workflow_version` INT(11) NOT NULL ,
  `destination_state_id` INT(11) NOT NULL ,
  `dest_xinco_workflow_id` INT(11) NOT NULL ,
  `dest_workflow_version` INT(11) NOT NULL ,
  `xinco_action_id` INT(11) NOT NULL ,
  PRIMARY KEY (`source_state_id`, `source_xinco_workflow_id`, `source_xinco_workflow_version`, `destination_state_id`, `dest_xinco_workflow_id`, `dest_workflow_version`, `xinco_action_id`) ,
  INDEX `fk_xinco_state_transitions_to_xinco_state_has_xinco_action_xi1` (`source_state_id` ASC, `source_xinco_workflow_id` ASC, `source_xinco_workflow_version` ASC, `destination_state_id` ASC, `dest_xinco_workflow_id` ASC, `dest_workflow_version` ASC) ,
  INDEX `fk_xinco_state_transitions_to_xinco_state_has_xinco_action_xi2` (`xinco_action_id` ASC) ,
  CONSTRAINT `fk_xinco_state_transitions_to_xinco_state_has_xinco_action_xi1`
    FOREIGN KEY (`source_state_id` , `source_xinco_workflow_id` , `source_xinco_workflow_version` , `destination_state_id` , `dest_xinco_workflow_id` , `dest_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_state_transitions_to_xinco_state` (`source_state_id` , `source_xinco_state_xinco_workflow_id` , `source_xinco_state_xinco_workflow_version` , `destination_state_id` , `dest_xinco_state_xinco_workflow_id1` , `dest_xinco_state_xinco_workflow_version1` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_state_transitions_to_xinco_state_has_xinco_action_xi2`
    FOREIGN KEY (`xinco_action_id` )
    REFERENCES `xinco_workflow`.`xinco_action` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`transition_has_xinco_core_user_restriction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`transition_has_xinco_core_user_restriction` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`transition_has_xinco_core_user_restriction` (
  `source_state_id` INT(11) NOT NULL ,
  `source_xinco_workflow_id` INT(11) NOT NULL ,
  `source_xinco_workflow_version` INT(11) NOT NULL ,
  `destination_state_id` INT(11) NOT NULL ,
  `dest_xinco_workflow_id` INT(11) NOT NULL ,
  `dest_xinco_workflow_version` INT(11) NOT NULL ,
  `user_link_id` INT(11) NOT NULL ,
  PRIMARY KEY (`source_state_id`, `source_xinco_workflow_id`, `source_xinco_workflow_version`, `destination_state_id`, `dest_xinco_workflow_id`, `dest_xinco_workflow_version`, `user_link_id`) ,
  INDEX `fk_xinco_state_transitions_to_xinco_state_has_xinco_core_user1` (`source_state_id` ASC, `source_xinco_workflow_id` ASC, `source_xinco_workflow_version` ASC, `destination_state_id` ASC, `dest_xinco_workflow_id` ASC, `dest_xinco_workflow_version` ASC) ,
  INDEX `fk_transition_has_xinco_core_user_restriction_user_link1` (`user_link_id` ASC) ,
  CONSTRAINT `fk_xinco_state_transitions_to_xinco_state_has_xinco_core_user1`
    FOREIGN KEY (`source_state_id` , `source_xinco_workflow_id` , `source_xinco_workflow_version` , `destination_state_id` , `dest_xinco_workflow_id` , `dest_xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_state_transitions_to_xinco_state` (`source_state_id` , `source_xinco_state_xinco_workflow_id` , `source_xinco_state_xinco_workflow_version` , `destination_state_id` , `dest_xinco_state_xinco_workflow_id1` , `dest_xinco_state_xinco_workflow_version1` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transition_has_xinco_core_user_restriction_user_link1`
    FOREIGN KEY (`user_link_id` )
    REFERENCES `xinco_workflow`.`user_link` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_parameter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_parameter` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_parameter` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `xinco_action_id` INT(11) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `value_type` VARCHAR(45) NOT NULL ,
  `value` VARCHAR(255) NOT NULL ,
  PRIMARY KEY (`id`, `xinco_action_id`) ,
  INDEX `fk_xinco_parameter_xinco_action1` (`xinco_action_id` ASC) ,
  CONSTRAINT `fk_xinco_parameter_xinco_action1`
    FOREIGN KEY (`xinco_action_id` )
    REFERENCES `xinco_workflow`.`xinco_action` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_state_has_actors`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_state_has_actors` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_state_has_actors` (
  `xinco_state_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_version` INT(11) NOT NULL ,
  `user_link_id` INT(11) NOT NULL ,
  PRIMARY KEY (`xinco_state_id`, `xinco_state_xinco_workflow_id`, `xinco_state_xinco_workflow_version`, `user_link_id`) ,
  INDEX `fk_xinco_state_has_xinco_core_user_xinco_state1` (`xinco_state_id` ASC, `xinco_state_xinco_workflow_id` ASC, `xinco_state_xinco_workflow_version` ASC) ,
  INDEX `fk_xinco_state_has_actors_user_link1` (`user_link_id` ASC) ,
  CONSTRAINT `fk_xinco_state_has_xinco_core_user_xinco_state1`
    FOREIGN KEY (`xinco_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_workflow_state` (`id` , `xinco_workflow_id` , `xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_state_has_actors_user_link1`
    FOREIGN KEY (`user_link_id` )
    REFERENCES `xinco_workflow`.`user_link` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_state_has_xinco_action`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_state_has_xinco_action` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_state_has_xinco_action` (
  `xinco_state_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_version` INT(11) NOT NULL ,
  `xinco_action_id` INT(11) NOT NULL ,
  PRIMARY KEY (`xinco_state_id`, `xinco_state_xinco_workflow_id`, `xinco_state_xinco_workflow_version`, `xinco_action_id`) ,
  INDEX `fk_xinco_state_has_xinco_action_xinco_state1` (`xinco_state_id` ASC, `xinco_state_xinco_workflow_id` ASC, `xinco_state_xinco_workflow_version` ASC) ,
  INDEX `fk_xinco_state_has_xinco_action_xinco_action1` (`xinco_action_id` ASC) ,
  CONSTRAINT `fk_xinco_state_has_xinco_action_xinco_state1`
    FOREIGN KEY (`xinco_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_workflow_state` (`id` , `xinco_workflow_id` , `xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_state_has_xinco_action_xinco_action1`
    FOREIGN KEY (`xinco_action_id` )
    REFERENCES `xinco_workflow`.`xinco_action` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_workflow_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_workflow_item` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_workflow_item` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `creation_date` DATETIME NOT NULL ,
  `completion_date` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_work_item_has_xinco_state`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_work_item_has_xinco_state` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_work_item_has_xinco_state` (
  `xinco_work_item_id` INT(11) NOT NULL ,
  `xinco_state_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_version` INT(11) NOT NULL ,
  `sequence` INT(11) NOT NULL DEFAULT '1' ,
  `state_reached_date` DATETIME NOT NULL ,
  PRIMARY KEY (`xinco_work_item_id`, `xinco_state_id`, `xinco_state_xinco_workflow_id`, `xinco_state_xinco_workflow_version`) ,
  INDEX `fk_xinco_work_item_has_xinco_state_xinco_work_item1` (`xinco_work_item_id` ASC) ,
  INDEX `fk_xinco_work_item_has_xinco_state_xinco_state1` (`xinco_state_id` ASC, `xinco_state_xinco_workflow_id` ASC, `xinco_state_xinco_workflow_version` ASC) ,
  CONSTRAINT `fk_xinco_work_item_has_xinco_state_xinco_work_item1`
    FOREIGN KEY (`xinco_work_item_id` )
    REFERENCES `xinco_workflow`.`xinco_workflow_item` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_work_item_has_xinco_state_xinco_state1`
    FOREIGN KEY (`xinco_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_workflow_state` (`id` , `xinco_workflow_id` , `xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'This will hold the history od this work item in the workflow';


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_work_item_parameter`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_work_item_parameter` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_work_item_parameter` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `xinco_work_item_id` INT(11) NOT NULL ,
  `xinco_state_id` INT(11) NOT NULL ,
  `xinco_workflow_id` INT(11) NOT NULL ,
  `xinco_workflow_version` INT(11) NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `value_type` VARCHAR(45) NOT NULL ,
  `value` BLOB NOT NULL ,
  PRIMARY KEY (`id`, `xinco_work_item_id`, `xinco_state_id`, `xinco_workflow_id`, `xinco_workflow_version`) ,
  INDEX `fk_xinco_work_item_parameter_xinco_work_item_has_xinco_state1` (`xinco_work_item_id` ASC, `xinco_state_id` ASC, `xinco_workflow_id` ASC, `xinco_workflow_version` ASC) ,
  CONSTRAINT `fk_xinco_work_item_parameter_xinco_work_item_has_xinco_state1`
    FOREIGN KEY (`xinco_work_item_id` , `xinco_state_id` , `xinco_workflow_id` , `xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_work_item_has_xinco_state` (`xinco_work_item_id` , `xinco_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'This will hold the item\'s parameters in this state.';


-- -----------------------------------------------------
-- Table `xinco_workflow`.`xinco_workflow_id`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `xinco_workflow`.`xinco_workflow_id` ;

CREATE  TABLE IF NOT EXISTS `xinco_workflow`.`xinco_workflow_id` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `tablename` VARCHAR(255) NOT NULL ,
  `last_id` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8
PACK_KEYS = 0;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_language`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (1, 'n/a', 'unknown');
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (2, 'en', 'English');
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (3, 'de', 'German');
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (4, 'fr', 'French');
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (5, 'it', 'Italian');
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (6, 'es', 'Spanish');
insert into `xinco`.`xinco_core_language` (`id`, `sign`, `designation`) values (7, 'ru', 'Russian');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_node`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) values (1, NULL, 1, 'xincoRoot', 1);
insert into `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) values (2, 1, 1, 'Trash', 1);
insert into `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) values (3, 1, 1, 'Temp', 1);
insert into `xinco`.`xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) values (4, 1, 1, 'News', 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_data_type`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) values (1, 'general.data.type.file', 'general.data.type.file.description');
insert into `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) values (2, 'general.data.type.text', 'general.data.type.text.description');
insert into `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) values (3, 'general.data.type.URL', 'general.data.type.URL.description');
insert into `xinco`.`xinco_core_data_type` (`id`, `designation`, `description`) values (4, 'general.data.type.contact', 'general.data.type.contact.description');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_data`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_data` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) values (1, 1, 2, 3, 'Apache License 2.0', 5);
insert into `xinco`.`xinco_core_data` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) values (2, 1, 2, 3, 'xinco.org', 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_user`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) values (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator', 'Xinco', 'admin@xinco.org', 1, 0, now());
insert into `xinco`.`xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) values (2, 'user', 'ee11cbb19052e40b07aac0ca060c23ee', 'User', 'Default', 'user@xinco.org', 1, 0, now());
insert into `xinco`.`xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) values (3, 'bluecubs', 'a1681d95b94018a6721432c774bcef13', 'System', 'User', 'info@bluecubs.com', 1, 0, now());

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_group`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_group` (`id`, `designation`, `status_number`) values (1, 'general.group.admin', 1);
insert into `xinco`.`xinco_core_group` (`id`, `designation`, `status_number`) values (2, 'general.group.allusers', 1);
insert into `xinco`.`xinco_core_group` (`id`, `designation`, `status_number`) values (3, 'general.group.public', 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_ace`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (1, 1, NULL, 1, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (2, 1, NULL, 2, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (3, 1, NULL, 3, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (4, NULL, 1, 1, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (5, NULL, 1, 2, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (6, NULL, 1, 3, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (7, NULL, 2, 1, NULL, 1, 1, 1, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (8, NULL, 2, 2, NULL, 1, 1, 1, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (9, NULL, 2, 3, NULL, 1, 1, 1, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (10, 1, NULL, NULL, 1, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (11, 1, NULL, NULL, 2, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (12, NULL, 1, NULL, 1, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (13, NULL, 1, NULL, 2, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (14, NULL, 2, NULL, 1, 1, 0, 0, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (15, NULL, 2, NULL, 2, 1, 0, 0, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (16, 1, NULL, 4, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (17, NULL, 1, 4, NULL, 1, 1, 1, 1);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (18, NULL, 2, 4, NULL, 1, 0, 0, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (19, NULL, 3, 1, NULL, 1, 0, 0, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (20, NULL, 3, 4, NULL, 1, 0, 0, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (21, NULL, 3, NULL, 1, 1, 0, 0, 0);
insert into `xinco`.`xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) values (22, NULL, 3, NULL, 2, 1, 0, 0, 0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) values (1, 1, 1);
insert into `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) values (1, 2, 1);
insert into `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) values (2, 2, 1);
insert into `xinco`.`xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) values (3, 2, 1);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_id`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (1, 'xinco_core_language', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (2, 'xinco_core_data_type', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (3, 'xinco_core_user', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (4, 'xinco_core_group', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (5, 'xinco_core_node', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (6, 'xinco_core_data', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (7, 'xinco_core_ace', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (8, 'xinco_core_log', 1000);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (9, 'xinco_core_user_modified_record', 0);
insert into `xinco`.`xinco_id` (`id`, `tablename`, `last_id`) values (10, 'xinco_setting', 1000);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_add_attribute`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_add_attribute` (`xinco_core_data_id`, `attribute_id`, `attrib_int`, `attrib_unsignedint`, `attrib_double`, `attrib_varchar`, `attrib_text`, `attrib_datetime`) values (2, 1, 0, 0, 0, 'http://www.xinco.org', '', now());
insert into `xinco`.`xinco_add_attribute` (`xinco_core_data_id`, `attribute_id`, `attrib_int`, `attrib_unsignedint`, `attrib_double`, `attrib_varchar`, `attrib_text`, `attrib_datetime`) values (1, 1, 0, 0, 0, 'http://www.apache.org/licenses/LICENSE-2.0.html', '', now());

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_data_type_attribute`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 1, 'File Name', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 2, 'Size', 'unsignedint', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 3, 'Checksum', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 4, 'Revision_Model', 'unsignedint', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 5, 'Archiving_Model', 'unsignedint', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 6, 'Archiving_Date', 'datetime', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 7, 'Archiving_Days', 'unsignedint', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 8, 'Archiving_Location', 'text', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 9, 'Description', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 10, 'Keyword_1', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 11, 'Keyword_2', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (1, 12, 'Keyword_3', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (2, 1, 'Text', 'text', 0);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (3, 1, 'URL', 'varchar', 255);
insert into `xinco`.`xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) values (4, 1, 'Salutation', 'varchar', 255);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_core_log`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) values (1, 1, 1, 1, now(), 'Creation!', 1, 0, 0, '');
insert into `xinco`.`xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) values (2, 2, 1, 1, now(), 'Creation!', 1, 0, 0, '');

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco`.`xinco_setting`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (1, 'password.aging', 120, NULL, 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (2, 'password.attempts', 3, NULL, 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (3, 'password.unusable_period', 365, NULL, 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (4, 'general.copyright.date', 0, '2004-2009', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (5, 'setting.enable.savepassword', NULL, NULL, 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (6, 'system.password', NULL, 'system', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (7, 'xinco/FileRepositoryPath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (8, 'xinco/FileIndexPath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\index\\\\', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (9, 'xinco/FileArchivePath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\archive\\\\', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (10, 'xinco/FileArchivePeriod', NULL, NULL, 0, 14400000);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (11, 'xinco/FileIndexer_1_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (12, 'xinco/FileIndexer_1_Ext', NULL, 'pdf', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (13, 'xinco/FileIndexer_2_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftWord', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (14, 'xinco/FileIndexer_2_Ext', NULL, 'doc', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (15, 'xinco/FileIndexer_3_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftExcel', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (16, 'xinco/FileIndexer_3_Ext', NULL, 'xls', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (17, 'xinco/FileIndexer_4_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftPowerpoint', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (18, 'xinco/FileIndexer_4_Ext', NULL, 'ppt', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (19, 'xinco/FileIndexer_5_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexHTML', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (20, 'xinco/FileIndexer_5_Ext', NULL, 'asp;htm;html;jsf;jsp;php;php3;php4', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (21, 'xinco/IndexNoIndex', NULL, ';aac;ac3;ace;ade;adp;aif;aifc;aiff;amf;arc;arj;asx;au;avi;b64;bh;bmp;bz;bz2;cab;cda;chm;class;com;div;divx;ear;exe;far;fla;gif;gz;hlp;ico;;iso;jar;jpe;jpeg;jpg;lha;lzh;mda;mdb;mde;mdn;mdt;mdw;mid;midi;mim;mod;mov;mp1;mp2;mp2v;mp3;mp4;mpa;mpe;mpeg;mpg;mpg4;mpv2;msi;ntx;ocx;ogg;ogm;okt;pae;pcx;pk3;png;pot;ppa;pps;ppt;pwz;qwk;ra;ram;rar;raw;rep;rm;rmi;snd;swf;swt;tar;taz;tbz;tbz2;tgz;tif;tiff;uu;uue;vxd;war;wav;wbm;wbmp;wma;wmd;wmf;wmv;xpi;xxe;z;zip;zoo;;;', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (22, 'xinco/MaxSearchResult', 100, NULL, 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (23, 'setting.email.host', NULL, 'smtp.bluecubs.com', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (24, 'setting.email.user', NULL, 'info@bluecubs.com', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (25, 'setting.email.password', NULL, 'password', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (26, 'setting.email.port', 25, NULL, 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (27, 'setting.allowoutsidelinks', NULL, NULL, 1, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (28, 'setting.backup.path', NULL, 'C:\\\\Temp\\\\xinco\\\\backup\\\\', 0, 0);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (29, 'xinco/FileIndexOptimizerPeriod', NULL, NULL, 0, 14400000);
insert into `xinco`.`xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) values (30, 'setting.allowpublisherlist', NULL, NULL, 1, 0);

COMMIT;

-- -----------------------------------------------------
-- Data for table `xinco_workflow`.`xinco_workflow_id`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
insert into `xinco_workflow`.`xinco_workflow_id` (`id`, `tablename`, `last_id`) values (1, 'xinco_action', 1000);

COMMIT;
