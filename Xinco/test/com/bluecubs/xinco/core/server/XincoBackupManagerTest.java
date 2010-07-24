package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoBackupManagerTest extends XincoTestCase {

    private HashMap<String, Integer> stats = new HashMap<String, Integer>(),
            afterStats = new HashMap<String, Integer>();

    public XincoBackupManagerTest(String testName) {
        super(testName);
    }

    /**
     * Test of restoreFromBackup method, of class XincoBackupManager.
     */
    public void testRestoreFromBackup() {
        ArrayList<XincoCoreLogServer> settings = new ArrayList<XincoCoreLogServer>();
        try {
            System.out.println( "Backup");
            assertFalse(XincoDBManager.isLocked());
            //Gather Stats
            for (EntityType et : XincoDBManager.getEntityManagerFactory().getMetamodel().getEntities()) {
                stats.put(et.getName(), XincoDBManager.namedQuery(et.getName() + ".findAll").size());
            }
            assertTrue(XincoBackupManager.backup());
            assertFalse(XincoDBManager.isLocked());
            System.out.println( "RestoreFromBackup");
            //Get info from a table to compare later
            settings.addAll(XincoCoreLogServer.getXincoCoreLogs(1));
            //Delete logs in the DB so they are recreated from backup
            for (XincoCoreLogServer x : settings) {
                System.out.println("Deleting Log " + x.getId());
                new XincoCoreLogJpaController().destroy(x.getId());
                System.out.println( "Done!");
            }
            assertFalse(XincoDBManager.isLocked());
            System.out.println( "Restoring from backup... ");
            assertTrue(XincoBackupManager.restoreFromBackup(XincoBackupManager.getBackupFiles().get(XincoBackupManager.getBackupFiles().size() - 1)));
            assertFalse(XincoDBManager.isLocked());
            //Gather Stats
            for (EntityType et : XincoDBManager.getEntityManagerFactory().getMetamodel().getEntities()) {
                afterStats.put(et.getName(), XincoDBManager.namedQuery(et.getName() + ".findAll").size());
            }
            //Compare stats
            assertTrue(stats.size() == afterStats.size());
            for (Entry e : stats.entrySet()) {
                System.out.println("Comparing stats for table: " + e.getKey());
                System.out.println("Before: "+e.getKey() + ", Then: " + e.getValue() + " Now: " + afterStats.get((String) e.getKey()));
                assertTrue(e.getValue() == afterStats.get((String) e.getKey()));
            }
        } catch (Exception e) {
            Logger.getLogger(XincoBackupManagerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of getScriptellaProperties method, of class XincoBackupManager.
     */
    @Test
    public void testGetScriptellaProperties() {
        try {
            System.out.println( "getScriptellaProperties");
            EntityManagerFactory source = Persistence.createEntityManagerFactory("XincoPU");
            EntityManagerFactory dest = Persistence.createEntityManagerFactory("XincoBackup");
            //Start the embeded DB
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            HashMap result = XincoBackupManager.getScriptellaProperties(source, dest);
            assertTrue(result.size() == 6);
            assertTrue(result.get("url1").equals(source.getProperties().get("javax.persistence.jdbc.url").toString().replaceAll(";create=true", "")));
            assertTrue(result.get("user1").equals(source.getProperties().get("javax.persistence.jdbc.user")));
            assertTrue(result.get("pass1").equals(source.getProperties().get("javax.persistence.jdbc.password")));
            assertTrue(result.get("url2").equals(dest.getProperties().get("javax.persistence.jdbc.url").toString().replaceAll(";create=true", "")));
            assertTrue(result.get("user2").equals(dest.getProperties().get("javax.persistence.jdbc.user")));
            assertTrue(result.get("pass2").equals(dest.getProperties().get("javax.persistence.jdbc.password")));
            try {
                //Stop Derby database in order to delete
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException e) {
                //When the database shuts down it'll throw an exception
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(XincoBackupManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(XincoBackupManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(XincoBackupManagerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
//    @Test
//    public void testGetProcessingOrder() {
//        System.out.println("getProcessingOrder");
//        EntityManagerFactory source = XincoDBManager.getEntityManagerFactory();
//        ArrayList<String> result = XincoBackupManager.getProcessingOrder(source.createEntityManager());
//        for (String s : result) {
//            System.out.println(s);
//        }
//        System.out.println("Expected size: " + source.getMetamodel().getEntities().size() + ". Actual size: " + result.size());
//        assertTrue(result.size() == source.getMetamodel().getEntities().size());
//    }
}
