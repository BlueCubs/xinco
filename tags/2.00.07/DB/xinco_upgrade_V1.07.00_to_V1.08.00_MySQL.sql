UPDATE xinco_core_data_type_attribute SET attribute_id=attribute_id+5 WHERE xinco_core_data_type_id=1 AND attribute_id>=4 AND attribute_id<=7;

INSERT INTO xinco_core_data_type_attribute VALUES (1, 4, 'Revision_Model', 'unsignedint', 0);       

INSERT INTO xinco_core_data_type_attribute VALUES (1, 5, 'Archiving_Model', 'unsignedint', 0);       

INSERT INTO xinco_core_data_type_attribute VALUES (1, 6, 'Archiving_Date', 'datetime', 0);      

INSERT INTO xinco_core_data_type_attribute VALUES (1, 7, 'Archiving_Days', 'unsignedint', 0);      

INSERT INTO xinco_core_data_type_attribute VALUES (1, 8, 'Archiving_Location', 'text', 0);      





INSERT INTO xinco_add_attribute
SELECT xaa.xinco_core_data_id, xaa.attribute_id+5, xaa.attrib_int, xaa.attrib_unsignedint, xaa.attrib_double, xaa.attrib_varchar, xaa.attrib_text, xaa.attrib_datetime
FROM xinco_core_data xcd, xinco_add_attribute xaa
WHERE xcd.id=xaa.xinco_core_data_id
AND xcd.xinco_core_data_type_id=1
AND xaa.attribute_id >= 4
AND xaa.attribute_id <= 7;

DELETE xinco_add_attribute
FROM xinco_core_data xcd, xinco_add_attribute xaa
WHERE xcd.id=xaa.xinco_core_data_id
AND xcd.xinco_core_data_type_id=1
AND xaa.attribute_id >= 4
AND xaa.attribute_id <= 7;


-- Revision Model
INSERT INTO xinco_add_attribute
SELECT xcd.id, 4, NULL, 1, NULL, NULL, NULL, NULL
FROM xinco_core_data xcd
WHERE xcd.xinco_core_data_type_id=1;

-- Archiving Model
INSERT INTO xinco_add_attribute
SELECT xcd.id, 5, NULL, 0, NULL, NULL, NULL, NULL
FROM xinco_core_data xcd
WHERE xcd.xinco_core_data_type_id=1;

-- Archiving Date
INSERT INTO xinco_add_attribute
SELECT xcd.id, 6, NULL, NULL, NULL, NULL, NULL, NULL
FROM xinco_core_data xcd
WHERE xcd.xinco_core_data_type_id=1;

-- Archiving Timeframe (in days)
INSERT INTO xinco_add_attribute
SELECT xcd.id, 7, NULL, 0, NULL, NULL, NULL, NULL
FROM xinco_core_data xcd
WHERE xcd.xinco_core_data_type_id=1;

-- Archiving Location
INSERT INTO xinco_add_attribute
SELECT xcd.id, 8, NULL, NULL, NULL, NULL, '', NULL
FROM xinco_core_data xcd
WHERE xcd.xinco_core_data_type_id=1;
