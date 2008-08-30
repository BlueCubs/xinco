package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.hibernate.audit.XincoAbstractAuditableObject;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroupPK;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreUserHasXincoCoreGroupServerTest extends TestCase {

    HashMap parameters = new HashMap();
    private XincoCoreUserHasXincoCoreGroupPK tempId;

    public XincoCoreUserHasXincoCoreGroupServerTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(XincoCoreUserHasXincoCoreGroupServerTest.class);
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
     * Test of findById method, of class XincoCoreUserHasXincoCoreGroupServer.
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public void testFindById() throws Exception {
        try {
            System.out.println("findById");
            parameters = new HashMap();
            parameters.put("xincoCoreGroupId", 1);
            parameters.put("xincoCoreUserId", 1);
            XincoCoreUserHasXincoCoreGroupServer instance = new XincoCoreUserHasXincoCoreGroupServer(1, 1);
            XincoAbstractAuditableObject result = (XincoAbstractAuditableObject) instance.findById(parameters);
            assertEquals(1, ((XincoCoreUserHasXincoCoreGroup) result).getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of findWithDetails method, of class XincoCoreUserHasXincoCoreGroupServer.
     * @throws Exception
     */
    public void testFindWithDetails() throws Exception {
        try {
            System.out.println("findWithDetails");
            XincoCoreUserHasXincoCoreGroupServer instance = new XincoCoreUserHasXincoCoreGroupServer(1, 1);
            XincoCoreUserHasXincoCoreGroup[] expResult = {instance};
            XincoAbstractAuditableObject[] result = (XincoAbstractAuditableObject[]) instance.findWithDetails(instance.getParameters());
            assertEquals(expResult[0].getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId(),
                    ((XincoCoreUserHasXincoCoreGroup) result[0]).getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getParameters method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    @SuppressWarnings("unchecked")
    public void testGetParameters() {
        try {
            System.out.println("getParameters");
            XincoCoreUserHasXincoCoreGroupServer instance = new XincoCoreUserHasXincoCoreGroupServer(1, 1);
            HashMap expResult = new HashMap();
            expResult.put("xincoCoreGroupId", 1);
            expResult.put("xincoCoreUserId", 1);
            HashMap result = instance.getParameters();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of getNewID method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testGetNewID() {
        System.out.println("getNewID");
        try {
            XincoCoreUserHasXincoCoreGroupServer instance = new XincoCoreUserHasXincoCoreGroupServer();
            assertTrue(instance.getNewID(true) == 0);
        } catch (UnsupportedOperationException e) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, e);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }

    /**
     * Test of write2DB method, of class XincoCoreUserHasXincoCoreGroupServer.
     */
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoCoreUserHasXincoCoreGroupServer instance = new XincoCoreUserHasXincoCoreGroupServer(1, 3, 1);
            assertTrue(instance.write2DB());
            assertTrue(instance.findWithDetails(instance.getParameters()).length>0);
            //Blame the admin
            instance.setChangerID(1);
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (Exception ex) {
            Logger.getLogger(XincoCoreUserHasXincoCoreGroupServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
