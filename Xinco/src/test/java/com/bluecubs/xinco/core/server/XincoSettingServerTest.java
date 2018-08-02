package com.bluecubs.xinco.core.server;

import static com.bluecubs.xinco.core.server.XincoSettingServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSetting;
import static com.bluecubs.xinco.core.server.XincoSettingServer.getSettings;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

/**
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
public class XincoSettingServerTest extends AbstractXincoDataBaseTestCase {

    public XincoSettingServerTest(String testName) {
        super(testName);
    }

    /**
     * Test of write2DB method, of class XincoSettingServer.
     *
     * @throws Exception
     */
    public void testWrite2DB() throws Exception {
        try {
            XincoSettingServer instance = new XincoSettingServer(0, "Test", 0,
                    "", false, 0);
            assertTrue(instance.write2DB() > 0);
            String designation = instance.getDescription();
            instance.setDescription(designation + "2");
            assertTrue(instance.write2DB() > 0);
            instance = new XincoSettingServer(instance.getId());
            assertEquals(designation + "2", instance.getDescription());
            //Clean up
            deleteFromDB(instance);
        } catch (Exception ex) {
            getLogger(XincoSettingServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getSetting method, of class XincoSettingServer.
     *
     * @throws Exception
     */
    public void testGetSetting() throws Exception {
        String desc = "password.aging";
        assertTrue(getSetting(new XincoCoreUserServer(1), desc) != null);
    }

    /**
     * Test of getSettings method, of class XincoSettingServer.
     *
     * @throws Exception
     */
    public void testGetSettings() throws Exception {
        assertTrue(getSettings(new XincoCoreUserServer(1)).size() > 0);
    }
}
