-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = '未知' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = '英语' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = '德语' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = '法语' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = '意大利语' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = '西班牙语' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = '俄语' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = '文件', description = '存储于文件系统' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = '文本', description = '存储与数据库服务器' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = '网址', description = '常用站点地址链接' WHERE id=3;
-- Original Designation: Contact Description: Personal and business contacts.
UPDATE xinco_core_data_type SET designation = '通讯录', description = '个人及商业通讯录' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = '文件名' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = '文件大小' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = '校验总和' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = '修订模板' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = '归档模板' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = '归档日期' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = '归档天数' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = '归档位置' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = '描述' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword_1
UPDATE xinco_core_data_type_attribute SET designation = '关键词_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword_2
UPDATE xinco_core_data_type_attribute SET designation = '关键词_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword_3
UPDATE xinco_core_data_type_attribute SET designation = '关键词_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = '文本' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－称谓' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－名' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－中间名（外）' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－姓' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－名词缀（外）' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－办公电话' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－住宅电话' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－移动电话' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－传真' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－电子邮件' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－主页' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－地址' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－邮政编码' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－城市' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－省' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－国家' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－公司名称' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－位置' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = '通讯录－笔记' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = '管理员组' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = '所有用户组' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = '公共组' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = '根' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = '回收站' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = '临时文件夹' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = '新闻' WHERE id=4;
