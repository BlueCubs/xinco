package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoAction;
import com.bluecubs.xinco.workflow.persistence.XincoParameter;
import com.bluecubs.xinco.workflow.persistence.XincoParameterPK;
import com.bluecubs.xinco.workflow.persistence.controller.XincoActionJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.XincoParameterJpaController;
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
public class XincoParameterServerTest {

    private static final long serialVersionUID = 1L;

    public XincoParameterServerTest() {
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
     * Test of write2DB method, of class XincoParameterServer.
     */
    @Test
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            //Create an actio for this test
            XincoActionServer xas = new XincoActionServer("Test");
            XincoAction action = new XincoActionJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoAction(xas.write2DB());
            XincoParameterServer instance = new XincoParameterServer(
                    new XincoParameterPK(action.getId()), "Test", "test type", "test value");
            XincoParameter xp = new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoParameter(instance.write2DB());
            assertTrue(xp != null);
            assertTrue(XincoParameterServer.removeFromDB(xp) == 0);
            assertTrue(new XincoParameterJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoParameter(xp.getXincoParameterPK()) == null);
            //Delete action
            XincoActionServer.removeFromDB(action);
        } catch (XincoWorkflowException ex) {
            try {
                //Make sure the database is clear with any test data.
                HashMap parameters = new HashMap();
                parameters.put("name", "Test");
                if (XincoWorkflowDBManager.namedQuery("XincoAction.findByName", parameters).isEmpty()) {
                    fail();
                } else {
                    XincoActionServer.removeFromDB((XincoAction) XincoWorkflowDBManager.namedQuery("XincoAction.findByName", parameters).get(0));
                    fail("Traces of test data found and removed. Please retry this test.");
                }
            } catch (XincoWorkflowException ex1) {
                Logger.getLogger(XincoParameterServerTest.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(XincoParameterServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
