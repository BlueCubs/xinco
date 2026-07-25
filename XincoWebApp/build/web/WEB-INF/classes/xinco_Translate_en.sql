-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = 'unknown' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = 'English' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = 'German' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = 'French' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = 'Italian' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = 'Spanish' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = 'Russian' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = 'File', description = 'Files stored on the server file system.' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Text', description = 'Text stored inside the data base.' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'General uniform ressource locator.' WHERE id=3;
-- Original Designation: Contact Description: Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Contact', description = 'Personal and business contacts.' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = 'File Name' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'Size' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'Checksum' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Revision_Model' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Model' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Date' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Days' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Location' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'Description' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword_1
UPDATE xinco_core_data_type_attribute SET designation = 'Keyword_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword_2
UPDATE xinco_core_data_type_attribute SET designation = 'Keyword_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword_3
UPDATE xinco_core_data_type_attribute SET designation = 'Keyword_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Text' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Salutation' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'Firstname' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Middle_Name' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Last_Name' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = 'Name_Affix' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = 'Phone_business' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = 'Phone_private' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = 'Phone_mobile' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Fax' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'Email' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Website' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = 'Street_Address' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = 'Postal_Code' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'City' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = 'State_Province' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'Country' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Company_Name' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'Position' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'Notes' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = ' Administrators' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'AllUsers' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'Public' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'xincoRoot' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'Trash' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Temp' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'News' WHERE id=4;
