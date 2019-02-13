package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.XincoStateTransitionsToXincoState;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowPK;
import com.bluecubs.xinco.workflow.persistence.controller.XincoStateTransitionsToXincoStateJpaController;
import com.bluecubs.xinco.workflow.persistence.controller.XincoWorkflowJpaController;
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
public class XincoTransitionServerTest {

    private XincoWorkflowStateServer xwss1, xwss2;
    private XincoStateTypeServer xsts;
    private XincoUserLinkServer xuls;
    private XincoWorkflowServer xws;

    public XincoTransitionServerTest() {
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
            //Create test data
            createTestData();
            //Now finally to the test
            XincoTransitionServer instance = new XincoTransitionServer(xwss1, xwss2);
            XincoStateTransitionsToXincoState transition = new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoStateTransitionsToXincoState(instance.write2DB());
            assertTrue(transition != null);
            assertTrue(XincoTransitionServer.removeFromDB(transition) == 0);
            assertTrue(new XincoStateTransitionsToXincoStateJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoStateTransitionsToXincoState(transition.getXincoStateTransitionsToXincoStatePK()) == null);
            //Now delete everything we created
            destroyTestData();
        } catch (Error ex) {
            Logger.getLogger(XincoTransitionServerTest.class.getName()).log(Level.SEVERE, null, ex);
            destroyTestData();
            fail("Partial test data was already in the system and is now cleared. Please retry this test.");
        }
    }

    private void createTestData() throws XincoWorkflowException {
        System.out.println("Creating test data...");
        //Need an user
        xuls = new XincoUserLinkServer(0, "test");
        //Need a workflow
        xws = new XincoWorkflowServer(0, 1, xuls.write2DB(), "Test");
        XincoWorkflowPK pk = xws.write2DB();
        //Need state type
        xsts= new XincoStateTypeServer();
        xsts.setDescription("Test");
        xsts.setName("Test");
        xsts.write2DB();
        //Need 2 states for the transition
        xwss1 = new XincoWorkflowStateServer(pk.getId(), pk.getVersion(), "Step 1",
                new XincoWorkflowJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflow(xws.getXincoWorkflowPK()));
        xwss1.setXincoStateTypeId(xsts);
        xwss1.write2DB();
        xwss2 = new XincoWorkflowStateServer(pk.getId(), pk.getVersion(), "Step 2",
                new XincoWorkflowJpaController(XincoWorkflowDBManager.getEntityManagerFactory()).findXincoWorkflow(xws.getXincoWorkflowPK()));
        xwss2.setXincoStateTypeId(xsts);
        xwss2.write2DB();
        System.out.println("Done!");
    }

    private void destroyTestData() {
        System.out.println("Deleting test data...");
        try {
            XincoWorkflowStateServer.removeFromDB(xwss1);
        } catch (XincoWorkflowException ex) {
        }
        try {
            XincoWorkflowStateServer.removeFromDB(xwss1);
        } catch (XincoWorkflowException ex) {
        }
        try {
            XincoWorkflowServer.removeFromDB(xws);
        } catch (XincoWorkflowException ex) {
        }
        try {
            XincoUserLinkServer.removeFromDB(xuls);
        } catch (XincoWorkflowException ex) {
        }
        System.out.println("Done!");
    }
}
