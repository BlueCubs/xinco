/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.XincoSetting;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAbstractAuditableObject;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoSettingServerTest {

    private XincoPersistanceManager pm = new XincoPersistanceManager();

    public XincoSettingServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getXinco_settings method, of class XincoSettingServer.
     */
    @Test
    public void getXinco_settings() {
        System.err.println("getXinco_settings");
        XincoSettingServer instance = new XincoSettingServer(1);
        Vector result = instance.getXinco_settings();
        while (result.size() > 0) {
            if (((XincoSetting) result.get(0)) == null) {
                fail("Got empty setting from server.");
            }
            result.remove(0);
        }
        assertTrue(true);
    }

    /**
     * Test of setXinco_settings method, of class XincoSettingServer.
     */
    @Test
    public void setXinco_settings() {
        System.err.println("setXinco_settings");
        XincoSettingServer instance = new XincoSettingServer();
        Vector xinco_settings = instance.getXinco_settings();
        instance.setXinco_settings(xinco_settings);
        boolean equal = xinco_settings == instance.getXinco_settings() && xinco_settings != null;
        assertTrue(equal);
    }

    /**
     * Test of getChangerID method, of class XincoSettingServer.
     */
    @Test
    public void getChangerID() {
        System.err.println("getChangerID");
        XincoSettingServer instance = new XincoSettingServer(1);
        instance.setChangerID(1);
        int expResult = 1;
        int result = instance.getChangerID();
        assertEquals(expResult, result);
    }

    /**
     * Test of setChangerID method, of class XincoSettingServer.
     */
    @Test
    public void setChangerID() {
        try {
            System.err.println("setChangerID");
            int changerID = 1;
            XincoSettingServer instance = new XincoSettingServer(1);
            instance.setChangerID(changerID);
            instance.write2DB();
            assertEquals(changerID, instance.getChangerID());
        } catch (XincoException ex) {
            Logger.getLogger(XincoSettingServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getSetting method, of class XincoSettingServer.
     */
    @Test
    public void getSetting() {
        System.err.println("getSetting");
        int i = 1;
        XincoSettingServer instance = new XincoSettingServer(i);
        XincoSetting result = instance.getSetting(i);
        assertTrue(result != null);
    }
    
    /**
     * Test of creating a XincoSettingServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void createSetting() {
        try {
            System.err.println("creating Setting");
            XincoSettingServer instance = new XincoSettingServer();
            instance.setDescription("Temporary setting");
            instance.write2DB();
            HashMap temp = new HashMap();
            temp.put("description", instance.getDescription());
            XincoSetting result = (XincoSetting)pm.namedQuery("XincoSetting.findByDescription", temp).get(0);
            assertTrue(instance.getDescription().equals(result.getDescription()));
            deleteSetting(result.getId());
        } catch (XincoException ex) {
            Logger.getLogger(XincoSettingServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
    
    /**
     * Test of deleting a XincoSettingServer. 
     * @param i 
     */
    public void deleteSetting(int i) {
        try {
            System.err.println("Deleting Setting: "+i);
            XincoSettingServer instance = new XincoSettingServer(i);
            instance.deleteFromDB();
            XincoSettingServer result = new XincoSettingServer(i);
            assertTrue(result.getReason()==null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoSettingServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
