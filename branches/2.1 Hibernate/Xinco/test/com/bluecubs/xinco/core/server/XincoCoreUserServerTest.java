package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreUser;
import com.bluecubs.xinco.tools.MD5;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author ortijaus
 */
public class XincoCoreUserServerTest extends TestCase {

    HashMap parameters = new HashMap();

    public XincoCoreUserServerTest(String testName) {
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
     * Test of getXincoCoreUsers method, of class XincoCoreUserServer.
     */
    public void testGetXincoCoreUsers() {
        try {
            System.out.println("getXincoCoreUsers");
            Vector result = XincoCoreUserServer.getXincoCoreUsers();
            assertTrue(result.size() > 0);
        } catch (Exception e) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail(e.getLocalizedMessage());
        }
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer.
     */
    public void testIsPasswordUsable() {
        try {
            System.out.println("isPasswordUsable");
            XincoCoreUserServer instance = new XincoCoreUserServer(0, "test", MD5.encrypt("test"), "test",
                    "test", "test", 1, 0, new Timestamp(System.currentTimeMillis()));
            instance.write2DB();
            //Rejects empty passwords
            assertTrue(!instance.isPasswordUsable(""));
            //Rejects current password
            assertTrue(!instance.isPasswordUsable("test"));
            //Change password
            instance.setUserpassword(MD5.encrypt("test2"));
            instance.setChange(true);
            instance.write2DB();
            //Rejects password used recently
            assertTrue(!instance.isPasswordUsable("test"));
            //Rejects password used recently
            assertTrue(instance.isPasswordUsable("test3"));
            //Blame the admin
            instance.setChangerID(1);
            instance.deleteFromDB();
        } catch (Exception e) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail(e.getLocalizedMessage());
        }
    }

    /**
     * Test of getXincoCoreGroups method, of class XincoCoreUserServer.
     */
    public void testGetXincoCoreGroups() {
        try {
            System.out.println("getXincoCoreGroups");
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            Vector result = instance.getXincoCoreGroups();
            assertTrue(result.size() > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findById method, of class XincoCoreUserServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("id", 1);
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, (int) ((XincoCoreUser) result).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreUserServer.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            parameters.clear();
            parameters.put("username", "admin");
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
            assertEquals(instance.getId(), ((XincoCoreUser) result[0]).getId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreUserServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreUserServer.
     */
    public void testGetNewID() {
        try {
            System.out.println("getNewID");
            XincoCoreUserServer instance = new XincoCoreUserServer(1);
            int result = instance.getNewID(true);
            System.out.println("New id: " + result);
            assertTrue(result > 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreUserServer.
     */
    public void testWrite2DBAndDelete() {
        try {
            System.out.println("write2DB");
            XincoCoreUserServer instance = new XincoCoreUserServer(0, "test", "test", "test",
                    "test", "test", 1, 0, new Timestamp(System.currentTimeMillis()));
            System.out.println("Instance id before writing: " + instance.getId());
            assertTrue(instance.write2DB());
            System.out.println("Instance id after writing: " + instance.getId());
            assertTrue(instance.getId() > 0);
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreUserServer.
     */
    public void testDeleteFromDB() {
        try {
            System.out.println("deleteFromDB");
            XincoCoreUserServer instance = new XincoCoreUserServer(0, "test", "test", "test",
                    "test", "test", 1, 0, new Timestamp(System.currentTimeMillis()));
            instance.write2DB();
            assertTrue(instance.deleteFromDB());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
