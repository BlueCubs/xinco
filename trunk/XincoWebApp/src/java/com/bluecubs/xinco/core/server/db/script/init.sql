-- -----------------------------------------------------
-- Data for table `xinco_core_language`
-- -----------------------------------------------------


INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('1', 'n/a', 'unknown');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('2', 'en', 'English');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('3', 'de', 'German');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('4', 'fr', 'French');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('5', 'it', 'Italian');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('6', 'es', 'Spanish');
INSERT INTO `xinco_core_language` (`id`, `sign`, `designation`) VALUES ('7', 'ru', 'Russian');



-- -----------------------------------------------------
-- Data for table `xinco_core_node`
-- -----------------------------------------------------


INSERT INTO `xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('1', NULL, '1', 'xincoRoot', '1');
INSERT INTO `xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('2', '1', '1', 'Trash', '1');
INSERT INTO `xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('3', '1', '1', 'Temp', '1');
INSERT INTO `xinco_core_node` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `designation`, `status_number`) VALUES ('4', '1', '1', 'News', '1');



-- -----------------------------------------------------
-- Data for table `xinco_core_data_type`
-- -----------------------------------------------------


INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('1', 'general.data.type.file', 'general.data.type.file.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('2', 'general.data.type.text', 'general.data.type.text.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('3', 'general.data.type.URL', 'general.data.type.URL.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('4', 'general.data.type.contact', 'general.data.type.contact.description');
INSERT INTO `xinco_core_data_type` (`id`, `designation`, `description`) VALUES ('5', 'general.data.type.rendering', 'general.data.type.rendering.description');



-- -----------------------------------------------------
-- Data for table `xinco_core_data`
-- -----------------------------------------------------


INSERT INTO `xinco_core_data` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) VALUES ('1', '1', '2', '3', 'Apache License 2.0', '5');
INSERT INTO `xinco_core_data` (`id`, `xinco_core_node_id`, `xinco_core_language_id`, `xinco_core_data_type_id`, `designation`, `status_number`) VALUES ('2', '1', '2', '3', 'xinco.org', '1');



-- -----------------------------------------------------
-- Data for table `xinco_core_group`
-- -----------------------------------------------------


INSERT INTO `xinco_core_group` (`id`, `designation`, `status_number`) VALUES ('1', 'general.group.admin', '1');
INSERT INTO `xinco_core_group` (`id`, `designation`, `status_number`) VALUES ('2', 'general.group.allusers', '1');
INSERT INTO `xinco_core_group` (`id`, `designation`, `status_number`) VALUES ('3', 'general.group.public', '1');



-- -----------------------------------------------------
-- Data for table `xinco_core_ace`
-- -----------------------------------------------------


INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('1', '1', NULL, '1', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('2', '1', NULL, '2', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('3', '1', NULL, '3', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('4', NULL, '1', '1', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('5', NULL, '1', '2', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('6', NULL, '1', '3', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('7', NULL, '2', '1', NULL, 1, 1, 1, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('8', NULL, '2', '2', NULL, 1, 1, 1, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('9', NULL, '2', '3', NULL, 1, 1, 1, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('10', '1', NULL, NULL, '1', 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('11', '1', NULL, NULL, '2', 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('12', NULL, '1', NULL, '1', 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('13', NULL, '1', NULL, '2', 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('14', NULL, '2', NULL, '1', 1, 0, 0, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('15', NULL, '2', NULL, '2', 1, 0, 0, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('16', '1', NULL, '4', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('17', NULL, '1', '4', NULL, 1, 1, 1, 1);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('18', NULL, '2', '4', NULL, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('19', NULL, '3', '1', NULL, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('20', NULL, '3', '4', NULL, 1, 0, 0, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('21', NULL, '3', NULL, '1', 1, 0, 0, 0);
INSERT INTO `xinco_core_ace` (`id`, `xinco_core_user_id`, `xinco_core_group_id`, `xinco_core_node_id`, `xinco_core_data_id`, `read_permission`, `write_permission`, `execute_permission`, `admin_permission`) VALUES ('22', NULL, '3', NULL, '2', 1, 0, 0, 0);



-- -----------------------------------------------------
-- Data for table `xinco_core_user`
-- -----------------------------------------------------


INSERT INTO `xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Administrator', 'Xinco', 'admin@xinco.org', '1', '0', now());
INSERT INTO `xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES ('2', 'user', 'ee11cbb19052e40b07aac0ca060c23ee', 'User', 'Default', 'user@xinco.org', '1', '0', now());
INSERT INTO `xinco_core_user` (`id`, `username`, `userpassword`, `name`, `firstname`, `email`, `status_number`, `attempts`, `last_modified`) VALUES ('3', 'bluecubs', 'a1681d95b94018a6721432c774bcef13', 'System', 'User', 'info@bluecubs.com', '1', '0', now());



-- -----------------------------------------------------
-- Data for table `xinco_core_user_has_xinco_core_group`
-- -----------------------------------------------------


INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('1', '1', '1');
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('1', '2', '1');
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('2', '2', '1');
INSERT INTO `xinco_core_user_has_xinco_core_group` (`xinco_core_user_id`, `xinco_core_group_id`, `status_number`) VALUES ('3', '2', '1');



-- -----------------------------------------------------
-- Data for table `xinco_add_attribute`
-- -----------------------------------------------------


INSERT INTO `xinco_add_attribute` (`xinco_core_data_id`, `attribute_id`, `attrib_int`, `attrib_unsignedint`, `attrib_double`, `attrib_varchar`, `attrib_text`, `attrib_datetime`) VALUES ('2', '1', '0', '0', '0', 'http://www.xinco.org', '', now());
INSERT INTO `xinco_add_attribute` (`xinco_core_data_id`, `attribute_id`, `attrib_int`, `attrib_unsignedint`, `attrib_double`, `attrib_varchar`, `attrib_text`, `attrib_datetime`) VALUES ('1', '1', '0', '0', '0', 'http://www.apache.org/licenses/LICENSE-2.0.html', '', now());



-- -----------------------------------------------------
-- Data for table `xinco_core_data_type_attribute`
-- -----------------------------------------------------


INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '1', 'general.filename', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '2', 'general.size', 'unsignedint', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '3', 'general.checksum', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '4', 'general.revision.model', 'unsignedint', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '5', 'general.archive.model', 'unsignedint', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '6', 'general.archive.date', 'datetime', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '7', 'general.archive.days', 'unsignedint', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '8', 'general.archive.location', 'text', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '9', 'general.description', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '10', 'Keyword_1', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '11', 'Keyword_2', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('1', '12', 'Keyword_3', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('2', '1', 'general.data.type.text', 'text', '0');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('3', '1', 'general.data.type.URL', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('4', '1', 'general.salutation', 'varchar', '255');
INSERT INTO `xinco_core_data_type_attribute` (`xinco_core_data_type_id`, `attribute_id`, `designation`, `data_type`, `attr_size`) VALUES ('5', '1', 'general.format', 'varchar', '45');



-- -----------------------------------------------------
-- Data for table `xinco_core_log`
-- -----------------------------------------------------


INSERT INTO `xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) VALUES ('1', '1', '1', '1', now(), 'Creation!', '1', '0', '0', '');
INSERT INTO `xinco_core_log` (`id`, `xinco_core_data_id`, `xinco_core_user_id`, `op_code`, `op_datetime`, `op_description`, `version_high`, `version_mid`, `version_low`, `version_postfix`) VALUES ('2', '2', '1', '1', now(), 'Creation!', '1', '0', '0', '');



-- -----------------------------------------------------
-- Data for table `xinco_setting`
-- -----------------------------------------------------


INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('1', 'password.aging', '120', NULL, 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('2', 'password.attempts', '3', NULL, 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('3', 'password.unusable_period', '365', NULL, 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('4', 'general.copyright.date', '0', '2004-2009', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('5', 'setting.enable.savepassword', NULL, NULL, 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('6', 'system.password', NULL, 'system', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('7', 'xinco/FileRepositoryPath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('8', 'xinco/FileIndexPath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\index\\\\', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('9', 'xinco/FileArchivePath', NULL, 'C:\\\\Temp\\\\xinco\\\\file_repository\\\\archive\\\\', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('10', 'xinco/FileArchivePeriod', NULL, NULL, 0, '14400000');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('11', 'xinco/FileIndexer_1_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('12', 'xinco/FileIndexer_1_Ext', NULL, 'pdf', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('13', 'xinco/FileIndexer_2_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftWord', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('14', 'xinco/FileIndexer_2_Ext', NULL, 'doc', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('15', 'xinco/FileIndexer_3_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftExcel', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('16', 'xinco/FileIndexer_3_Ext', NULL, 'xls', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('17', 'xinco/FileIndexer_4_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftPowerpoint', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('18', 'xinco/FileIndexer_4_Ext', NULL, 'ppt', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('19', 'xinco/FileIndexer_5_Class', NULL, 'com.bluecubs.xinco.index.filetypes.XincoIndexHTML', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('20', 'xinco/FileIndexer_5_Ext', NULL, 'asp;htm;html;jsf;jsp;php;php3;php4', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('21', 'xinco/IndexNoIndex', NULL, ';aac;ac3;ace;ade;adp;aif;aifc;aiff;amf;arc;arj;asx;au;avi;b64;bh;bmp;bz;bz2;cab;cda;chm;class;com;div;divx;ear;exe;far;fla;gif;gz;hlp;ico;;iso;jar;jpe;jpeg;jpg;lha;lzh;mda;mdb;mde;mdn;mdt;mdw;mid;midi;mim;mod;mov;mp1;mp2;mp2v;mp3;mp4;mpa;mpe;mpeg;mpg;mpg4;mpv2;msi;ntx;ocx;ogg;ogm;okt;pae;pcx;pk3;png;pot;ppa;pps;ppt;pwz;qwk;ra;ram;rar;raw;rep;rm;rmi;snd;swf;swt;tar;taz;tbz;tbz2;tgz;tif;tiff;uu;uue;vxd;war;wav;wbm;wbmp;wma;wmd;wmf;wmv;xpi;xxe;z;zip;zoo;;;', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('22', 'xinco/MaxSearchResult', '100', NULL, 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('23', 'setting.email.host', NULL, 'smtp.bluecubs.com', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('24', 'setting.email.user', NULL, 'info@bluecubs.com', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('25', 'setting.email.password', NULL, 'password', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('26', 'setting.email.port', '25', NULL, 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('27', 'setting.allowoutsidelinks', NULL, NULL, 1, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('28', 'setting.backup.path', NULL, 'C:\\\\Temp\\\\xinco\\\\backup\\\\', 0, '0');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('29', 'xinco/FileIndexOptimizerPeriod', NULL, NULL, 0, '14400000');
INSERT INTO `xinco_setting` (`id`, `description`, `int_value`, `string_value`, `bool_value`, `long_value`) VALUES ('30', 'setting.allowpublisherlist', NULL, NULL, 1, '0');



-- -----------------------------------------------------
-- Data for table `xinco_dependency_behavior`
-- -----------------------------------------------------


INSERT INTO `xinco_dependency_behavior` (`id`, `designation`, `description`) VALUES ('1', 'dependency.behavior.one-way', 'dependency.behavior.one-way.desc');
INSERT INTO `xinco_dependency_behavior` (`id`, `designation`, `description`) VALUES ('2', 'dependency.behavior.two-way', 'dependency.behavior.two-way.desc');



-- -----------------------------------------------------
-- Data for table `xinco_dependency_type`
-- -----------------------------------------------------


INSERT INTO `xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('1', NULL, 'dependency.related', 'dependency.related.desc');
INSERT INTO `xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('2', NULL, 'dependency.component', 'dependency.component.desc');
INSERT INTO `xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('3', NULL, 'dependency.group', 'dependency.group.desc');
INSERT INTO `xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('4', NULL, 'dependency.package', 'dependency.package.desc');
INSERT INTO `xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('5', NULL, 'dependency.rendering', 'dependency.rendering.desc');
INSERT INTO `xinco_dependency_type` (`id`, `xinco_dependency_behavior_id`, `designation`, `description`) VALUES ('6', NULL, 'dependency.supporting', 'dependency.supporting.desc');


