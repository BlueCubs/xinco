package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistence.XincoSetting;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Javier
 */
public class XincoSettingServerTest extends TestCase {

    public XincoSettingServerTest(String testName) {
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
        XincoAbstractAuditableObject[] expResult = {instance};
        XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(instance.getParameters());
        assertEquals(expResult, result);
    }

    /**
     * Test of getParameters method, of class XincoSettingServer.
     */
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
     */
    public void testGetNewID() throws XincoException {
        System.out.println("getNewID");
        XincoSettingServer instance = new XincoSettingServer(1);
        int expResult = 0;
        int result = instance.getNewID();
        assertTrue(expResult < result);
    }

    /**
     * Test of write2DB method, of class XincoSettingServer.
     */
    public void testWrite2DB() throws XincoException {
        System.out.println("write2DB");
        XincoSettingServer instance = new XincoSettingServer("test");
        assertTrue(instance.write2DB());
    }

    /**
     * Test of deleteFromDB method, of class XincoSettingServer.
     */
    public void testDeleteFromDB() throws XincoException, Exception {
        System.out.println("deleteFromDB");
        XincoSettingServer instance = new XincoSettingServer(1);
        HashMap parameters = new HashMap();
        parameters.put("description", "test");
        XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(parameters);
        assertTrue(((XincoSettingServer) result[0]).deleteFromDB());
    }
}
