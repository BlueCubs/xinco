CREATE TABLE xinco_setting (
  id INTEGER(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  description VARCHAR(45) NOT NULL,
  int_value INTEGER(10) UNSIGNED NULL DEFAULT null,
  string_value VARCHAR(500) NULL DEFAULT null,
  bool_value BOOL NULL DEFAULT null,
  long_value BIGINT NULL DEFAULT null,
  PRIMARY KEY(id)
)
TYPE=InnoDB;

INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(1,'version.high', 2,null,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(2,'version.med', 0,null ,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(3,'version.low', 3,null,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(4,'version.postfix', null,null ,null ); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(5,'password.aging', 120,null,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(6,'password.attempts', 3,null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(7,'password.unusable_period', 365,null,null); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(8,'general.copyright.date', null,'2004-2007' ,null); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(9,'setting.enable.savepassword', null,null ,0); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(10,'setting.enable.developermode', null,null ,0); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(11,'setting.enable.lockidle', 5,null ,1); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(12,'system.password', null,'system',null); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(13,'xinco/FileRepositoryPath' ,null ,'C:\\Temp\\xinco\\file_repository\\' , null); 
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(14,'xinco/FileIndexPath' ,null ,'C:\\Temp\\xinco\\file_repository\\index\\' , null);  
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(15,'xinco/FileArchivePath' ,null ,'C:\\Temp\\xinco\\file_repository\\archive\\' , null);  
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(16,'xinco/FileArchivePeriod' ,null ,null, null,14400000);  
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(17,'xinco/FileIndexer_1_Class' ,null ,'com.bluecubs.xinco.index.filetypes.XincoIndexAdobePDF', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(18,'xinco/FileIndexer_1_Ext' ,null ,'pdf', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(19,'xinco/FileIndexer_2_Class' ,null ,'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftWord', null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(20,'xinco/FileIndexer_2_Ext' ,null ,'doc', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(21,'xinco/FileIndexer_3_Class' ,null ,'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftExcel', null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(22,'xinco/FileIndexer_3_Ext' ,null ,'xls', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(23,'xinco/FileIndexer_4_Class' ,null ,'com.bluecubs.xinco.index.filetypes.XincoIndexMicrosoftPowerpoint', null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(24,'xinco/FileIndexer_4_Ext' ,null ,'ppt', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(25,'xinco/FileIndexer_5_Class' ,null ,'com.bluecubs.xinco.index.filetypes.XincoIndexHTML', null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(26,'xinco/FileIndexer_5_Ext' ,null ,'asp;htm;html;jsf;jsp;php;php3;php4', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(27,'xinco/IndexNoIndex' ,null ,';aac;ac3;ace;ade;adp;aif;aifc;aiff;amf;arc;arj;asx;au;avi;b64;bh;bmp;bz;bz2;cab;cda;chm;class;com;div;divx;ear;exe;far;fla;gif;gz;hlp;ico;;iso;jar;jpe;jpeg;jpg;lha;lzh;mda;mdb;mde;mdn;mdt;mdw;mid;midi;mim;mod;mov;mp1;mp2;mp2v;mp3;mp4;mpa;mpe;mpeg;mpg;mpg4;mpv2;msi;ntx;ocx;ogg;ogm;okt;pae;pcx;pk3;png;pot;ppa;pps;ppt;pwz;qwk;ra;ram;rar;raw;rep;rm;rmi;snd;swf;swt;tar;taz;tbz;tbz2;tgz;tif;tiff;uu;uue;vxd;war;wav;wbm;wbmp;wma;wmd;wmf;wmv;xpi;xxe;z;zip;zoo;;;', null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(28,'xinco/JNDIDB' ,null ,'java:comp/env/jdbc/XincoDB', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(29,'xinco/MaxSearchResult' ,100 ,null, null,null);  
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(30,'setting.email.host',null,'smtp.gmail.com', null,null); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(31,'setting.email.user',null,'info@bluecubs.com', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(32,'setting.email.password',null,'password', null,null);  
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(33,'setting.email.port',null,'25', null,null);  
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(34,'setting.index.lock',null,null,false,null);  
 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(35,'setting.allowoutsidelinks',null,null,true,null);  

INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(36,'setting.securitycheck.enable',null,null,true,null);   

/*Inserts 51-100 reserved for Workflow settings*/ 

UPDATE XINCO_CORE_DATA_TYPE SET DESIGNATION = 'general.data.type.file' WHERE ID =1

UPDATE XINCO_CORE_DATA_TYPE SET DESIGNATION = 'general.data.type.text' WHERE ID =2

UPDATE XINCO_CORE_DATA_TYPE SET DESIGNATION = 'general.data.type.URL' WHERE ID =3

UPDATE XINCO_CORE_DATA_TYPE SET DESIGNATION = 'general.data.type.contact' WHERE ID =4

UPDATE XINCO_CORE_group SET DESIGNATION = 'general.group.admin' WHERE ID =1;

UPDATE XINCO_CORE_group SET DESIGNATION = 'general.group.allusers' WHERE ID =2;

UPDATE XINCO_CORE_group SET DESIGNATION = 'general.group.public' WHERE ID =3;