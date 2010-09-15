UPDATE xinco_id SET last_id=last_id+1000;

UPDATE xinco_core_language SET id=id+1000 WHERE id>7;
UPDATE xinco_core_data_type SET id=id+1000 WHERE id>3;
UPDATE xinco_core_user SET id=id+1000 WHERE id>2;
UPDATE xinco_core_group SET id=id+1000 WHERE id>2;
UPDATE xinco_core_data SET id=id+1000 WHERE id>2;
UPDATE xinco_core_ace SET id=id+1000 WHERE id>15;
UPDATE xinco_core_log SET id=id+1000 WHERE id>2;
-- ----------------------------------------------------------
-- Does NOT work because xinco_core_node references itself!!!
-- This is a MySQL issue, trying to avoid infinite loops!
-- ----------------------------------------------------------
-- UPDATE xinco_core_node SET id=id+1000 WHERE id>3;
-- ----------------------------------------------------------
-- You need to free xinco_core_node ids required for this
-- upgrade script manually and move the affected nodes
-- to higher ids (above 1000).
-- Example: (x=id to free / y?=ids of reference to x / z=new id)
-- UPDATE xinco_core_node SET xinco_core_node_id=1 WHERE id=y?;
-- ...                                                   id=y?;
-- UPDATE xinco_core_node SET id=z WHERE id=x;
-- UPDATE xinco_core_node SET xinco_core_node_id=z WHERE id=y?;
-- ...                                                   id=y?;
-- ----------------------------------------------------------

INSERT INTO xinco_core_node VALUES (4, 1, 1, 'News', 1); 

INSERT INTO xinco_core_ace VALUES (16, 1, NULL, 4, NULL, 1, 1, 1, 1);          

INSERT INTO xinco_core_ace VALUES (17, NULL, 1, 4, NULL, 1, 1, 1, 1);           

INSERT INTO xinco_core_ace VALUES (18, NULL, 2, 4, NULL, 1, 0, 0, 0);           


INSERT INTO xinco_core_data_type VALUES (4, 'Contact', 'Personal and business contacts.');      

INSERT INTO xinco_core_data_type_attribute VALUES (4, 1, 'Salutation', 'varchar', 255);     

INSERT INTO xinco_core_data_type_attribute VALUES (4, 2, 'First Name', 'varchar', 255);     

INSERT INTO xinco_core_data_type_attribute VALUES (4, 3, 'Middle Name', 'varchar', 255);     

INSERT INTO xinco_core_data_type_attribute VALUES (4, 4, 'Last Name', 'varchar', 255);     

INSERT INTO xinco_core_data_type_attribute VALUES (4, 5, 'Name Affix', 'varchar', 255);     

INSERT INTO xinco_core_data_type_attribute VALUES (4, 6, 'Phone (business)', 'varchar', 255);      

INSERT INTO xinco_core_data_type_attribute VALUES (4, 7, 'Phone (private)', 'varchar', 255);      

INSERT INTO xinco_core_data_type_attribute VALUES (4, 8, 'Phone (mobile)', 'varchar', 255);      

INSERT INTO xinco_core_data_type_attribute VALUES (4, 9, 'Fax', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 10, 'Email', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 11, 'Website', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 12, 'Street Address', 'text', 0);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 13, 'Postal Code', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 14, 'City', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 15, 'State / Province', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 16, 'Country', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 17, 'Company Name', 'varchar', 255);      

INSERT INTO xinco_core_data_type_attribute VALUES (4, 18, 'Position', 'varchar', 255);       

INSERT INTO xinco_core_data_type_attribute VALUES (4, 19, 'Notes', 'text', 0);         

