-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = 'nieznane' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = 'angielski' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = 'niemiecki' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = 'francuski' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = 'wloski' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = 'hiszpanski' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = 'rosyjski' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = 'plik', description = 'w serwerze, w systemie plikowym, zaprogramowane pliki' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Text', description = 'Text zaprogramoway w bazie danych' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'ogolny zewnetrzny link' WHERE id=3;
-- Original Designation: Contact Description: Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Kontakt', description = 'prywatne oraz zawodowe/biznesowe kontakty' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = 'nazwa pliku' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'rozmiar' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'sumy sprawdzajace/inspekcyjne' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = 'model rewizyjny' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = 'model archiwalny' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = 'data archiwalna' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = 'dni archiwowania' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = 'miejesce archiwacji' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'opis' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword 1
UPDATE xinco_core_data_type_attribute SET designation = 'slowo_kluczowe_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword 2
UPDATE xinco_core_data_type_attribute SET designation = 'slowo_kluczowe_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword 3
UPDATE xinco_core_data_type_attribute SET designation = 'slowo_kluczowe_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Text' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Anrede' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'imie' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = 'drugie_imie' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = 'nazwisko' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = 'dodatek' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_zawodowy' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_prywatny' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_komorkowy' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Fax' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'Email' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Website' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = 'ulica' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = 'kod pocztowy' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'miasto' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = 'wojewodztwo' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'panstwo' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = 'nazwa firmy' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'pozycja' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'notatki' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = 'Administratorzy' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'wszyscyUzytkownicy' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'publiczne' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'xincoRoot' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'smietnik' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Temp' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'Nowosci' WHERE id=4;
