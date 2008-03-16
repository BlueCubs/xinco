alter table xinco_core_user drop INDEX xinco_core_user_index_username;
alter table xinco_core_user add UNIQUE INDEX xinco_core_user_index_username(username);

alter table xinco_core_group add UNIQUE INDEX xinco_core_group_index_designation(designation);

/* DOES NOT EXIST IN OLD UPGRADED DATABASE - UNCOMMENT IF APPLICABLE! */
/* alter table xinco_core_language drop INDEX xinco_core_language_unique; */
alter table xinco_core_language add UNIQUE INDEX xinco_core_language_index_sign(sign);
alter table xinco_core_language add UNIQUE INDEX xinco_core_language_index_designation(designation);
