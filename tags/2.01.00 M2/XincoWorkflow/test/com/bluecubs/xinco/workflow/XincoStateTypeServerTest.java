package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoStateType;
import com.bluecubs.xinco.workflow.persistence.controller.XincoStateTypeJpaController;
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoStateTypeServerTest {

    public XincoStateTypeServerTest() {
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
     * Test of write2DB method, of class XincoStateTypeServer.
     */
    @Test
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoStateTypeServer instance = new XincoStateTypeServer("Test");
            XincoStateType xst = new XincoStateTypeJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoStateType(instance.write2DB());
            assertTrue(xst != null);
            assertTrue(XincoStateTypeServer.removeFromDB(xst) == 0);
            assertTrue(new XincoStateTypeJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoStateType(xst.getId()) == null);
        } catch (XincoWorkflowException ex) {
            Logger.getLogger(XincoStateTypeServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
