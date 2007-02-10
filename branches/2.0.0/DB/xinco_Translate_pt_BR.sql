-- Original Designation: 
-- Original Designation: unknown
-- unindentified
UPDATE xinco_core_language SET designation = 'desconhecido' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = 'Inglês' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = 'Alemão' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = 'Francês' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = 'Italiano' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = 'Espanhol' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = 'Russo' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = 'Database', description = 'Arquivos armazenados no sistema de arquivos do servidor.' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Texto', description = 'Texto armazenado na base de dados.' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'link externo.' WHERE id=3;
-- Original Designation: Contact Description: Contact', 'Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Contato', description = 'Contatos pessoais e de negócios' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nome do arquivo' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'Tamanho' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'Cheksum' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Revision_Model' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Model' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Date' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = 'Archiving_Days' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = 'Local de arquivamento' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'Descrição' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword_1
UPDATE xinco_core_data_type_attribute SET designation = 'Palavra-chave_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword_2
UPDATE xinco_core_data_type_attribute SET designation = 'Palavra-chave_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword_3
UPDATE xinco_core_data_type_attribute SET designation = 'Palavra-chave_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Texto' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Saldação' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'Primeiro_nome' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nome_do_meio' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Sobrenome' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = 'Prenome' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = 'Telefone_do_trabalho' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = 'Telefone_pessoal' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = 'Celular' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Fax' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'Email' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Website' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = 'Endereço' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = 'CEP' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'Cidade' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = 'Estado' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'Pais' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nome_da_Empresa' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'Posição' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'Notas' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = 'Administradores' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'Todos usuários' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'Público' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'xincoRoot' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'Lixeira' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Temp' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'Notícias' WHERE id=4;
