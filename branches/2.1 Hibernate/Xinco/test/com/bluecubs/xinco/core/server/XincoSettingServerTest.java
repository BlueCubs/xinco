package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistence.XincoSetting;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier
 */
public class XincoSettingServerTest extends TestCase {

    public XincoSettingServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoSettingServerTest.class);
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
     * Test of getSetting method, of class XincoSettingServer.
     */
    public void testGetSetting() {
        System.out.println("getSetting");
        String desc = "version.high";
        XincoSetting result = XincoSettingServer.getSetting(desc);
        assertTrue(result.getIntValue() > 0);
    }

    /**
     * Test of findById method, of class XincoSettingServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        System.out.println("findById");
        HashMap parameters = new HashMap();
        parameters.put("id", 1);
        XincoSettingServer instance = new XincoSettingServer(1);
        XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
        assertEquals("version.high", ((XincoSetting) result).getDescription());
    }

    /**
     * Test of findWithDetails method, of class XincoSettingServer.
     * @throws Exception 
     */
    public void testFindWithDetails() throws Exception {
        System.out.println("findWithDetails");
        XincoSettingServer instance = new XincoSettingServer(1);
        XincoSetting[] expResult = {instance};
        XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(instance.getParameters());
        assertEquals(expResult[0].getDescription(), ((XincoSetting) result[0]).getDescription());
    }

    /**
     * Test of getParameters method, of class XincoSettingServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoSettingServer instance = new XincoSettingServer(1);
            HashMap expResult = new HashMap();
            expResult.put("id", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoSettingServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoSettingServer.
     * @throws XincoException
     */
    public void testGetNewID() throws XincoException {
        System.out.println("getNewID");
        XincoSettingServer instance = new XincoSettingServer(1);
        int result = instance.getNewID(true);
        System.out.println("New id: " + result);
        assertTrue(result > 0);
    }

    /**
     * Test of write2DB method, of class XincoSettingServer.
     * @throws XincoException
     */
    public void testWrite2DB() throws XincoException {
        System.out.println("write2DB");
        XincoSettingServer instance = new XincoSettingServer(0, "test", true, 0, 0, "");
        System.out.println("Instance id before writing: " + instance.getId());
        assertTrue(instance.write2DB());
        System.out.println("Instance id after writing: " + instance.getId());
        assertTrue(instance.getId() > 0);
    }

    /**
     * Test of deleteFromDB method, of class XincoSettingServer.
     * @throws XincoException
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "static-access"})
    public void testDeleteFromDB() throws XincoException, Exception {
        System.out.println("deleteFromDB");
        XincoSettingServer instance = new XincoSettingServer(1);
        HashMap parameters = new HashMap();
        parameters.put("description", "test");
        XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
        XincoSettingServer value = new XincoSettingServer(((XincoSetting) result[0]).getDescription());
        //Blame the admin :P
        value.setChangerID(1);
        assertTrue(value.deleteFromDB());
    }
}
