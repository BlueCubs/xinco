package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.bluecubs.xinco.core.server.persistence.XincoCoreData;
import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.envers.AuditReaderFactory;

/**
 * Verifies V1_3 migration logic: legacy _t table data is correctly imported into Envers *_AUD
 * tables and is queryable via AuditReader.
 *
 * <p>Uses H2-compatible SQL to simulate the MySQL V1_3 migration script in the test environment.
 * The @rev_base user-variable pattern is not portable, so the revision base offset is computed in
 * Java and injected into the SQL strings.
 */
public class AuditHistoryMigrationTest extends AbstractXincoDataBaseTestCase {

  public AuditHistoryMigrationTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(AuditHistoryMigrationTest.class);
  }

  public void testMigratedDataIsQueryableViaAuditReader() throws Exception {
    JdbcDataSource ds = new JdbcDataSource();
    ds.setPassword("xinco");
    ds.setUser("root");
    ds.setURL("jdbc:h2:file:./target/data/xinco-test;AUTO_SERVER=TRUE");

    try (Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement()) {

      // --- Create legacy tables (stubs equivalent to V1_3 CREATE TABLE IF NOT EXISTS) ---
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS xinco_core_user_modified_record ("
              + "id INT NOT NULL, record_id INT NOT NULL, "
              + "mod_Time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
              + "mod_Reason VARCHAR(255), PRIMARY KEY (id, record_id))");
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS xinco_core_data_t ("
              + "record_id INT NOT NULL PRIMARY KEY, id INT NOT NULL DEFAULT 0, "
              + "xinco_core_node_id INT NOT NULL DEFAULT 0, "
              + "xinco_core_language_id INT NOT NULL DEFAULT 0, "
              + "xinco_core_data_type_id INT NOT NULL DEFAULT 0, "
              + "designation VARCHAR(255), status_number INT NOT NULL DEFAULT 0)");

      // --- Seed legacy data ---
      // record_id=999 links a xinco_core_user_modified_record to xinco_core_data_t
      // Use entity IDs that exist after DB seeding (node=1, language=1, datatype=1)
      stmt.executeUpdate(
          "INSERT INTO xinco_core_user_modified_record (id, record_id, mod_Time, mod_Reason)"
              + " VALUES (1, 999, CURRENT_TIMESTAMP, 'legacy reason')");
      stmt.executeUpdate(
          "INSERT INTO xinco_core_data_t (record_id, id, xinco_core_node_id,"
              + " xinco_core_language_id, xinco_core_data_type_id, designation, status_number)"
              + " VALUES (999, 9001, 1, 1, 1, 'MigratedData', 1)");

      // --- Step 1: build mapping temp table ---
      stmt.executeUpdate("DROP TABLE IF EXISTS _legacy_rev_mapping");
      stmt.executeUpdate(
          "CREATE TEMPORARY TABLE _legacy_rev_mapping ("
              + "seq_id INT AUTO_INCREMENT PRIMARY KEY, "
              + "record_id INT NOT NULL UNIQUE, "
              + "modifier_id INT NOT NULL DEFAULT 1, "
              + "mod_time TIMESTAMP NOT NULL, "
              + "mod_reason VARCHAR(255))");
      stmt.executeUpdate(
          "INSERT INTO _legacy_rev_mapping (record_id, modifier_id, mod_time, mod_reason)"
              + " SELECT all_t.record_id,"
              + "        COALESCE(mr.id, 1),"
              + "        COALESCE(mr.mod_Time, CURRENT_TIMESTAMP),"
              + "        mr.mod_Reason"
              + " FROM (SELECT record_id FROM xinco_core_data_t) all_t"
              + " LEFT JOIN xinco_core_user_modified_record mr"
              + "   ON mr.record_id = all_t.record_id"
              + " ORDER BY all_t.record_id");

      // --- Step 2: compute rev base ---
      int revBase;
      try (ResultSet rs = stmt.executeQuery("SELECT IFNULL(MAX(id), 0) FROM xinco_revisioninfo")) {
        rs.next();
        revBase = rs.getInt(1);
      }

      // --- Step 3: insert xinco_revisioninfo rows (H2: use TIMESTAMPDIFF for epoch ms) ---
      stmt.executeUpdate(
          "INSERT INTO xinco_revisioninfo (id, timestamp, modifier_id, mod_reason)"
              + " SELECT "
              + revBase
              + " + seq_id,"
              + "        TIMESTAMPDIFF(MILLISECOND, TIMESTAMP '1970-01-01 00:00:00', mod_time),"
              + "        modifier_id,"
              + "        IFNULL(mod_reason, 'migrated from legacy audit tables')"
              + " FROM _legacy_rev_mapping");

      // --- Step 4: insert xinco_core_data_AUD ---
      stmt.executeUpdate(
          "INSERT INTO xinco_core_data_AUD"
              + " (REV, REVTYPE, id, designation, status_number,"
              + "  xinco_core_data_type_id, xinco_core_language_id, xinco_core_node_id)"
              + " SELECT "
              + revBase
              + " + lrm.seq_id, 0,"
              + "        t.id, t.designation, t.status_number,"
              + "        t.xinco_core_data_type_id, t.xinco_core_language_id,"
              + "        CASE WHEN t.xinco_core_node_id = 0 THEN NULL"
              + "             ELSE t.xinco_core_node_id END"
              + " FROM xinco_core_data_t t"
              + " JOIN _legacy_rev_mapping lrm ON lrm.record_id = t.record_id");

      // --- Step 5: drop temp table ---
      stmt.executeUpdate("DROP TABLE IF EXISTS _legacy_rev_mapping");
    }

    // --- Verify via AuditReader ---
    try (EntityManager em = getEntityManagerFactory().createEntityManager()) {
      org.hibernate.envers.AuditReader reader = AuditReaderFactory.get(em);
      List<Number> revisions = reader.getRevisions(XincoCoreData.class, 9001);
      assertNotNull("revisions list should not be null", revisions);
      assertFalse("at least one revision should exist for migrated data", revisions.isEmpty());
    }
  }

  /** Verifies that V1_3 is a no-op when the legacy _t tables are empty (fresh installation). */
  public void testNoOpOnFreshInstallation() throws Exception {
    JdbcDataSource ds = new JdbcDataSource();
    ds.setPassword("xinco");
    ds.setUser("root");
    ds.setURL("jdbc:h2:file:./target/data/xinco-test;AUTO_SERVER=TRUE");

    int revCountBefore;
    try (Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement()) {
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM xinco_revisioninfo");
      rs.next();
      revCountBefore = rs.getInt(1);

      // Create empty stubs (simulating fresh install where _t tables don't exist)
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS xinco_core_data_t ("
              + "record_id INT NOT NULL PRIMARY KEY, id INT NOT NULL DEFAULT 0, "
              + "xinco_core_node_id INT NOT NULL DEFAULT 0, "
              + "xinco_core_language_id INT NOT NULL DEFAULT 0, "
              + "xinco_core_data_type_id INT NOT NULL DEFAULT 0, "
              + "designation VARCHAR(255), status_number INT NOT NULL DEFAULT 0)");
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS xinco_core_user_modified_record ("
              + "id INT NOT NULL, record_id INT NOT NULL, "
              + "mod_Time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
              + "mod_Reason VARCHAR(255), PRIMARY KEY (id, record_id))");

      // Run migration steps (tables are empty → nothing to insert)
      stmt.executeUpdate("DROP TABLE IF EXISTS _legacy_rev_mapping");
      stmt.executeUpdate(
          "CREATE TEMPORARY TABLE _legacy_rev_mapping ("
              + "seq_id INT AUTO_INCREMENT PRIMARY KEY, "
              + "record_id INT NOT NULL UNIQUE, "
              + "modifier_id INT NOT NULL DEFAULT 1, "
              + "mod_time TIMESTAMP NOT NULL, "
              + "mod_reason VARCHAR(255))");
      stmt.executeUpdate(
          "INSERT INTO _legacy_rev_mapping (record_id, modifier_id, mod_time, mod_reason)"
              + " SELECT all_t.record_id, COALESCE(mr.id, 1),"
              + "        COALESCE(mr.mod_Time, CURRENT_TIMESTAMP), mr.mod_Reason"
              + " FROM (SELECT record_id FROM xinco_core_data_t) all_t"
              + " LEFT JOIN xinco_core_user_modified_record mr"
              + "   ON mr.record_id = all_t.record_id");

      int revBase;
      try (ResultSet rsBase =
          stmt.executeQuery("SELECT IFNULL(MAX(id), 0) FROM xinco_revisioninfo")) {
        rsBase.next();
        revBase = rsBase.getInt(1);
      }
      stmt.executeUpdate(
          "INSERT INTO xinco_revisioninfo (id, timestamp, modifier_id, mod_reason)"
              + " SELECT "
              + revBase
              + " + seq_id,"
              + "        TIMESTAMPDIFF(MILLISECOND, TIMESTAMP '1970-01-01 00:00:00', mod_time),"
              + "        modifier_id, IFNULL(mod_reason, 'migrated')"
              + " FROM _legacy_rev_mapping");
      stmt.executeUpdate("DROP TABLE IF EXISTS _legacy_rev_mapping");

      // Drop stubs (V1_4 equivalent)
      stmt.executeUpdate("DROP TABLE IF EXISTS xinco_core_data_t");
      stmt.executeUpdate("DROP TABLE IF EXISTS xinco_core_user_modified_record");

      // Verify REVINFO count is unchanged
      ResultSet rsAfter = stmt.executeQuery("SELECT COUNT(*) FROM xinco_revisioninfo");
      rsAfter.next();
      int revCountAfter = rsAfter.getInt(1);
      assertEquals(
          "REVINFO count should be unchanged on fresh install", revCountBefore, revCountAfter);
    }
  }
}
