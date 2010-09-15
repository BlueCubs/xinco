-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = 'nieznany' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = 'Angielski' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = 'Niemiecki' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = 'Francuski' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = 'W³oski' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = 'Hiszpañski' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = 'Rosyjski' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = 'Plik', description = 'Pliki sk³adowane na serwerze systemu plików.' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Tekst', description = 'Tekst przechowywany wewn¹trz bazy danych.' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'Ogólny zunifikowany lokator zasobów.' WHERE id=3;
-- Original Designation: Contact Description: Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Kontakt', description = 'Kontakty osobiste i zawodowe.' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nazwa pliku' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'Rozmiar' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'Suma kontrolna' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Model_wersjonowania' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Model_archiwowania' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = 'Data_archiwowania' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = 'Dni_archiwowania' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = 'Po³o¿enie_archiwum' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'Opis' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword_1
UPDATE xinco_core_data_type_attribute SET designation = 'S³owo_kluczowe_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword_2
UPDATE xinco_core_data_type_attribute SET designation = 'S³owo_kluczowe_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword_3
UPDATE xinco_core_data_type_attribute SET designation = 'S³owo_kluczowe_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Tekst' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Powitanie' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'Imiê' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Drugie_imiê' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nazwisko' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = 'Przyrostek_imienia' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_do_pracy' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_prywatny' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_komórkowy' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Faks' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'Adres_email' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Strona_webowa' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = 'Ulica' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = 'Kod_pocztowy' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'Miasto' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = 'Wojewodztwo' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'Kraj' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nazwa_firmy' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'Stanowisko' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'Uwagi' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = ' Administratorzy' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'Wszyscy_u¿ytkownicy' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'Dostêp_publiczny' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'Folder_podstawowy' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'Œmietnik' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Tymczasowy' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'Nowoœci' WHERE id=4;
