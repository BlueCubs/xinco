-- ============================================================
-- V1_4: Drop legacy hand-rolled _t audit tables
-- ============================================================
-- Run after V1_3. Safe to run on fresh installations because
-- V1_3 created stubs for any missing _t tables, so these
-- DROP TABLE IF EXISTS statements are always no-ops or drops.
-- ============================================================

DROP TABLE IF EXISTS xinco_add_attribute_t;
DROP TABLE IF EXISTS xinco_core_ace_t;
DROP TABLE IF EXISTS xinco_core_data_has_dependency_t;
DROP TABLE IF EXISTS xinco_core_data_t;
DROP TABLE IF EXISTS xinco_core_data_type_attribute_t;
DROP TABLE IF EXISTS xinco_core_data_type_t;
DROP TABLE IF EXISTS xinco_core_group_t;
DROP TABLE IF EXISTS xinco_core_language_t;
DROP TABLE IF EXISTS xinco_core_node_t;
DROP TABLE IF EXISTS xinco_core_user_has_xinco_core_group_t;
DROP TABLE IF EXISTS xinco_core_user_t;
DROP TABLE IF EXISTS xinco_dependency_behavior_t;
DROP TABLE IF EXISTS xinco_dependency_type_t;
DROP TABLE IF EXISTS xinco_setting_t;
DROP TABLE IF EXISTS xinco_core_user_modified_record;
