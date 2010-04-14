/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.persistence.UserLink;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflow;
import com.bluecubs.xinco.workflow.persistence.XincoWorkflowPK;
import com.bluecubs.xinco.workflow.persistence.controller.UserLinkJpaController;
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
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowServerTest {

    private static final long serialVersionUID = 1L;

    public XincoWorkflowServerTest() {
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
     * Test of write2DB method, of class XincoWorkflowServer.
     */
    @Test
    public void testWrite2DB() {
        try {
            System.out.println("write2DB");
            //Create an user for this test
            XincoUserLinkServer xas = new XincoUserLinkServer(0,"test");
            UserLink user = new UserLinkJpaController().findUserLink(xas.write2DB());
            XincoWorkflowServer instance = new XincoWorkflowServer(new XincoWorkflowPK(1, user.getId()), "Test");
            XincoWorkflow xp = new XincoWorkflowJpaController().findXincoWorkflow(instance.write2DB());
            assertTrue(xp != null);
            assertTrue(XincoWorkflowServer.removeFromDB(xp) == 0);
            assertTrue(new XincoWorkflowJpaController().findXincoWorkflow(xp.getXincoWorkflowPK()) == null);
            //Delete user
            XincoUserLinkServer.removeFromDB(user);
        } catch (XincoWorkflowException ex) {
            Logger.getLogger(XincoWorkflowServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
