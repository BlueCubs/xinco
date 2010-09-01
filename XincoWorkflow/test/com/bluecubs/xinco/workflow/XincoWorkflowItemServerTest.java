package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoWorkflowItem;
import com.bluecubs.xinco.workflow.persistence.controller.XincoWorkflowItemJpaController;
import java.util.Date;
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
public class XincoWorkflowItemServerTest {

    public XincoWorkflowItemServerTest() {
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
     * Test of write2DB method, of class XincoWorkflowItemServer.
     */
    @Test
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            XincoWorkflowItemServer instance = new XincoWorkflowItemServer(new Date());
            XincoWorkflowItem xwi = new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowItem(instance.write2DB());
            assertTrue(xwi != null);
            assertTrue(XincoWorkflowItemServer.removeFromDB(xwi) == 0);
            assertTrue(new XincoWorkflowItemJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflowItem(xwi.getId()) == null);
        } catch (XincoWorkflowException ex) {
            Logger.getLogger(XincoWorkflowItemServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
