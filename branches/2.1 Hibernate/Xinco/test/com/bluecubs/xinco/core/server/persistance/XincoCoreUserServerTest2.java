/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreUserT;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author javydreamercsw
 */
public class XincoCoreUserServerTest2 {

    public XincoCoreUserServerTest2() {
    }
    private XincoPersistanceManager pm = new XincoPersistanceManager();

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
     * Test of getXincoCoreGroups method, of class XincoCoreUserServer.
     */
    @Test
    public void getXinco_core_groups() {
        try {
            System.err.println("Test getXinco_core_groups");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setChangerID(1);
            Vector result = instance.getXincoCoreGroups();
            assertTrue(result != null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setXincoCoreGroups method, of class XincoCoreUserServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void setXinco_core_groups() {
        try {
            System.err.println("Test setXinco_core_groups");
            Vector xinco_core_groups = new Vector();
            xinco_core_groups.add(1);
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setChangerID(1);
            instance.setXincoCoreGroups(xinco_core_groups);
            assertEquals(instance.getXincoCoreGroups(), xinco_core_groups);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isChange method, of class XincoCoreUserServer.
     */
    @Test
    public void isChange() {
        try {
            System.err.println("Test isChange");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            boolean expResult = false;
            boolean result = instance.isChange();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setChange method, of class XincoCoreUserServer.
     */
    @Test
    public void setChange() {
        try {
            System.err.println("Test setChange");
            boolean change = true;
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setChange(change);
            assertTrue(instance.isChange());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isWriteGroups method, of class XincoCoreUserServer.
     */
    @Test
    public void isWriteGroups() {
        try {
            System.err.println("Test isWriteGroups");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            boolean expResult = false;
            boolean result = instance.isWriteGroups();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setWriteGroups method, of class XincoCoreUserServer.
     */
    @Test
    public void setWriteGroups() {
        try {
            System.err.println("Test setWriteGroups");
            boolean writegroups = true;
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setWriteGroups(writegroups);
            assertTrue(instance.isWriteGroups());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreUserServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void deleteFromDB() throws Exception {
        try {
            System.err.println("Test deleteFromDB");
            XincoCoreUserServer instance = new XincoCoreUserServer(0, "test", "test",
                    "test", "test", "test", 1, 1, DateRange.startingNow().getStart().getTime());
            instance.setTransactionTime(DateRange.startingNow());
            instance.setChangerID(1);
            instance.setCreated(true);
            instance.write2DB();
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer, with an old password in the audit trail.
     */
    @Test
    public void isPasswordUsableUsed() {
        try {
            System.err.println("Test isPasswordUsable (vs. audit trail)");
            String newPass = "abcd";
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            int id = Integer.parseInt(pm.executeQuery("select max(t.recordId) from XincoCoreUserT t").get(0).toString()) + 1;
            XincoCoreUserT t = new XincoCoreUserT(id);
            t.setUsername(instance.getUsername());
            t.setUserpassword("abcd");
            t.setAttempts(0);
            t.setEmail("");
            t.setFirstname("");
            t.setId(1);
            t.setLastModified(DateRange.startingNow().getStart().getTime());
            t.setName("");
            t.setStatusNumber(1);
            pm.persist(t, false, true);
            boolean result = instance.isPasswordUsable(newPass);
            System.err.println("Result: "+result);
            assertFalse(result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest2.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
