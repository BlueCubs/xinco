-- Original Designation: 
-- Original Designation: unknown
UPDATE xinco_core_language SET designation = 'onbekend' WHERE id=1;
-- Original Designation: English
UPDATE xinco_core_language SET designation = 'Engels' WHERE id=2;
-- Original Designation: German
UPDATE xinco_core_language SET designation = 'Duits' WHERE id=3;
-- Original Designation: French
UPDATE xinco_core_language SET designation = 'Frans' WHERE id=4;
-- Original Designation: Italian
UPDATE xinco_core_language SET designation = 'Italiaans' WHERE id=5;
-- Original Designation: Spanish
UPDATE xinco_core_language SET designation = 'Spaans' WHERE id=6;
-- Original Designation: Russian
UPDATE xinco_core_language SET designation = 'Russisch' WHERE id=7;



-- Original Designation: File Description: Files stored on the server file system.
UPDATE xinco_core_data_type SET designation = 'Data', description = 'Op server opgeslagen data.' WHERE id=1;
-- Original Designation: Text Description: Text stored inside the data base.
UPDATE xinco_core_data_type SET designation = 'Text', description = 'In de database opgeslagen tekst.' WHERE id=2;
-- Original Designation: URL Description: General uniform ressource locator.
UPDATE xinco_core_data_type SET designation = 'URL', description = 'Algemene externe links.' WHERE id=3;
-- Original Designation: Contact Description: Contact', 'Personal and business contacts.
UPDATE xinco_core_data_type SET designation = 'Contact', description = 'Persoonlijke en organisatiecontacten' WHERE id=4;



-- Original Designation: File Name
UPDATE xinco_core_data_type_attribute SET designation = 'Bestandsnaam' WHERE xinco_core_data_type_id=1 AND attribute_id=1;
-- Original Designation: Size
UPDATE xinco_core_data_type_attribute SET designation = 'Grootte' WHERE xinco_core_data_type_id=1 AND attribute_id=2;
-- Original Designation: Checksum
UPDATE xinco_core_data_type_attribute SET designation = 'Checksum' WHERE xinco_core_data_type_id=1 AND attribute_id=3;

-- Original Designation: Revision Model
UPDATE xinco_core_data_type_attribute SET designation = 'Revisiemodel' WHERE xinco_core_data_type_id=1 AND attribute_id=4;
-- Original Designation: Archiving Model
UPDATE xinco_core_data_type_attribute SET designation = 'Archiveringsmodel' WHERE xinco_core_data_type_id=1 AND attribute_id=5;
-- Original Designation: Archiving Date
UPDATE xinco_core_data_type_attribute SET designation = 'Archiveringsdatum' WHERE xinco_core_data_type_id=1 AND attribute_id=6;
-- Original Designation: Archiving Days
UPDATE xinco_core_data_type_attribute SET designation = 'Archiveringsdagen' WHERE xinco_core_data_type_id=1 AND attribute_id=7;
-- Original Designation: Archiving Location
UPDATE xinco_core_data_type_attribute SET designation = 'Archiveringsplek' WHERE xinco_core_data_type_id=1 AND attribute_id=8;

-- Original Designation: Description
UPDATE xinco_core_data_type_attribute SET designation = 'Beschrijving' WHERE xinco_core_data_type_id=1 AND attribute_id=9;
-- Original Designation: Keyword 1
UPDATE xinco_core_data_type_attribute SET designation = 'Steekwoord_1' WHERE xinco_core_data_type_id=1 AND attribute_id=10;
-- Original Designation: Keyword 2
UPDATE xinco_core_data_type_attribute SET designation = 'Steekwoord_2' WHERE xinco_core_data_type_id=1 AND attribute_id=11;
-- Original Designation: Keyword 3
UPDATE xinco_core_data_type_attribute SET designation = 'Steekwoord_3' WHERE xinco_core_data_type_id=1 AND attribute_id=12;

-- Original Designation: Text
UPDATE xinco_core_data_type_attribute SET designation = 'Tekst' WHERE xinco_core_data_type_id=2 AND attribute_id=1;

-- Original Designation: Salutation
UPDATE xinco_core_data_type_attribute SET designation = 'Aanspreektitel' WHERE xinco_core_data_type_id=4 AND attribute_id=1;
-- Original Designation: Firstname
UPDATE xinco_core_data_type_attribute SET designation = 'Voornaam' WHERE xinco_core_data_type_id=4 AND attribute_id=2;
-- Original Designation: Middle Name
UPDATE xinco_core_data_type_attribute SET designation = 'Tweede_Voornaam' WHERE xinco_core_data_type_id=4 AND attribute_id=3;
-- Original Designation: Last Name
UPDATE xinco_core_data_type_attribute SET designation = 'Achternaam' WHERE xinco_core_data_type_id=4 AND attribute_id=4;
-- Original Designation: Name Affix
UPDATE xinco_core_data_type_attribute SET designation = 'Achtervoegsel' WHERE xinco_core_data_type_id=4 AND attribute_id=5;
-- Original Designation: Phone (business)
UPDATE xinco_core_data_type_attribute SET designation = 'Telefoon_bedrijf' WHERE xinco_core_data_type_id=4 AND attribute_id=6;
-- Original Designation: Phone (private)
UPDATE xinco_core_data_type_attribute SET designation = 'Telefoon_prive' WHERE xinco_core_data_type_id=4 AND attribute_id=7;
-- Original Designation:  Phone (mobile)
UPDATE xinco_core_data_type_attribute SET designation = 'Telefoon_mobiel' WHERE xinco_core_data_type_id=4 AND attribute_id=8;
-- Original Designation: Fax
UPDATE xinco_core_data_type_attribute SET designation = 'Fax' WHERE xinco_core_data_type_id=4 AND attribute_id=9;
-- Original Designation: Email
UPDATE xinco_core_data_type_attribute SET designation = 'E-mail' WHERE xinco_core_data_type_id=4 AND attribute_id=10;
-- Original Designation: Website
UPDATE xinco_core_data_type_attribute SET designation = 'Website' WHERE xinco_core_data_type_id=4 AND attribute_id=11;
-- Original Designation: Street Address
UPDATE xinco_core_data_type_attribute SET designation = 'Straat' WHERE xinco_core_data_type_id=4 AND attribute_id=12;
-- Original Designation: Postal Code
UPDATE xinco_core_data_type_attribute SET designation = 'Postcode' WHERE xinco_core_data_type_id=4 AND attribute_id=13;
-- Original Designation: City
UPDATE xinco_core_data_type_attribute SET designation = 'Plaatsnaam' WHERE xinco_core_data_type_id=4 AND attribute_id=14;
-- Original Designation: State / Province
UPDATE xinco_core_data_type_attribute SET designation = 'Provincie' WHERE xinco_core_data_type_id=4 AND attribute_id=15;
-- Original Designation: Country
UPDATE xinco_core_data_type_attribute SET designation = 'Land' WHERE xinco_core_data_type_id=4 AND attribute_id=16;
-- Original Designation: Company Name
UPDATE xinco_core_data_type_attribute SET designation = 'Bedrijfsnaam' WHERE xinco_core_data_type_id=4 AND attribute_id=17;
-- Original Designation: Position
UPDATE xinco_core_data_type_attribute SET designation = 'Positie' WHERE xinco_core_data_type_id=4 AND attribute_id=18;
-- Original Designation: Notes
UPDATE xinco_core_data_type_attribute SET designation = 'Notities' WHERE xinco_core_data_type_id=4 AND attribute_id=19;



-- Original Designation: Administrators
UPDATE xinco_core_group SET designation = 'Administratoren' WHERE id=1;
-- Original Designation: AllUsers
UPDATE xinco_core_group SET designation = 'AlleGebruikers' WHERE id=2;
-- Original Designation: Public
UPDATE xinco_core_group SET designation = 'Publiek' WHERE id=3;



-- Original Designation: xincoRoot
UPDATE xinco_core_node SET designation = 'xincoRoot' WHERE id=1;
-- Original Designation: Trash
UPDATE xinco_core_node SET designation = 'Prullenbak' WHERE id=2;
-- Original Designation: Temp
UPDATE xinco_core_node SET designation = 'Tijdelijk' WHERE id=3;
-- Original Designation: News
UPDATE xinco_core_node SET designation = 'Nieuws' WHERE id=4;
