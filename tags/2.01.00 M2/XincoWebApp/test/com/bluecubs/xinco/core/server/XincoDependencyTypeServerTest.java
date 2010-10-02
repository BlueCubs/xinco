package com.bluecubs.xinco.core.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoDependencyTypeServerTest extends XincoTestCase {

    public XincoDependencyTypeServerTest(String testName) {
        super(testName);
    }

    /**
     * Test of write2DB and deleteFromDB methods, of class XincoDependencyTypeServer.
     */
    @Test
    public void testWriteandDelete() {
        try {
            System.out.println("writeAndDelete");
            XincoDependencyTypeServer instance = new XincoDependencyTypeServer(0,1, "test");
            assertTrue(instance.write2DB() > 0);
            assertEquals(XincoDependencyTypeServer.deleteFromDB(instance.getId()), 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoDependencyTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
