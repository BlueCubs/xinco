package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreACE;
import java.sql.Timestamp;
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
public class XincoCoreACEServerTest extends TestCase {
private int tempId;
    public XincoCoreACEServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreACEServerTest.class);
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
     * Test of getXincoCoreACL method, of class XincoCoreACEServer.
     */
    public void testGetXincoCoreACL() {
        System.out.println("getXincoCoreACL");
        int attrID = 1;
        String attrT = "xinco_core_user";
        Vector result = XincoCoreACEServer.getXincoCoreACL(attrID, attrT);
        assertTrue(result.size() > 0);
    }

    /**
     * Test of checkAccess method, of class XincoCoreACEServer.
     */
    public void testCheckAccess() {
        System.out.println("checkAccess");
        boolean createdUser = false;
        //login as admin
        XincoCoreUserServer user = null;
        XincoCoreNodeServer node = null;
        XincoCoreACEServer acl = null;
        try {
            user = new XincoCoreUserServer("admin", "admin");
        } catch (XincoException ex) {
            try {
                //Create a new user and assign access to Trash
                user = new XincoCoreUserServer(0, "admin2", "admin", "", "", "",
                        1, 0, new Timestamp(System.currentTimeMillis()));
                user.write2DB();
                acl = new XincoCoreACEServer(0, user.getId(), 1, 2, -1, true, true, true, true);
                acl.write2DB();
                createdUser = true;
            } catch (XincoException ex1) {
                Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex1);
                fail();
            }
        }
        try {
            //Check Trash folder for access
            node = new XincoCoreNodeServer(2);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
        XincoCoreACE ace = XincoCoreACEServer.checkAccess(user, node.getXincoCoreACL());
        assertTrue(ace != null);
        if (createdUser) {
            //Undo created records
            acl.deleteFromDB();
            user.deleteFromDB();
        }
    }

    /**
     * Test of setUserId method, of class XincoCoreACEServer.
     * @throws XincoException
     */
    public void testSetUserId() throws XincoException {
        System.out.println("setUserId");
        int i = 0;
        XincoCoreACEServer instance = new XincoCoreACEServer(1);
        instance.setUserId(i);
        assertTrue(instance.getId() == 0);
    }

    /**
     * Test of findById method, of class XincoCoreACEServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = new HashMap();
        parameters.put("id", 1);
        XincoCoreACEServer instance = new XincoCoreACEServer(1);
        XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
        assertEquals(1, (int) ((XincoCoreACE) result).getId());
    }

    /**
     * Test of findWithDetails method, of class XincoCoreACEServer.
     * @throws Exception
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        XincoCoreACEServer instance = new XincoCoreACEServer(1);
        XincoCoreACE[] expResult = {instance};
        XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(instance.getParameters());
        assertEquals(expResult[0].getId(), ((XincoCoreACE) result[0]).getId());
    }

    /**
     * Test of getParameters method, of class XincoCoreACEServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreACEServer instance = new XincoCoreACEServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreACEServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreACEServer instance = new XincoCoreACEServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreACEServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreACEServer instance = new XincoCoreACEServer(0, 1, 1, 2, -1, true, true, true, true);
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            tempId=instance.getId();
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreACEServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testDeleteFromDB() throws Exception {
        try {
            System.out.println("deleteFromDB");
            XincoCoreACEServer instance = new XincoCoreACEServer(1);
            HashMap parameters = new HashMap();
            parameters.put("id", tempId);
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            XincoCoreACEServer value = new XincoCoreACEServer(((XincoCoreACE) result[0]).getId());
            //Blame the admin :P
            value.setChangerID(1);
            assertTrue(value.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreACEServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
