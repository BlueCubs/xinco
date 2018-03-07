package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoCoreDataTypeAttributeServer.getXincoCoreDataTypeAttributes;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttribute;
import com.bluecubs.xinco.core.server.persistence.XincoCoreDataTypeAttributePK;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataTypeAttributeJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.exceptions.NonexistentEntityException;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultron  javier.ortiz.78@gmail.com
 */
public class XincoCoreDataTypeAttributeServerTest extends AbstractXincoDataBaseTestCase {

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
            XincoCoreDataTypeAttribute findXincoCoreDataTypeAttribute = new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory()).findXincoCoreDataTypeAttribute(new XincoCoreDataTypeAttributePK(1, 20));
            if (findXincoCoreDataTypeAttribute != null) {
                new XincoCoreDataTypeAttributeJpaController(getEntityManagerFactory()).destroy(findXincoCoreDataTypeAttribute.getXincoCoreDataTypeAttributePK());
            }
            XincoCoreDataTypeAttributeServer instance = new XincoCoreDataTypeAttributeServer(1, 20, "Test", "Test", 1);
            int result = instance.write2DB();
            assertTrue(result > 0);
            assertTrue(deleteFromDB(instance, 1) == 0);
        } catch (XincoException ex) {
            getLogger(XincoCoreDataTypeAttributeServerTest.class.getSimpleName()).log(SEVERE, null, ex);
            fail();
        } catch (NonexistentEntityException ex) {
            getLogger(XincoCoreDataTypeAttributeServerTest.class.getName()).log(SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getXincoCoreDataTypeAttributes method, of class
     * XincoCoreDataTypeAttributeServer.
     */
    public void testGetXincoCoreDataTypeAttributes() {
        assertTrue(getXincoCoreDataTypeAttributes(1).size() > 0);
    }
}
