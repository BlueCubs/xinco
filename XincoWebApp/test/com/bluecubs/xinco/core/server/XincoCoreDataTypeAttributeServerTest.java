package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataTypeAttributeServerTest extends XincoTestCase {

    public XincoCoreDataTypeAttributeServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreDataTypeAttributeServerTest.class);
        return suite;
    }

    /**
     * Test of write2DB method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreDataTypeAttribute findXincoCoreDataTypeAttribute = new XincoCoreDataTypeAttributeJpaController(XincoDBManager.getEntityManagerFactory()).findXincoCoreDataTypeAttribute(new XincoCoreDataTypeAttributePK(1, 20));
            if (findXincoCoreDataTypeAttribute != null) {
                new XincoCoreDataTypeAttributeJpaController(XincoDBManager.getEntityManagerFactory()).destroy(findXincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK());
            }
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1, 20, "Test", "Test", 1);
            int result = instance.write2DB();
            assertTrue(result > 0);
            assertTrue(XincoCoreDataTypeAttributeServer.deleteFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getSimpleName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreDataTypeAttributes method, of class XincoCoreDataTypeAttributeServer.
     */
    public void testGetXincoCoreDataTypeAttributes() {
        System.out.println("getXincoCoreDataTypeAttributes");
        assertTrue(XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes(1).size() > 0);
    }
}
