CREATE TABLE Activity (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  description VARCHAR(45) NOT NULL,
  className VARCHAR(255) NOT NULL,
  PRIMARY KEY(id)
)
TYPE=InnoDB;

INSERT INTO Activity (id, description,className) VALUES(1,'workflow.activity.notify','com.bluecubs.xinco.workflow.server.activity.Notify');


CREATE TABLE Activity_has_Property (
  Activity_id INTEGER UNSIGNED NOT NULL,
  Property_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Activity_id, Property_id),
  INDEX Activity_has_Property_FKIndex1(Activity_id),
  INDEX Activity_has_Property_FKIndex2(Property_id)
)
TYPE=InnoDB;

INSERT INTO Activity_has_Property (Activity_id, Property_id) VALUES(1,1);
INSERT INTO Activity_has_Property (Activity_id, Property_id) VALUES(1,2); 


CREATE TABLE Activity_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  className VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE Activity_Type (
  activity_type_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  activity_type VARCHAR(45) NOT NULL,
  PRIMARY KEY(activity_type_id, id),
  INDEX Activity_Type_FKIndex1(id)
)
TYPE=InnoDB;

INSERT INTO Activity_Type (activity_type_id, id, activity_type) VALUES(1,1,'workflow.activity.notify');


CREATE TABLE activity_type_properties (
  attribute_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  activity_type_id INTEGER UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  PRIMARY KEY(attribute_id, id, activity_type_id),
  INDEX activity_type_properties_FKIndex1(activity_type_id, id)
)
TYPE=InnoDB;

INSERT INTO activity_type_properties (attribute_id, id, activity_type_id, designation) VALUES(1,1,1,'workflow.property.email');
INSERT INTO activity_type_properties (attribute_id, id, activity_type_id, designation) VALUES(2,1,1,'workflow.property.email.message'); 
INSERT INTO activity_type_properties (attribute_id, id, activity_type_id, designation) VALUES(3,1,1,'workflow.property.email.address');


CREATE TABLE Instance_Property (
  property_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  Workflow_Template_id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  propertyString VARCHAR(255) NULL,
  propertyBool BOOL NULL,
  propertyInt INTEGER UNSIGNED NULL,
  propertyLong BIGINT NULL,
  PRIMARY KEY(property_id, id, Workflow_Template_id),
  INDEX Instance_Property_FKIndex1(id, Workflow_Template_id)
)
TYPE=InnoDB;

CREATE TABLE Instance_Property_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  propertyString VARCHAR(255) NULL,
  propertyBool BOOL NULL,
  propertyInt INTEGER UNSIGNED NULL,
  propertyLong BIGINT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE Node (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  description VARCHAR(45) NOT NULL,
  PRIMARY KEY(id),
  INDEX Node_FKIndex1(id)
)
TYPE=InnoDB;

INSERT INTO Node (id, description) VALUES(1,'workflow.step.notify');
INSERT INTO Node (id, description) VALUES(2, 'workflow.step.evaluate');
INSERT INTO Node (id, description) VALUES(3, 'workflow.step.notifyoriginator');
INSERT INTO Node (id, description) VALUES(4, 'workflow.step.modify');
INSERT INTO Node (id, description) VALUES(5, 'workflow.step.checkout');
INSERT INTO Node (id, description) VALUES(6, 'workflow.step.checkin');


CREATE TABLE Node_has_Activity (
  Node_id INTEGER UNSIGNED NOT NULL,
  Activity_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Node_id, Activity_id),
  INDEX Node_has_Activity_FKIndex1(Node_id),
  INDEX Node_has_Activity_FKIndex2(Activity_id)
)
TYPE=InnoDB;

CREATE TABLE Node_has_Property (
  Node_id INTEGER UNSIGNED NOT NULL,
  Property_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Node_id, Property_id),
  INDEX Node_has_Property_FKIndex1(Node_id),
  INDEX Node_has_Property_FKIndex2(Property_id)
)
TYPE=InnoDB;

INSERT INTO Node_has_Property (Node_id, Property_id) VALUES(2, 4);


CREATE TABLE Node_has_Transaction (
  Node_id INTEGER UNSIGNED NOT NULL,
  Transaction_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Node_id, Transaction_id),
  INDEX Node_has_Transaction_FKIndex1(Node_id),
  INDEX Node_has_Transaction_FKIndex2(Transaction_id)
)
TYPE=InnoDB;

INSERT INTO Node_has_Transaction (Node_id, Transaction_id) VALUES(1,1);
INSERT INTO Node_has_Transaction (Node_id, Transaction_id) VALUES(2,2);
INSERT INTO Node_has_Transaction (Node_id, Transaction_id) VALUES(2,3);


CREATE TABLE Node_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE Property (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  description VARCHAR(45) NOT NULL,
  propertyString VARCHAR(255) NULL,
  propertyBool BOOL NULL,
  propertyInt INTEGER UNSIGNED NULL,
  propertyLong BIGINT NULL,
  PRIMARY KEY(id)
)
TYPE=InnoDB;

INSERT INTO Property (id, description, propertyString, propertyBool, propertyInt, Propertylong) VALUES(1, 'workflow.property.email', 'javier.ortiz.78@gmail.com',null, null,null ); 
INSERT INTO Property (id, description, propertyString, propertyBool, propertyInt, Propertylong) VALUES(2, 'workflow.property.email.message', 'Test message',null, 1,null );  
INSERT INTO Property (id, description, propertyString, propertyBool, propertyInt, Propertylong) VALUES(3, 'workflow.activity.notify', null,true,0, null);
INSERT INTO Property (id, description, propertyString, propertyBool, propertyInt, Propertylong) VALUES(4, 'workflow.step.evaluate', null,true,0, null);


CREATE TABLE Property_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  propertyString VARCHAR(255) NULL,
  propertyBool BOOL NULL,
  propertyInt INTEGER UNSIGNED NULL,
  longProperty BIGINT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE Resource_has_Activity (
  xinco_core_user_id INTEGER UNSIGNED NOT NULL,
  Activity_id INTEGER UNSIGNED NOT NULL,
  assignedOn TIMESTAMP NOT NULL,
  completedOn TIMESTAMP NULL,
  PRIMARY KEY(xinco_core_user_id, Activity_id),
  INDEX Resource_has_Activity_FKIndex1(xinco_core_user_id),
  INDEX Resource_has_Activity_FKIndex2(Activity_id)
)
TYPE=InnoDB;

CREATE TABLE Transaction (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  Node_id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY(id),
  INDEX Transaction_FKIndex1(Node_id)
)
TYPE=InnoDB;

INSERT INTO Transaction (id, description, Node_id) VALUES(1, 'workflow.transaction.notification.complete',2);
INSERT INTO Transaction (id, description, Node_id) VALUES(2, 'workflow.transaction.evaluate.accept',6); 
INSERT INTO Transaction (id, description, Node_id) VALUES(3, 'workflow.transaction.evaluate.reject',3); 
INSERT INTO Transaction (id, description, Node_id) VALUES(4, 'workflow.transaction.notificationoriginator.complete',4); 
INSERT INTO Transaction (id, description, Node_id) VALUES(5, 'workflow.transaction.checkout.complete',4); 
INSERT INTO Transaction (id, description, Node_id) VALUES(6, 'workflow.transaction.desition.yes',1); 
INSERT INTO Transaction (id, description, Node_id) VALUES(7, 'workflow.transaction.desition.no',6); 


CREATE TABLE Transaction_has_Activity (
  Activity_id INTEGER UNSIGNED NOT NULL,
  Transaction_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Activity_id, Transaction_id),
  INDEX Transaction_has_Activity_FKIndex1(Activity_id),
  INDEX Transaction_has_Activity_FKIndex2(Transaction_id)
)
TYPE=InnoDB;

CREATE TABLE Transaction_has_Property (
  Property_id INTEGER UNSIGNED NOT NULL,
  Transaction_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Property_id, Transaction_id),
  INDEX Transaction_has_Property_FKIndex1(Property_id),
  INDEX Transaction_has_Property_FKIndex2(Transaction_id)
)
TYPE=InnoDB;

INSERT INTO Transaction_has_Property (Property_id, Transaction_id) VALUES(3,1);


CREATE TABLE Transaction_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  Node_id INTEGER UNSIGNED NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Instance (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  Workflow_Template_id INTEGER UNSIGNED NOT NULL,
  Node_id INTEGER UNSIGNED NOT NULL,
  creationTime TIMESTAMP NOT NULL,
  PRIMARY KEY(id, Workflow_Template_id),
  INDEX Workflow_Instance_FKIndex1(Workflow_Template_id),
  INDEX Workflow_Instance_FKIndex2(Node_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Instance_has_Activity (
  Activity_id INTEGER UNSIGNED NOT NULL,
  Workflow_Instance_Workflow_Template_id INTEGER UNSIGNED NOT NULL,
  Workflow_Instance_id INTEGER UNSIGNED NOT NULL,
  Transaction_id INTEGER UNSIGNED NOT NULL,
  Node_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(Activity_id, Workflow_Instance_Workflow_Template_id, Workflow_Instance_id),
  INDEX Workflow_Instance_has_Activity_FKIndex1(Activity_id),
  INDEX Workflow_Instance_has_Activity_FKIndex2(Workflow_Instance_id, Workflow_Instance_Workflow_Template_id),
  INDEX Workflow_Instance_has_Activity_FKIndex3(Node_id),
  INDEX Workflow_Instance_has_Activity_FKIndex4(Transaction_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Instance_has_Node (
  Node_id INTEGER UNSIGNED NOT NULL,
  Workflow_Template_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  completed BOOL NOT NULL DEFAULT false,
  isStartNode BOOL NOT NULL DEFAULT false,
  isEndNode BOOL NOT NULL DEFAULT false,
  PRIMARY KEY(Node_id, Workflow_Template_id, id),
  INDEX Workflow_Instance_has_Node_FKIndex1(Node_id),
  INDEX Workflow_Instance_has_Node_FKIndex2(id, Workflow_Template_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Instance_has_Transaction (
  Transaction_id INTEGER UNSIGNED NOT NULL,
  Workflow_Template_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  Node_id INTEGER UNSIGNED NOT NULL,
  completed BOOL NOT NULL DEFAULT false,
  PRIMARY KEY(Transaction_id, Workflow_Template_id, id, Node_id),
  INDEX Workflow_Instance_has_Transaction_FKIndex1(Transaction_id),
  INDEX Workflow_Instance_has_Transaction_FKIndex2(id, Workflow_Template_id),
  INDEX Workflow_Instance_has_Transaction_FKIndex3(Node_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Instance_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  creationTime TIMESTAMP NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE workflow_log (
  id INTEGER UNSIGNED NOT NULL,
  Workflow_Instance_Workflow_Template_id INTEGER UNSIGNED NOT NULL,
  Workflow_Instance_id INTEGER UNSIGNED NOT NULL,
  xinco_core_user_id INTEGER UNSIGNED NOT NULL,
  op_code INTEGER UNSIGNED NOT NULL,
  op_datetime DATETIME NOT NULL,
  op_description VARCHAR(255) NOT NULL,
  PRIMARY KEY(id, Workflow_Instance_Workflow_Template_id, Workflow_Instance_id),
  INDEX workflow_log_FKIndex1(xinco_core_user_id),
  INDEX workflow_log_FKIndex2(Workflow_Instance_id, Workflow_Instance_Workflow_Template_id)
)
TYPE=InnoDB;

CREATE TABLE workflow_log_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  op_code INTEGER UNSIGNED NOT NULL,
  op_datetime DATETIME NOT NULL,
  op_description VARCHAR(255) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Template (
  id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  PRIMARY KEY(id)
)
TYPE=InnoDB;

INSERT INTO Workflow_Template (id, description) VALUES(1,'workflow.modify');


CREATE TABLE Workflow_Template_has_Node (
  id INTEGER UNSIGNED NOT NULL,
  Node_id INTEGER UNSIGNED NOT NULL,
  isStartNode BOOL NOT NULL DEFAULT false,
  isEndNode BOOL NOT NULL DEFAULT false,
  PRIMARY KEY(id, Node_id),
  INDEX Workflow_Template_has_Node_FKIndex1(id),
  INDEX Workflow_Template_has_Node_FKIndex2(Node_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Template_has_Transaction (
  id INTEGER UNSIGNED NOT NULL,
  Transaction_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(id, Transaction_id),
  INDEX Workflow_Template_has_Transaction_FKIndex1(id),
  INDEX Workflow_Template_has_Transaction_FKIndex2(Transaction_id)
)
TYPE=InnoDB;

CREATE TABLE Workflow_Template_t (
  record_id INTEGER UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  description VARCHAR(45) NOT NULL,
  PRIMARY KEY(record_id)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_group (
  id INTEGER UNSIGNED NOT NULL,
  designation VARCHAR(255) NOT NULL,
  status_number INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  INDEX xinco_core_group_index_status(status_number)
)
TYPE=InnoDB;

/*Entries 51-100 reserved for Workflow*/
INSERT INTO xinco_core_group VALUES (51, 'Editors', 1);    
INSERT INTO xinco_core_group VALUES (52, 'Approver', 1);   
   


CREATE TABLE xinco_core_user (
  id INTEGER UNSIGNED NOT NULL,
  username VARCHAR(255) NOT NULL,
  userpassword VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  status_number INTEGER UNSIGNED NOT NULL,
  attempts INTEGER UNSIGNED NOT NULL DEFAULT 0,
  last_modified DATE NOT NULL,
  PRIMARY KEY(id),
  INDEX xinco_core_user_index_username(username),
  INDEX xinco_core_user_index_status(status_number)
)
TYPE=InnoDB;

CREATE TABLE xinco_core_user_has_xinco_core_group (
  xinco_core_group_id INTEGER UNSIGNED NOT NULL,
  xinco_core_user_id INTEGER UNSIGNED NOT NULL,
  status_number INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(xinco_core_group_id, xinco_core_user_id),
  INDEX xinco_core_user_has_xinco_core_group_FKIndex3(xinco_core_group_id),
  INDEX xinco_core_user_has_xinco_core_group_index_status(status_number),
  INDEX xinco_core_user_has_xinco_core_group_FKIndex1(xinco_core_group_id),
  INDEX xinco_core_user_has_xinco_core_group_FKIndex2(xinco_core_user_id)
)
TYPE=InnoDB;

INSERT INTO xinco_core_user_has_xinco_core_group VALUES (1, 1, 1);
INSERT INTO xinco_core_user_has_xinco_core_group VALUES (1, 2, 1);  
INSERT INTO xinco_core_user_has_xinco_core_group VALUES (2, 2, 1);  


CREATE TABLE xinco_core_user_modified_record (
  record_id INTEGER(10) UNSIGNED NOT NULL,
  id INTEGER UNSIGNED NOT NULL,
  mod_Time TIMESTAMP NOT NULL,
  mod_Reason VARCHAR(255) NULL,
  PRIMARY KEY(record_id, id),
  INDEX xinco_core_user_modified_record_FKIndex1(id)
)
TYPE=InnoDB;

CREATE TABLE xinco_id (
  tablename VARCHAR(255) NOT NULL,
  last_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(tablename)
)
TYPE=InnoDB;

INSERT INTO xinco_id (tablename, last_id) VALUES('workflow_instance',0);
INSERT INTO xinco_id (tablename, last_id) VALUES('workflow_template',1000);
INSERT INTO xinco_id (tablename, last_id) VALUES('node',1000);
INSERT INTO xinco_id (tablename, last_id) VALUES('activity',1000);
INSERT INTO xinco_id (tablename, last_id) VALUES('property',1000);
INSERT INTO xinco_id (tablename, last_id) VALUES('transaction',1000);
INSERT INTO xinco_id (tablename, last_id) VALUES('workflow_log',0);
INSERT INTO xinco_id (tablename, last_id) VALUES('instance_property',0);


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

/*Inserts 1-50 reserved for Xinco DMS*/
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(51,'workflow.version.high', 1,null,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(52,'workflow.version.med', 0,null ,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(53,'workflow.version.low', 0,null,null ); 
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value) VALUES(54,'workflow.version.postfix', null,null ,null );



