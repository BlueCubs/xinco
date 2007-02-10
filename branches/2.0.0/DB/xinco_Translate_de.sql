-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = 'unbekannt' WHERE id=1;
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
UPDATE xinco_core_data_type SET designation = 'Datei', description = 'Im Server-Dateisystem gespeicherte Dateien.' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Text', description = 'In der Datenbank gespeicherter Text.' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'Allgemeiner externer Link.' WHERE id=3;
-- Original Designation: Contact Description: Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Kontakt', description = 'Persönliche und Geschäftskontakte' WHERE id=4;



-- Original Designation: File_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Dateiname' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'Groesse' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'Pruefsumme' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Revisionsmodell' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving_Model
UPDATE xinco_core_data_type_attribute SET designation = 'Archivierungsmodell' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving_Date
UPDATE xinco_core_data_type_attribute SET designation = 'Archivierungsdatum' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving_Days
UPDATE xinco_core_data_type_attribute SET designation = 'Archivierungstage' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving_Location
UPDATE xinco_core_data_type_attribute SET designation = 'Archiverungsort' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'Beschreibung' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword 1
UPDATE xinco_core_data_type_attribute SET designation = 'Schluesselwort_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword 2
UPDATE xinco_core_data_type_attribute SET designation = 'Schluesselwort_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword 3
UPDATE xinco_core_data_type_attribute SET designation = 'Schluesselwort_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Text' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Anrede' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'Vorname' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Zweiter_Vorname' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Nachname' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name_Affix
UPDATE xinco_core_data_type_attribute SET designation = 'Namenszusatz' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone_business
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_geschaeftlich' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone_private
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_privat' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation: Phone_mobile
UPDATE xinco_core_data_type_attribute SET designation = 'Telefon_mobil' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Fax' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'Email' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Website' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street_Address
UPDATE xinco_core_data_type_attribute SET designation = 'Strasse' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal_Code
UPDATE xinco_core_data_type_attribute SET designation = 'PLZ' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'Stadt' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State_Province
UPDATE xinco_core_data_type_attribute SET designation = 'Bundesland' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'Land' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company_Name
UPDATE xinco_core_data_type_attribute SET designation = 'Firmenname' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'Position' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'Notizen' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = 'Administratoren' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'AlleBenutzer' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'Öffentlich' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'xincoRoot' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'Papierkorb' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Temp' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'News' WHERE id=4;
