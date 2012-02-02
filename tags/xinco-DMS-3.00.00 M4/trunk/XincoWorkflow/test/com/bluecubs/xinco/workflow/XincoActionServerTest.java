package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoAction;
import com.bluecubs.xinco.workflow.persistence.controller.XincoActionJpaController;
import java.util.HashMap;
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
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoActionServerTest {

    public XincoActionServerTest() {
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
     * Test of write2DB method, of class XincoActionServer.
     */
    @Test
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            //Make sure the database is clear with any test data.
            HashMap parameters = new HashMap();
            parameters.put("name", "Test");
            if (!XincoWorkflowDBManager.namedQuery("XincoAction.findByName", parameters).isEmpty()) {
                XincoActionServer.removeFromDB((XincoAction) XincoWorkflowDBManager.namedQuery("XincoAction.findByName", parameters).get(0));
                //Traces of test data found and removed.
            }
            XincoActionServer instance = new XincoActionServer("Test");
            XincoAction action = new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(instance.write2DB());
            assertTrue(action != null);
            assertTrue(XincoActionServer.removeFromDB(action) == 0);
            assertTrue(new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(action.getId()) == null);
        } catch (XincoWorkflowException ex) {
            try {
                //Make sure the database is clear with any test data.
                HashMap parameters = new HashMap();
                parameters.put("name", "Test");
                if (!XincoWorkflowDBManager.namedQuery("XincoAction.findByName", parameters).isEmpty()) {
                    XincoActionServer.removeFromDB((XincoAction) XincoWorkflowDBManager.namedQuery("XincoAction.findByName", parameters).get(0));
                    fail("Traces of test data found and removed. Please retry this test.");
                } else {
                    fail();
                }
            } catch (XincoWorkflowException ex1) {
                Logger.getLogger(XincoActionServerTest.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
