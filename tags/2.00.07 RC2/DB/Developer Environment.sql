/*Set Xinco in Developer mode for aditional debugging messages*/
update xinco_setting set bool_value = 1 where id =10;

/*Set up workflow instance test*/

/*Create test workflow instance*/
INSERT INTO Workflow_Instance (id, Workflow_Template_id, Node_id, creationTime) VALUES(1,1,1,now() );

/*Instance properties*/
INSERT INTO Instance_Property (property_id,id,Workflow_Template_id, description, propertyString, propertyBool, propertyInt, propertyLong) VALUES(1,1,1,'workflow.activity.notify', null,1,0,null);
INSERT INTO Instance_Property (property_id,id,Workflow_Template_id, description, propertyString, propertyBool, propertyInt, propertyLong) VALUES(2,1,1,'workflow.step.evaluate', null,1,0,null);

/*Intance's nodes*/
INSERT INTO Workflow_Instance_has_Node (Workflow_Template_id, id, Node_id, completed, isStartNode, isEndNode) VALUES(1,1,1,1,1,0);
INSERT INTO Workflow_Instance_has_Node (Workflow_Template_id, id, Node_id, completed, isStartNode, isEndNode) VALUES(1,1,2,0,0,0);
INSERT INTO Workflow_Instance_has_Node (Workflow_Template_id, id, Node_id, completed, isStartNode, isEndNode) VALUES(1,1,3,0,0,0);
INSERT INTO Workflow_Instance_has_Node (Workflow_Template_id, id, Node_id, completed, isStartNode, isEndNode) VALUES(1,1,4,0,0,0);
INSERT INTO Workflow_Instance_has_Node (Workflow_Template_id, id, Node_id, completed, isStartNode, isEndNode) VALUES(1,1,5,0,0,0);
INSERT INTO Workflow_Instance_has_Node (Workflow_Template_id, id, Node_id, completed, isStartNode, isEndNode) VALUES(1,1,6,0,0,1);

/*Intance's transactions*/
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(1,1,2,1,1);
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(2,1,6,2,0);
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(3,1,3,2,0);
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(4,1,4,3,0);
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(5,1,4,5,0);
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(6,1,1,4,0);
INSERT INTO Workflow_Instance_has_Transaction (Transaction_id, Workflow_Template_id, id, Node_id, completed) VALUES(7,1,6,4,0); 

/*Template's nodes*/
INSERT INTO Workflow_Template_has_Node (Node_id, id,isStartNode,isEndNode) VALUES(1, 1,1,0);INSERT INTO Workflow_Template_has_Node (Node_id, id,isStartNode,isEndNode) VALUES(2, 1,0,0); INSERT INTO Workflow_Template_has_Node (Node_id, id,isStartNode,isEndNode) VALUES(3, 1,0,0); INSERT INTO Workflow_Template_has_Node (Node_id, id,isStartNode,isEndNode) VALUES(4, 1,0,0);INSERT INTO Workflow_Template_has_Node (Node_id, id,isStartNode,isEndNode) VALUES(5, 1,0,0);INSERT INTO Workflow_Template_has_Node (Node_id, id,isStartNode,isEndNode) VALUES(6, 1,0,0); 

/*Template's transactions*/
INSERT INTO Workflow_Template_has_Transaction (Transaction_id, id) VALUES(1, 1);INSERT INTO Workflow_Template_has_Transaction (Transaction_id, id) VALUES(2, 1); INSERT INTO Workflow_Template_has_Transaction (Transaction_id, id) VALUES(3, 1);

