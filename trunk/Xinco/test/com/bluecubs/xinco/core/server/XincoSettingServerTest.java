package com.bluecubs.xinco.core.server;

import java.util.Vector;
import junit.framework.TestCase;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
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
     * Test of write2DB method, of class XincoSettingServer.
     * @throws Exception
     */
    public void testWrite2DB() throws Exception {
        try {
            System.out.println("write2DB");
            XincoSettingServer instance = new XincoSettingServer(0, "Test", 0,
                    "", false, 0, 0, new Vector());
            assertTrue(instance.write2DB() > 0);
            String designation = instance.getDescription();
            instance.setDescription(designation + "2");
            assertTrue(instance.write2DB() > 0);
            instance = new XincoSettingServer(instance.getId());
            assertEquals(designation + "2", instance.getDescription());
            //Clean up
            XincoSettingServer.deleteFromDB(instance);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getSetting method, of class XincoSettingServer.
     * @throws Exception
     */
    public void testGetSetting() throws Exception {
        System.out.println("getSetting");
        String desc = "password.aging";
        assertTrue(XincoSettingServer.getSetting(desc) != null);
    }

    /**
     * Test of getSettings method, of class XincoSettingServer.
     * @throws Exception 
     */
    public void testGetSettings() throws Exception {
        System.out.println("getSettings");
        assertTrue(XincoSettingServer.getSettings().size() > 0);
    }
}
