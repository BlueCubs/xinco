package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoCoreNodeServer.findXincoCoreNodes;
import static com.bluecubs.xinco.core.server.XincoCoreNodeServer.getXincoCoreNodeParents;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreNodeJpaController;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public class XincoCoreNodeServerTest extends AbstractXincoDataBaseTestCase {

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
            assertTrue(getXincoCoreNodeParents(1).size() == 1);
            assertTrue(getXincoCoreNodeParents(2).size() == 2);
        } catch (Exception e) {
            getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of fillXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreNodes() {
        try {
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            assertTrue((instance.getXincoCoreNodes()).size() > 0);
        } catch (Exception e) {
            getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of fillXincoCoreData method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreData() {
        try {
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreData();
            assertTrue(instance.getXincoCoreData().size() > 0);
        } catch (Exception e) {
            getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of findXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFindXincoCoreNodes() {
        try {
            assertTrue(findXincoCoreNodes("xincoRoot", 1).size() > 0);
        } catch (Exception e) {
            getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreNodeServer.
     */
    public void testWrite2DB() {
        try {
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
            getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(SEVERE, null, e);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreNodeServer.
     */
    public void testDeleteFromDB() {
        try {
            int userID = 1;
            XincoCoreNodeServer instance = new XincoCoreNodeServer(0, 1, 1, "Test", 1);
            instance.setChangerID(1);
            int id = instance.write2DB();
            assertTrue(id > 0);
            instance.deleteFromDB(true, userID);
            assertTrue(new XincoCoreNodeJpaController(getEntityManagerFactory()).findXincoCoreNode(id) == null);
        } catch (Exception e) {
            getLogger(XincoCoreNodeServerTest.class.getSimpleName()).log(SEVERE, null, e);
            fail();
        }
    }
}
