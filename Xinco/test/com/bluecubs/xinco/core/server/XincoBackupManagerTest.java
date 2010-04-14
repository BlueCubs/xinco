package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreLogJpaController;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;
import junit.framework.TestCase;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoBackupManagerTest extends TestCase {

    private HashMap<String, Integer> stats= new HashMap<String, Integer>(),
            afterStats=new HashMap<String, Integer>();

    public XincoBackupManagerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of restoreFromBackup method, of class XincoBackupManager.
     * @throws Exception
     */
    public void testRestoreFromBackup() throws Exception {
        Vector<XincoCoreLogServer> settings=null;
        try {
            System.out.println("backup");
            assertFalse(XincoDBManager.isLocked());
            assertTrue(XincoBackupManager.backup());
            for (Entry<String, Integer> e : XincoBackupManager.stats.entrySet()) {
                System.out.println(e.getKey() + ": " + e.getValue());
                stats.put(e.getKey(), e.getValue());
            }
            assertFalse(XincoDBManager.isLocked());
            System.out.println("restoreFromBackup");
            //Get info from a table to compare later
            settings = XincoCoreLogServer.getXincoCoreLogs(1);
            //Delete logs in the DB so they are recreated from backup
            for (XincoCoreLogServer x : settings) {
                System.out.println("Deleting Log " + x.getId());
                new XincoCoreLogJpaController().destroy(x.getId());
                System.out.println("Done!");
            }
            assertFalse(XincoDBManager.isLocked());
            assertTrue(XincoBackupManager.restoreFromBackup(XincoBackupManager.getBackupFiles().lastElement()));
            afterStats = XincoBackupManager.stats;
            assertFalse(XincoDBManager.isLocked());
            //Compare stats
            assertTrue(stats.size() == afterStats.size());
            for (Entry e : stats.entrySet()) {
                System.out.println("Comparing stats for table: " + e.getKey());
                System.out.println("Before: "+e.getKey() + ", Now: " + e.getValue() + "Then: " + afterStats.get((String) e.getKey()));
                assertTrue(e.getValue() == afterStats.get((String) e.getKey()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Restore whatever was deleted
            for (XincoCoreLogServer x : settings) {
                System.out.println("Restoring Log " + x.getId());
                x.write2DB();
                System.out.println("Done!");
            }
            fail();
        }
    }
}
