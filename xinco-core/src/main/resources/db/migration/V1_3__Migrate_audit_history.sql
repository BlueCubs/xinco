-- ============================================================
-- V1_3: Migrate legacy hand-rolled _t audit tables → Envers
-- ============================================================
-- On existing production databases: run this script once before
-- the application upgrade. The *_AUD and xinco_revisioninfo
-- tables must already exist (Hibernate hbm2ddl.auto creates them
-- at startup before updateDatabase() executes seed scripts).
--
-- On fresh installations (no _t tables): the CREATE TABLE IF NOT
-- EXISTS stubs produce empty tables, making every INSERT a no-op.
-- V1_4 then drops those empty stubs.
-- ============================================================

-- === Stubs: create _t tables if absent (no-op on existing DBs) ===

CREATE TABLE IF NOT EXISTS xinco_core_user_modified_record (
    id INT NOT NULL,
    record_id INT NOT NULL,
    mod_Time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mod_Reason VARCHAR(255),
    PRIMARY KEY (id, record_id)
);

CREATE TABLE IF NOT EXISTS xinco_add_attribute_t (
    record_id INT NOT NULL PRIMARY KEY,
    xinco_core_data_id INT NOT NULL DEFAULT 0,
    attribute_id INT NOT NULL DEFAULT 0,
    attrib_int INT NOT NULL DEFAULT 0,
    attrib_unsignedint BIGINT NOT NULL DEFAULT 0,
    attrib_double DOUBLE NOT NULL DEFAULT 0,
    attrib_varchar VARCHAR(255),
    attrib_text TEXT,
    attrib_datetime DATETIME
);

CREATE TABLE IF NOT EXISTS xinco_core_ace_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    xinco_core_user_id INT,
    xinco_core_group_id INT,
    xinco_core_node_id INT,
    xinco_core_data_id INT,
    read_permission TINYINT(1) NOT NULL DEFAULT 0,
    write_permission TINYINT(1) NOT NULL DEFAULT 0,
    execute_permission TINYINT(1) NOT NULL DEFAULT 0,
    admin_permission TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_data_has_dependency_t (
    record_id INT NOT NULL PRIMARY KEY,
    xinco_core_data_parent_id INT NOT NULL DEFAULT 0,
    xinco_core_data_children_id INT NOT NULL DEFAULT 0,
    dependency_type_id INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_data_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    xinco_core_node_id INT NOT NULL DEFAULT 0,
    xinco_core_language_id INT NOT NULL DEFAULT 0,
    xinco_core_data_type_id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    status_number INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_data_type_attribute_t (
    record_id INT NOT NULL PRIMARY KEY,
    xinco_core_data_type_id INT NOT NULL DEFAULT 0,
    attribute_id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    data_type VARCHAR(255),
    attr_size INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_data_type_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS xinco_core_group_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    status_number INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_language_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    sign VARCHAR(255),
    designation VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS xinco_core_node_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    xinco_core_node_id INT NOT NULL DEFAULT 0,
    xinco_core_language_id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    status_number INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_user_has_xinco_core_group_t (
    record_id INT NOT NULL PRIMARY KEY,
    xinco_core_user_id INT NOT NULL DEFAULT 0,
    xinco_core_group_id INT NOT NULL DEFAULT 0,
    status_number INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS xinco_core_user_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    username VARCHAR(255),
    userpassword VARCHAR(255),
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    email VARCHAR(255),
    status_number INT NOT NULL DEFAULT 0,
    attempts INT NOT NULL DEFAULT 0,
    last_modified DATETIME
);

CREATE TABLE IF NOT EXISTS xinco_dependency_behavior_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS xinco_dependency_type_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    xinco_dependency_behavior_id INT NOT NULL DEFAULT 0,
    designation VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS xinco_setting_t (
    record_id INT NOT NULL PRIMARY KEY,
    id INT NOT NULL DEFAULT 0,
    description VARCHAR(255),
    int_value INT,
    string_value VARCHAR(255),
    bool_value TINYINT(1),
    long_value BIGINT NOT NULL DEFAULT 0
);

-- === Step 1: Build record_id → REVINFO mapping ===
-- All _t tables share a global record_id sequence (from xinco_id),
-- so UNION across all 14 tables produces unique record_ids.

CREATE TEMPORARY TABLE _legacy_rev_mapping (
    seq_id INT AUTO_INCREMENT PRIMARY KEY,
    record_id INT NOT NULL UNIQUE,
    modifier_id INT NOT NULL DEFAULT 1,
    mod_time DATETIME NOT NULL,
    mod_reason VARCHAR(255)
);

INSERT INTO _legacy_rev_mapping (record_id, modifier_id, mod_time, mod_reason)
SELECT all_t.record_id,
       COALESCE(mr.id, 1),
       COALESCE(mr.mod_Time, NOW()),
       mr.mod_Reason
FROM (
    SELECT record_id FROM xinco_add_attribute_t
    UNION SELECT record_id FROM xinco_core_ace_t
    UNION SELECT record_id FROM xinco_core_data_has_dependency_t
    UNION SELECT record_id FROM xinco_core_data_t
    UNION SELECT record_id FROM xinco_core_data_type_attribute_t
    UNION SELECT record_id FROM xinco_core_data_type_t
    UNION SELECT record_id FROM xinco_core_group_t
    UNION SELECT record_id FROM xinco_core_language_t
    UNION SELECT record_id FROM xinco_core_node_t
    UNION SELECT record_id FROM xinco_core_user_has_xinco_core_group_t
    UNION SELECT record_id FROM xinco_core_user_t
    UNION SELECT record_id FROM xinco_dependency_behavior_t
    UNION SELECT record_id FROM xinco_dependency_type_t
    UNION SELECT record_id FROM xinco_setting_t
) all_t
LEFT JOIN xinco_core_user_modified_record mr ON mr.record_id = all_t.record_id
ORDER BY all_t.record_id;

-- === Step 2: Reserve a contiguous block of REVINFO ids ===
-- @rev_base is the current max id; new ids = @rev_base + seq_id (1..N).
SET @rev_base = (SELECT IFNULL(MAX(id), 0) FROM xinco_revisioninfo);

-- === Step 3: Insert REVINFO rows with explicit ids ===
INSERT INTO xinco_revisioninfo (id, timestamp, modifier_id, mod_reason)
SELECT @rev_base + seq_id,
       UNIX_TIMESTAMP(mod_time) * 1000,
       modifier_id,
       IFNULL(mod_reason, 'migrated from legacy audit tables')
FROM _legacy_rev_mapping;

-- === Step 4: Insert *_AUD rows (REVTYPE=0 = initial snapshot) ===

INSERT INTO xinco_core_data_AUD
    (REV, REVTYPE, id, designation, status_number,
     xinco_core_data_type_id, xinco_core_language_id, xinco_core_node_id)
SELECT @rev_base + lrm.seq_id, 0,
    t.id, t.designation, t.status_number,
    t.xinco_core_data_type_id, t.xinco_core_language_id,
    NULLIF(t.xinco_core_node_id, 0)
FROM xinco_core_data_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_user_AUD
    (REV, REVTYPE, id, username, userpassword, last_name, first_name,
     email, status_number, attempts, last_modified)
SELECT @rev_base + lrm.seq_id, 0,
    t.id, t.username, t.userpassword, t.last_name, t.first_name,
    t.email, t.status_number, t.attempts, t.last_modified
FROM xinco_core_user_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_node_AUD
    (REV, REVTYPE, id, designation, status_number,
     xinco_core_language_id, xinco_core_node_id)
SELECT @rev_base + lrm.seq_id, 0,
    t.id, t.designation, t.status_number,
    t.xinco_core_language_id, NULLIF(t.xinco_core_node_id, 0)
FROM xinco_core_node_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_group_AUD (REV, REVTYPE, id, designation, status_number)
SELECT @rev_base + lrm.seq_id, 0, t.id, t.designation, t.status_number
FROM xinco_core_group_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_ace_AUD
    (REV, REVTYPE, id, read_permission, write_permission,
     execute_permission, admin_permission,
     xinco_core_user_id, xinco_core_group_id,
     xinco_core_node_id, xinco_core_data_id)
SELECT @rev_base + lrm.seq_id, 0,
    t.id, t.read_permission, t.write_permission,
    t.execute_permission, t.admin_permission,
    NULLIF(t.xinco_core_user_id, 0),  NULLIF(t.xinco_core_group_id, 0),
    NULLIF(t.xinco_core_node_id, 0),  NULLIF(t.xinco_core_data_id, 0)
FROM xinco_core_ace_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_language_AUD (REV, REVTYPE, id, sign, designation)
SELECT @rev_base + lrm.seq_id, 0, t.id, t.sign, t.designation
FROM xinco_core_language_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_data_type_AUD (REV, REVTYPE, id, designation, description)
SELECT @rev_base + lrm.seq_id, 0, t.id, t.designation, t.description
FROM xinco_core_data_type_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_data_type_attribute_AUD
    (REV, REVTYPE, xinco_core_data_type_id, attribute_id,
     designation, data_type, attr_size)
SELECT @rev_base + lrm.seq_id, 0,
    t.xinco_core_data_type_id, t.attribute_id,
    t.designation, t.data_type, t.attr_size
FROM xinco_core_data_type_attribute_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_add_attribute_AUD
    (REV, REVTYPE, xinco_core_data_id, attribute_id,
     attrib_int, attrib_unsignedint, attrib_double,
     attrib_varchar, attrib_text, attrib_datetime)
SELECT @rev_base + lrm.seq_id, 0,
    t.xinco_core_data_id, t.attribute_id,
    t.attrib_int, t.attrib_unsignedint, t.attrib_double,
    t.attrib_varchar, t.attrib_text, t.attrib_datetime
FROM xinco_add_attribute_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_data_has_dependency_AUD
    (REV, REVTYPE, xinco_core_data_parent_id, xinco_core_data_children_id,
     dependency_type_id)
SELECT @rev_base + lrm.seq_id, 0,
    t.xinco_core_data_parent_id, t.xinco_core_data_children_id,
    t.dependency_type_id
FROM xinco_core_data_has_dependency_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_core_user_has_xinco_core_group_AUD
    (REV, REVTYPE, xinco_core_user_id, xinco_core_group_id, status_number)
SELECT @rev_base + lrm.seq_id, 0,
    t.xinco_core_user_id, t.xinco_core_group_id, t.status_number
FROM xinco_core_user_has_xinco_core_group_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_dependency_behavior_AUD
    (REV, REVTYPE, id, designation, description)
SELECT @rev_base + lrm.seq_id, 0, t.id, t.designation, t.description
FROM xinco_dependency_behavior_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_dependency_type_AUD
    (REV, REVTYPE, id, designation, description, xinco_dependency_behavior_id)
SELECT @rev_base + lrm.seq_id, 0,
    t.id, t.designation, t.description, NULLIF(t.xinco_dependency_behavior_id, 0)
FROM xinco_dependency_type_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

INSERT INTO xinco_setting_AUD
    (REV, REVTYPE, id, description, int_value, string_value, bool_value, long_value)
SELECT @rev_base + lrm.seq_id, 0,
    t.id, t.description, t.int_value, t.string_value, t.bool_value, t.long_value
FROM xinco_setting_t t
JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id;

DROP TEMPORARY TABLE IF EXISTS _legacy_rev_mapping;
