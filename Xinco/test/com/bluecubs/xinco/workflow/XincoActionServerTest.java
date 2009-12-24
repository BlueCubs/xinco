package com.bluecubs.xinco.workflow;

import com.bluecubs.xinco.workflow.db.XincoStateTransitionsToXincoState;
import java.util.Vector;
import com.bluecubs.xinco.core.server.XincoException;
import com.bluecubs.xinco.workflow.db.XincoAction;
import com.bluecubs.xinco.workflow.db.controller.XincoActionJpaController;
import com.bluecubs.xinco.workflow.db.controller.exceptions.IllegalOrphanException;
import com.bluecubs.xinco.workflow.db.controller.exceptions.NonexistentEntityException;
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
    public void testWrite2DB(){
        try {
            System.out.println("write2DB");
            XincoActionServer instance = new XincoActionServer(0, "Test");
            XincoAction action = new XincoActionJpaController().findXincoAction(instance.write2DB());
            assertTrue(action != null);
            new XincoActionJpaController().destroy(instance.getId());
            assertTrue(new XincoActionJpaController().findXincoAction(instance.getId()) == null);
            try{
                action.setXincoStateTransitionsToXincoStateList(
                        new Vector<XincoStateTransitionsToXincoState>());
                action.getXincoStateTransitionsToXincoStateList().add(new XincoStateTransitionsToXincoState());
                XincoActionServer.removeFromDB(action);
                fail();
            }catch(XincoException e){
               action.setXincoStateTransitionsToXincoStateList(null);
            }
            assertTrue(XincoActionServer.removeFromDB(action) == 0);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(XincoActionServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(XincoActionServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail();
        }
    }
}
