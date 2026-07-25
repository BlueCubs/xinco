-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = 'Desconocido' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = 'Englisch' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = 'Deutsch' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = 'Französisch' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = 'Italienisch' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = 'Spanisch' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = 'Russisch' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = 'Descripción Archivo', description = 'Archivos almacenados en el sistema de archivos del servidor.' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Texto', description = 'Texto almacenado en la base de datos.' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'Enlace.' WHERE id=3;
-- Original Designation: Contact Description: Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Contacto', description = 'Contactos personales y de negocios' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nombre_del_Archivo' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'Tamaño' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'Verificación' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Modelo_de_Revisión' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Modelo_de_Archivo' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = 'Fecha_de_Archivo' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = 'Días_Archivado' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = 'Localización_de_Archivo' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'Descripción' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword_1
UPDATE xinco_core_data_type_attribute SET designation = 'Palabra_Clave_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword_2
UPDATE xinco_core_data_type_attribute SET designation = 'Palabra_Clave_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword_3
UPDATE xinco_core_data_type_attribute SET designation = 'Palabra_Clave_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Texto' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Saludo' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'Nombre' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Segundo Nombre' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Apellido' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = 'Prefijo' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = 'Telefono_Trabajo' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon0_Privado' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = 'Telefono_Móvil' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Fax' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'Email' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Website' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = 'Dirección' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = 'Código_Postal' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'Ciudad' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = 'Estado_Provincia' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'País' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nombre_Compañía' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'Posición' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'Notas' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = 'Administradores' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'Todos los Usuarios' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'Publico' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'xincoRoot' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'Basura' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Temp' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'Noticias' WHERE id=4;
