SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER SCHEMA `xinco`  DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci ;

USE `xinco`;

ALTER TABLE `xinco`.`xinco_core_ace` DROP FOREIGN KEY `fk_xinco_core_ace_xinco_core_user1` ;

ALTER TABLE `xinco`.`xinco_core_log` DROP FOREIGN KEY `fk_xinco_core_log_xinco_core_user1` ;

ALTER TABLE `xinco`.`xinco_add_attribute` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_add_attribute_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_ace` CHARACTER SET = utf8 , COLLATE = utf8_general_ci , CHANGE COLUMN `xinco_core_user_id` `xinco_core_user_id` INT(10) UNSIGNED NULL DEFAULT NULL  AFTER `id` , 
  ADD CONSTRAINT `fk_xinco_core_ace_xinco_core_user2`
  FOREIGN KEY (`xinco_core_user_id` )
  REFERENCES `xinco`.`xinco_core_user` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_xinco_core_ace_xinco_core_user2` (`xinco_core_user_id` ASC) 
, DROP INDEX `fk_xinco_core_ace_xinco_core_user1` ;

ALTER TABLE `xinco`.`xinco_core_ace_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_data` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_has_dependency` (
  `xinco_core_data_parent_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_data_children_id` INT(10) UNSIGNED NOT NULL ,
  `dependency_type_id` INT(11) NOT NULL ,
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
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_core_data_has_dependency_t` (
  `record_id` INT(10) NOT NULL ,
  `xinco_core_data_parent_id` INT(10) UNSIGNED NOT NULL ,
  `xinco_core_data_children_id` INT(10) UNSIGNED NOT NULL ,
  `dependency_type_id` INT(11) NOT NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

ALTER TABLE `xinco`.`xinco_core_data_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_data_type` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_data_type_attribute` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_data_type_attribute_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_data_type_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_group` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_group_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_language` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_language_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_log` CHARACTER SET = utf8 , COLLATE = utf8_general_ci , 
  ADD CONSTRAINT `fk_xinco_core_log_xinco_core_user2`
  FOREIGN KEY (`xinco_core_user_id` )
  REFERENCES `xinco`.`xinco_core_user` (`id` )
  ON DELETE NO ACTION
  ON UPDATE NO ACTION
, ADD INDEX `fk_xinco_core_log_xinco_core_user2` (`xinco_core_user_id` ASC) 
, DROP INDEX `fk_xinco_core_log_xinco_core_user1` ;

ALTER TABLE `xinco`.`xinco_core_node` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_node_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_user` CHARACTER SET = utf8 , COLLATE = utf8_general_ci , CHANGE COLUMN `firstname` `fisrt_name` VARCHAR(255) NOT NULL, CHANGE COLUMN `name` `last_name` VARCHAR(255) NOT NULL;

ALTER TABLE `xinco`.`xinco_core_user_has_xinco_core_group` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_user_has_xinco_core_group_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci ;

ALTER TABLE `xinco`.`xinco_core_user_modified_record` CHARACTER SET = utf8 , COLLATE = utf8_general_ci , CHANGE COLUMN `mod_Reason` `mod_Reason` VARCHAR(255) NOT NULL  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`record_id`, `id`) ;

ALTER TABLE `xinco`.`xinco_core_user_t` CHARACTER SET = utf8 , COLLATE = utf8_general_ci , CHANGE COLUMN `firstname` `fisrt_name` VARCHAR(255) NOT NULL, CHANGE COLUMN `name` `last_name` VARCHAR(255) NOT NULL;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_behavior` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `Unique` (`designation` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_behavior_t` (
  `record_id` INT(10) NOT NULL ,
  `id` INT(11) NOT NULL ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `xinco_dependency_behavior_id` INT(11) NOT NULL ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `designation_UNIQUE` (`designation` ASC) ,
  INDEX `fk_xinco_dependency_type_xinco_dependency_behavior1` (`xinco_dependency_behavior_id` ASC) ,
  CONSTRAINT `fk_xinco_dependency_type_xinco_dependency_behavior1`
    FOREIGN KEY (`xinco_dependency_behavior_id` )
    REFERENCES `xinco`.`xinco_dependency_behavior` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_dependency_type_t` (
  `record_id` INT(10) NOT NULL ,
  `id` INT(11) NOT NULL ,
  `xinco_dependency_behavior_id` INT(11) NOT NULL ,
  `designation` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

ALTER TABLE `xinco`.`xinco_id` COLLATE = utf8_general_ci ;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_setting` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `description` VARCHAR(45) NOT NULL ,
  `int_value` INT(10) UNSIGNED NULL DEFAULT NULL ,
  `string_value` TEXT NULL DEFAULT NULL ,
  `bool_value` TINYINT(1) NULL DEFAULT NULL ,
  `long_value` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique` (`description` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 37
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `xinco`.`xinco_setting_t` (
  `record_id` INT(10) NOT NULL ,
  `id` INT(11) NOT NULL ,
  `description` VARCHAR(45) NOT NULL ,
  `int_value` INT(10) UNSIGNED NULL DEFAULT NULL ,
  `string_value` TEXT NULL DEFAULT NULL ,
  `bool_value` TINYINT(1) NULL DEFAULT NULL ,
  `long_value` BIGINT(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`record_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
