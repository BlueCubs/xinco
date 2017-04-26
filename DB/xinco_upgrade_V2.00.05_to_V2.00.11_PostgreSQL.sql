INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(39,'password.max',-1,null,false,null);
INSERT INTO xinco_setting (id, description, int_value, string_value, bool_value, long_value) VALUES(40,'password.min',-1,null,false,null);
/*Mark all users to change password*/
update xinco_core_user set status_number = 3 where id != 1;