/**
 *Copyright 2008 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            Test
 *
 * Description:     Test
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.persistence.XincoSetting;
import com.dreamer.Hibernate.PersistenceManager;
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

    private PersistenceManager pm = new PersistenceManager();

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
     * Test of getXincoSettings method, of class XincoSettingServer.
     */
    @Test
    public void getXinco_settings() {
        System.err.println("getXinco_settings");
        XincoSettingServer instance = new XincoSettingServer(1);
        Vector result = instance.getXincoSettings();
        while (result.size() > 0) {
            if (((XincoSetting) result.get(0)) == null) {
                fail("Got empty setting from server.");
            }
            result.remove(0);
        }
        assertTrue(true);
    }

    /**
     * Test of setXincoSettings method, of class XincoSettingServer.
     */
    @Test
    public void setXinco_settings() {
        System.err.println("setXinco_settings");
        XincoSettingServer instance = new XincoSettingServer();
        Vector xinco_settings = instance.getXincoSettings();
        instance.setXincoSettings(xinco_settings);
        boolean equal = xinco_settings == instance.getXincoSettings() && xinco_settings != null;
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
        } catch (Throwable ex) {
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
        XincoSettingServer instance = new XincoSettingServer(1);
        XincoSetting result = instance.getSetting(1);
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
            XincoSetting result = (XincoSetting) pm.namedQuery("XincoSetting.findByDescription", temp).get(0);
            assertTrue(instance.getDescription().equals(result.getDescription()));
            deleteSetting(result.getId());
        } catch (Throwable ex) {
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
            System.err.println("Deleting Setting: " + i);
            XincoSettingServer instance = new XincoSettingServer(i);
            instance.deleteFromDB();
            XincoSettingServer result = new XincoSettingServer(i);
            assertTrue(result.getReason() == null);
        } catch (Throwable ex) {
            Logger.getLogger(XincoSettingServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
