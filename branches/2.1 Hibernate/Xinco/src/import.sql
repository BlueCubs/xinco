-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_node`
-- -----------------------------------------------------
INSERT INTO `xinco_core_node`(`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES (1, NULL, 1, 'xincoRoot', 1);
INSERT INTO `xinco_core_node`(`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES (2, 1, 1, 'Trash', 1);
INSERT INTO `xinco_core_node`(`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES (3, 1, 1, 'Temp', 1);
INSERT INTO `xinco_core_node`(`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES (4, 1, 1, 'News', 1);

-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_data`
-- -----------------------------------------------------
INSERT INTO `xinco_core_data`(`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) VALUES(1, 1, 2, 2, 'Apache License 2.0', 5);
INSERT INTO `xinco_core_data`(`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) VALUES (2, 1, 2, 3, 'xinco.org', 1);
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_language`
-- -----------------------------------------------------
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(1, 'n/a', 'unknown');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(2, 'en', 'English');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(3, 'de', 'German');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(4, 'fr', 'French');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(5, 'it', 'Italian');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(6, 'es', 'Spanish');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES(7, 'ru', 'Russian');
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_ace`
-- -----------------------------------------------------
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(1, 1, -1, 1, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(2, 1, -1, 2, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(3, 1, -1, 3, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(4, -1, 1, 1, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(5, -1, 1, 2, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(6, -1, 1, 3, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(7, -1, 2, 1, -1, 1, 1, 1, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(8, -1, 2, 2, -1, 1, 1, 1, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(9, -1, 2, 3, -1, 1, 1, 1, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(10, 1, -1, -1, 1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(11, 1, -1, -1, 2, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(12, -1, 1, -1, 1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(13, -1, 1, -1, 2, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(14, -1, 2, -1, 1, 1, 0, 0, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(15, -1, 2, -1, 2, 1, 0, 0, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(16, 1, -1, 4, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(17, -1, 1, 4, -1, 1, 1, 1, 1);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(18, -1, 2, 4, -1, 1, 0, 0, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(19, -1, 3, 1, -1, 1, 0, 0, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(20, -1, 3, 4, -1, 1, 0, 0, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(21, -1, 3, -1, 1, 1, 0, 0, 0);
 INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES(22, -1, 3, -1, 2, 1, 0, 0, 0);
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_user`
-- -----------------------------------------------------
INSERT INTO `xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES(1, 'admin', MD5('admin'), 'Administrator', 'Xinco', 'admin@xinco.org', 1, 0, now());
INSERT INTO `xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES(2, 'user', MD5('user'), 'User', 'Default', 'user@xinco.org', 1, 0, now());
INSERT INTO `xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES(3, 'bluecubs', MD5('bluecubs'), 'System', 'User', 'info@bluecubs.com', 1, 0, now());
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES(1, 1, 1);
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES(1, 2, 1);
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES(2, 2, 1);
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES(3, 2, 1);
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_id`
-- -----------------------------------------------------
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
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_data_type`
-- -----------------------------------------------------
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES(1, 'general.data.type.file', 'general.data.type.file.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES(2, 'general.data.type.text', 'general.data.type.text.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES(3, 'general.data.type.URL', 'general.data.type.URL.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES(4, 'general.data.type.contact', 'general.data.type.contact.description');
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_data_type_attribute`
-- -----------------------------------------------------
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 1, 'File_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 2, 'Size', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 3, 'Checksum', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 4, 'Revision_Model', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 5, 'Archiving_Model', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 6, 'Archiving_Date', 'datetime', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 7, 'Archiving_Days', 'unsignedint', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 8, 'Archiving_Location', 'text', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 9, 'Description', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 10, 'Keyword_1', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 11, 'Keyword_2', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(1, 12, 'Keyword_3', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(2, 1, 'Text', 'text', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(3, 1, 'URL', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 1, 'Salutation', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 2, 'First_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 3, 'Middle_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 4, 'Last_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 5, 'Name_Affix', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 6, 'Phone_business', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 7, 'Phone_private', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 8, 'Phone_mobile', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 9, 'Fax', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 10, 'Email', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 11, 'Website', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 12, 'Street_Address', 'text', 0);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 13, 'Postal_Code', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 14, 'City', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 15, 'State_Province', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 16, 'Country', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 17, 'Company_Name', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 18, 'Position', 'varchar', 255);
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `size`) VALUES(4, 19, 'Notes', 'text', 0);
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_core_log`
-- -----------------------------------------------------
INSERT INTO `xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) VALUES(1, 1, 1, 1, now(), 'audit.general.create', 1, 0, 0, '');
INSERT INTO `xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) VALUES(2, 2, 1, 1, now(), 'audit.general.create', 1, 0, 0, '');
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_setting`
-- -----------------------------------------------------
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(1,'version.high', 2,null,null,0 );
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(2,'version.med', 1,null ,null,0 );
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(3,'version.low', 0,null,null,0 );
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(4,'version.postfix', 0,null ,null,0 );
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(5,'password.aging', 120,null,null,0 );
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(6,'password.attempts', 3,null,null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(7,'password.unusable_period', 365,null,null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(8,'general.copyright.date', 0,'2004-2008' ,null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(9,'setting.enable.savepassword', 0,null ,null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(10,'setting.enable.developermode', 0,null ,0,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(11,'setting.enable.lockidle', 5,null ,1,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(12,'system.password', 0,'system',null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(13,'xinco/FileRepositoryPath' , 0 ,'C:\\Temp\\xinco\\file_repository\\' , null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(14,'xinco/FileIndexPath' , 0 ,'C:\\Temp\\xinco\\file_repository\\index\\' , null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(15,'xinco/FileArchivePath' , 0 ,'C:\\Temp\\xinco\\file_repository\\archive\\' , null,0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(16,'xinco/FileArchivePeriod' ,0 , 0, null,14400000);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(17,'xinco/FileIndexer_1_Class' , 0 ,'com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(18,'xinco/FileIndexer_1_Ext' , 0 ,'pdf', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(19,'xinco/FileIndexer_2_Class' , 0 ,'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftWord', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(20,'xinco/FileIndexer_2_Ext' , 0 ,'doc', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(21,'xinco/FileIndexer_3_Class' , 0 ,'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftExcel', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(22,'xinco/FileIndexer_3_Ext' , 0 ,'xls', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(23,'xinco/FileIndexer_4_Class' , 0 ,'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftPowerpoint', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(24,'xinco/FileIndexer_4_Ext' , 0 ,'ppt', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(25,'xinco/FileIndexer_5_Class' , 0 ,'com.bluecubs.xinco.index.filetypes.XincoIndexHTML', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(26,'xinco/FileIndexer_5_Ext' , 0 ,'asp;htm;html;jsf;jsp;php;php3;php4', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(27,'xinco/IndexNoIndex' , 0 ,';aac;ac3;ace;ade;adp;aif;aifc;aiff;amf;arc;arj;asx;au;avi;b64;bh;bmp;bz;bz2;cab;cda;chm;class;com;div;divx;ear;exe;far;fla;gif;gz;hlp;ico;;iso;jar;jpe;jpeg;jpg;lha;lzh;mda;mdb;mde;mdn;mdt;mdw;mid;midi;mim;mod;mov;mp1;mp2;mp2v;mp3;mp4;mpa;mpe;mpeg;mpg;mpg4;mpv2;msi;ntx;ocx;ogg;ogm;okt;pae;pcx;pk3;png;pot;ppa;pps;ppt;pwz;qwk;ra;ram;rar;raw;rep;rm;rmi;snd;swf;swt;tar;taz;tbz;tbz2;tgz;tif;tiff;uu;uue;vxd;war;wav;wbm;wbmp;wma;wmd;wmf;wmv;xpi;xxe;z;zip;zoo;;;', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(28,'xinco/JNDIDB' , 0 ,'java:comp/env/jdbc/XincoDB', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(29,'xinco/MaxSearchResult' ,100 , 0, null, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(30,'setting.email.host', 0,'smtp.gmail.com', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(31,'setting.email.user', 0,'info@bluecubs.com', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(32,'setting.email.password', 0,'password', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(33,'setting.email.port', 0,'25', 0, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(34,'setting.index.lock', 0, 0,false, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(35,'setting.allowoutsidelinks', 0, 0,true, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(36,'setting.securitycheck.enable', 0, 0,true, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(37,'setting.printDBTransactions.enable', 0, 0,false, 0);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(38,'xinco/FileIndexOptimizerPeriod', 0, 0,null,14400000);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(39,'xinco/FileArchivePeriod', 0, 0,null,14400000);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(40,'setting.allowpublisherlist', 0, 0,true, 0);
-- -----------------------------------------------------
-- Data for table `mydb`.`xinco_add_attribute`
-- -----------------------------------------------------
INSERT INTO xinco_add_attribute (xinco_core_data_id, attribute_id, attrib_text) VALUES (1, 1, '
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

   1. Definitions.

      "License" shall mean the terms and conditions for use, reproduction,
      and distribution as defined by Sections 1 through 9 of this document.

      "Licensor" shall mean the copyright owner or entity authorized by
      the copyright owner that is granting the License.

      "Legal Entity" shall mean the union of the acting entity and all
      other entities that control, are controlled by, or are under common
      control with that entity. For the purposes of this definition,
      "control" means (i) the power, direct or indirect, to cause the
      direction or management of such entity, whether by contract or
      otherwise, or (ii) ownership of fifty percent (50%) or more of the
      outstanding shares, or (iii) beneficial ownership of such entity.

      "You" (or "Your") shall mean an individual or Legal Entity
      exercising permissions granted by this License.

      "Source" form shall mean the preferred form for making modifications,
      including but not limited to software source code, documentation
      source, and configuration files.

      "Object" form shall mean any form resulting from mechanical
      transformation or translation of a Source form, including but
      not limited to compiled object code, generated documentation,
      and conversions to other media types.

      "Work" shall mean the work of authorship, whether in Source or
      Object form, made available under the License, as indicated by a
      copyright notice that is included in or attached to the work
      (an example is provided in the Appendix below).

      "Derivative Works" shall mean any work, whether in Source or Object
      form, that is based on (or derived from) the Work and for which the
      editorial revisions, annotations, elaborations, or other modifications
      represent, as a whole, an original work of authorship. For the purposes
      of this License, Derivative Works shall not include works that remain
      separable from, or merely link (or bind by name) to the interfaces of,
      the Work and Derivative Works thereof.

      "Contribution" shall mean any work of authorship, including
      the original version of the Work and any modifications or additions
      to that Work or Derivative Works thereof, that is intentionally
      submitted to Licensor for inclusion in the Work by the copyright owner
      or by an individual or Legal Entity authorized to submit on behalf of
      the copyright owner. For the purposes of this definition, "submitted"
      means any form of electronic, verbal, or written communication sent
      to the Licensor or its representatives, including but not limited to
      communication on electronic mailing lists, source code control systems,
      and issue tracking systems that are managed by, or on behalf of, the
      Licensor for the purpose of discussing and improving the Work, but
      excluding communication that is conspicuously marked or otherwise
      designated in writing by the copyright owner as "Not a Contribution."

      "Contributor" shall mean Licensor and any individual or Legal Entity
      on behalf of whom a Contribution has been received by Licensor and
      subsequently incorporated within the Work.

   2. Grant of Copyright License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      copyright license to reproduce, prepare Derivative Works of,
      publicly display, publicly perform, sublicense, and distribute the
      Work and such Derivative Works in Source or Object form.

   3. Grant of Patent License. Subject to the terms and conditions of
      this License, each Contributor hereby grants to You a perpetual,
      worldwide, non-exclusive, no-charge, royalty-free, irrevocable
      (except as stated in this section) patent license to make, have made,
      use, offer to sell, sell, import, and otherwise transfer the Work,
      where such license applies only to those patent claims licensable
      by such Contributor that are necessarily infringed by their
      Contribution(s) alone or by combination of their Contribution(s)
      with the Work to which such Contribution(s) was submitted. If You
      institute patent litigation against any entity (including a
      cross-claim or counterclaim in a lawsuit) alleging that the Work
      or a Contribution incorporated within the Work constitutes direct
      or contributory patent infringement, then any patent licenses
      granted to You under this License for that Work shall terminate
      as of the date such litigation is filed.

   4. Redistribution. You may reproduce and distribute copies of the
      Work or Derivative Works thereof in any medium, with or without
      modifications, and in Source or Object form, provided that You
      meet the following conditions:

      (a) You must give any other recipients of the Work or
          Derivative Works a copy of this License; and

      (b) You must cause any modified files to carry prominent notices
          stating that You changed the files; and

      (c) You must retain, in the Source form of any Derivative Works
          that You distribute, all copyright, patent, trademark, and
          attribution notices from the Source form of the Work,
          excluding those notices that do not pertain to any part of
          the Derivative Works; and

      (d) If the Work includes a "NOTICE" text file as part of its
          distribution, then any Derivative Works that You distribute must
          include a readable copy of the attribution notices contained
          within such NOTICE file, excluding those notices that do not
          pertain to any part of the Derivative Works, in at least one
          of the following places: within a NOTICE text file distributed
          as part of the Derivative Works; within the Source form or
          documentation, if provided along with the Derivative Works; or,
          within a display generated by the Derivative Works, if and
          wherever such third-party notices normally appear. The contents
          of the NOTICE file are for informational purposes only and
          do not modify the License. You may add Your own attribution
          notices within Derivative Works that You distribute, alongside
          or as an addendum to the NOTICE text from the Work, provided
          that such additional attribution notices cannot be construed
          as modifying the License.

      You may add Your own copyright statement to Your modifications and
      may provide additional or different license terms and conditions
      for use, reproduction, or distribution of Your modifications, or
      for any such Derivative Works as a whole, provided Your use,
      reproduction, and distribution of the Work otherwise complies with
      the conditions stated in this License.

   5. Submission of Contributions. Unless You explicitly state otherwise,
      any Contribution intentionally submitted for inclusion in the Work
      by You to the Licensor shall be under the terms and conditions of
      this License, without any additional terms or conditions.
      Notwithstanding the above, nothing herein shall supersede or modify
      the terms of any separate license agreement you may have executed
      with Licensor regarding such Contributions.

   6. Trademarks. This License does not grant permission to use the trade
      names, trademarks, service marks, or product names of the Licensor,
      except as required for reasonable and customary use in describing the
      origin of the Work and reproducing the content of the NOTICE file.

   7. Disclaimer of Warranty. Unless required by applicable law or
      agreed to in writing, Licensor provides the Work (and each
      Contributor provides its Contributions) on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
      implied, including, without limitation, any warranties or conditions
      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
      PARTICULAR PURPOSE. You are solely responsible for determining the
      appropriateness of using or redistributing the Work and assume any
      risks associated with Your exercise of permissions under this License.

   8. Limitation of Liability. In no event and under no legal theory,
      whether in tort (including negligence), contract, or otherwise,
      unless required by applicable law (such as deliberate and grossly
      negligent acts) or agreed to in writing, shall any Contributor be
      liable to You for damages, including any direct, indirect, special,
      incidental, or consequential damages of any character arising as a
      result of this License or out of the use or inability to use the
      Work (including but not limited to damages for loss of goodwill,
      work stoppage, computer failure or malfunction, or any and all
      other commercial damages or losses), even if such Contributor
      has been advised of the possibility of such damages.

   9. Accepting Warranty or Additional Liability. While redistributing
      the Work or Derivative Works thereof, You may choose to offer,
      and charge a fee for, acceptance of support, warranty, indemnity,
      or other liability obligations and/or rights consistent with this
      License. However, in accepting such obligations, You may act only
      on Your own behalf and on Your sole responsibility, not on behalf
      of any other Contributor, and only if You agree to indemnify,
      defend, and hold each Contributor harmless for any liability
      incurred by, or claims asserted against, such Contributor by reason
      of your accepting any such warranty or additional liability.

   END OF TERMS AND CONDITIONS

   APPENDIX: How to apply the Apache License to your work.

      To apply the Apache License to your work, attach the following
      boilerplate notice, with the fields enclosed by brackets "[]"
      replaced with your own identifying information. (Do not include
      the brackets!)  The text should be enclosed in the appropriate
      comment syntax for the file format. We also recommend that a
      file or class name and description of purpose be included on the
      same "printed page" as the copyright notice for easier
      identification within third-party archives.

   Copyright [yyyy] [name of copyright owner]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
');
INSERT INTO xinco_add_attribute (xinco_core_data_id, attribute_id, attrib_varchar) VALUES (2, 1, 'http://www.xinco.org');
