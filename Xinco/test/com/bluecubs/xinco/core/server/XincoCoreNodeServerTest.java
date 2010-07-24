package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreNodeJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeServerTest extends XincoTestCase {

    public XincoCoreNodeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreNodeServerTest.class);
        return suite;
    }

    /**
     * Test of getXincoCoreNodeParents method, of class XincoCoreNodeServer.
     */
    public void testGetXincoCoreNodeParents() {
        try {
            System.out.println("getXincoCoreNodeParents");
            assertTrue(XincoCoreNodeServer.getXincoCoreNodeParents(1).size() == 1);
            assertTrue(XincoCoreNodeServer.getXincoCoreNodeParents(2).size() == 2);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of fillXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreNodes() {
        try {
            System.out.println("fillXincoCoreNodes");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            assertTrue((instance.getXinco_core_nodes()).size() > 0);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of fillXincoCoreData method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreData() {
        try {
            System.out.println("fillXincoCoreData");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreData();
            assertTrue(instance.getXinco_core_data().size() > 0);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of findXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFindXincoCoreNodes() {
        try {
            System.out.println("findXincoCoreNodes");
            assertTrue(XincoCoreNodeServer.findXincoCoreNodes("xincoRoot", 1).size() > 0);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }
    
    /**
     * Test of write2DB method, of class XincoCoreNodeServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            assertTrue(instance.write2DB() > 0);
            String designation = instance.getDesignation();
            instance.setDesignation(designation.replaceAll(":", "") + ":");
            assertTrue(instance.write2DB() > 0);
            assertEquals(designation.replaceAll(":", "") + ":", instance.getDesignation());
            //Clean up
            instance.setDesignation(designation.replaceAll(":", ""));
            instance.write2DB();
        } catch (Exception e) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreNodeServer.
     */
    public void testDeleteFromDB() {
        try {
            System.out.println("deleteFromDB");
            int userID = 1;
            XincoCoreNodeServer instance = new XincoCoreNodeServer(0, 1, 1, "Test", 1);
            instance.setChangerID(1);
            int id = instance.write2DB();
            assertTrue(id > 0);
            instance.deleteFromDB(true, userID);
            assertTrue(new XincoCoreNodeJpaController().findXincoCoreNode(id) == null);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(Level.SEVERE, null, e);
            fail();
        }
    }
}
