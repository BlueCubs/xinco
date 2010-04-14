SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `xinco` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
CREATE SCHEMA IF NOT EXISTS `xinco_workflow` DEFAULT CHARACTER SET utf8 ;

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
  `xinco_state_xinco_workflow_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_version` INT(11) NOT NULL ,
  `destination_state_id` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_id1` INT(11) NOT NULL ,
  `xinco_state_xinco_workflow_version1` INT(11) NOT NULL ,
  PRIMARY KEY (`source_state_id`, `xinco_state_xinco_workflow_id`, `xinco_state_xinco_workflow_version`, `destination_state_id`, `xinco_state_xinco_workflow_id1`, `xinco_state_xinco_workflow_version1`) ,
  INDEX `fk_xinco_state_has_xinco_state_xinco_state1` (`source_state_id` ASC, `xinco_state_xinco_workflow_id` ASC, `xinco_state_xinco_workflow_version` ASC) ,
  INDEX `fk_xinco_state_has_xinco_state_xinco_state2` (`destination_state_id` ASC, `xinco_state_xinco_workflow_id1` ASC, `xinco_state_xinco_workflow_version1` ASC) ,
  CONSTRAINT `fk_xinco_state_has_xinco_state_xinco_state1`
    FOREIGN KEY (`source_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` )
    REFERENCES `xinco_workflow`.`xinco_workflow_state` (`id` , `xinco_workflow_id` , `xinco_workflow_version` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_xinco_state_has_xinco_state_xinco_state2`
    FOREIGN KEY (`destination_state_id` , `xinco_state_xinco_workflow_id1` , `xinco_state_xinco_workflow_version1` )
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
    REFERENCES `xinco_workflow`.`xinco_state_transitions_to_xinco_state` (`source_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` , `destination_state_id` , `xinco_state_xinco_workflow_id1` , `xinco_state_xinco_workflow_version1` )
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
    REFERENCES `xinco_workflow`.`xinco_state_transitions_to_xinco_state` (`source_state_id` , `xinco_state_xinco_workflow_id` , `xinco_state_xinco_workflow_version` , `destination_state_id` , `xinco_state_xinco_workflow_id1` , `xinco_state_xinco_workflow_version1` )
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
  `value` BLOB NOT NULL ,
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
-- Data for table `xinco_workflow`.`xinco_workflow_id`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `xinco_workflow`;
insert into `xinco_workflow`.`xinco_workflow_id` (`id`, `tablename`, `last_id`) values (1, 'xinco_action', 1000);

COMMIT;
