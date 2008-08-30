package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreNode;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreNodeServerTest extends TestCase {

    HashMap parameters = new HashMap();

    public XincoCoreNodeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreNodeServerTest.class);
        return suite;
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
     * Test of fillXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFillXincoCoreNodes() {
        try {
            System.out.println("fillXincoCoreNodes");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            instance.fillXincoCoreNodes();
            assertTrue(instance.getXincoCoreNodes().size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
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
            assertTrue(instance.getXincoCoreData().size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findXincoCoreNodes method, of class XincoCoreNodeServer.
     */
    public void testFindXincoCoreNodes() {
        try {
            System.out.println("findXincoCoreNodes");
            String attrS = "xincoRoot";
            int attrLID = 1;
            Vector result = XincoCoreNodeServer.findXincoCoreNodes(attrS, attrLID);
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreNodeParents method, of class XincoCoreNodeServer.
     */
    public void testGetXincoCoreNodeParents() {
        try {
            System.out.println("getXincoCoreNodeParents");
            int attrID = 2;
            Vector result = XincoCoreNodeServer.getXincoCoreNodeParents(attrID);
            assertTrue(result.size() > 0);
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreNodeServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoCoreNode) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreNodeServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            XincoCoreNode[] expResult = {instance};
            parameters.clear();
            parameters.put("designation", "xincoRoot");
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            assertEquals(expResult[0].getId(), ((XincoCoreNode) result[0]).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreNodeServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreNodeServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreNodeServer.
     */
    public void testWrite2DBAndDelete() {
        try {
            System.out.println("write2DB");
            XincoCoreNodeServer instance = new XincoCoreNodeServer(0, 1, 1, "test", 1);
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreNodeServer.
     * @throws Exception
     */
    public void testDeleteFromDB_boolean_int() throws Exception {
        try {
            System.out.println("deleteFromDB");
            boolean delete_this = false;
            int userID = 0;
            XincoCoreNodeServer instance = new XincoCoreNodeServer(0, 1, 1, "test", 1);
            instance.write2DB();
            instance.deleteFromDB(delete_this, userID);
            assertTrue(XincoCoreNodeServer.findXincoCoreNodes("test", 1).size() > 0);
            assertTrue(instance.deleteFromDB(true, userID));
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreNodeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
