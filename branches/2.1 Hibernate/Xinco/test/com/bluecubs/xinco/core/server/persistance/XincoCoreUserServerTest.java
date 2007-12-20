/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.persistance.audit.XincoCoreUserT;
import com.bluecubs.xinco.core.server.persistance.audit.XincoAuditingDAOHelper;
import java.sql.Timestamp;
import java.util.Date;
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
public class XincoCoreUserServerTest {

    private XincoPersistanceManager pm = new XincoPersistanceManager();

    public XincoCoreUserServerTest() {
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
     * Test of setLastModified method, of class XincoCoreUserServer.
     */
    @Test
    public void setLastModified() {
        try {
            System.err.println("setLastModified");
            Timestamp lastModified = new Timestamp(System.currentTimeMillis());
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setLastModified(lastModified);
            assertEquals(lastModified, instance.getLastModified());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getLastModified method, of class XincoCoreUserServer.
     */
    @Test
    public void getLastModified() {
        try {
            System.err.println("getLastModified");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            Date result = instance.getLastModified();
            assertTrue(result == null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXincoCoreUsers method, of class XincoCoreUserServer.
     */
    @Test
    public void getXincoCoreUsers() {
        System.err.println("getXincoCoreUsers");
        Vector result = XincoCoreUserServer.getXincoCoreUsers();
        assertTrue(result != null);
    }

    /**
     * Test of isHashPassword method, of class XincoCoreUserServer.
     */
    @Test
    public void isHashPassword() {
        try {
            System.err.println("isHashPassword");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            XincoAuditingDAOHelper.create(instance, instance);
            boolean result = instance.isHashPassword();
            assertEquals(true, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setHashPassword method, of class XincoCoreUserServer.
     */
    @Test
    public void setHashPassword() {
        try {
            System.err.println("setHashPassword");
            boolean hashPassword = false;
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setHashPassword(hashPassword);
            assertFalse(instance.isHashPassword());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isIncreaseAttempts method, of class XincoCoreUserServer.
     */
    @Test
    public void isIncreaseAttempts() {
        try {
            System.err.println("isIncreaseAttempts");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            boolean result = instance.isIncreaseAttempts();
            assertEquals(false, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setIncreaseAttempts method, of class XincoCoreUserServer.
     */
    @Test
    public void setIncreaseAttempts() {
        try {
            System.err.println("setIncreaseAttempts");
            boolean increaseAttempts = false;
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setIncreaseAttempts(increaseAttempts);
            assertFalse(instance.isIncreaseAttempts());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer.
     */
    @Test
    public void isPasswordUsable() {
        try {
            System.err.println("isPasswordUsable");
            String newPass = "abcd";
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            boolean result = instance.isPasswordUsable(newPass);
            assertTrue(result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer, with an old password in the audit trail.
     */
    @Test
    public void isPasswordUsableUsed() {
        try {
            System.err.println("isPasswordUsable (vs. audit trail)");
            String newPass = "abcd";
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            int id = Integer.parseInt(pm.executeQuery("select max(recordid) from XincoCoreUserT").get(0).toString()) +1;
            XincoCoreUserT t = new XincoCoreUserT(id);
            t.setUsername("admin");
            t.setUserpassword("abcd");
            pm.persist(t, false, true);
            boolean result = instance.isPasswordUsable(newPass);
            assertFalse(result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of getXinco_core_groups method, of class XincoCoreUserServer.
     */
    @Test
    public void getXinco_core_groups() {
        try {
            System.err.println("getXinco_core_groups");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            Vector result = instance.getXinco_core_groups();
            assertTrue(result != null);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setXinco_core_groups method, of class XincoCoreUserServer.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void setXinco_core_groups() {
        try {
            System.err.println("setXinco_core_groups");
            Vector xinco_core_groups = new Vector();
            xinco_core_groups.add(1);
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setXinco_core_groups(xinco_core_groups);
            assertEquals(instance.getXinco_core_groups(), xinco_core_groups);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isChange method, of class XincoCoreUserServer.
     */
    @Test
    public void isChange() {
        try {
            System.err.println("isChange");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            boolean expResult = false;
            boolean result = instance.isChange();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setChange method, of class XincoCoreUserServer.
     */
    @Test
    public void setChange() {
        try {
            System.err.println("setChange");
            boolean change = true;
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setChange(change);
            assertTrue(instance.isChange());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of isWriteGroups method, of class XincoCoreUserServer.
     */
    @Test
    public void isWriteGroups() {
        try {
            System.err.println("isWriteGroups");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            boolean expResult = false;
            boolean result = instance.isWriteGroups();
            assertEquals(expResult, result);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of setWriteGroups method, of class XincoCoreUserServer.
     */
    @Test
    public void setWriteGroups() {
        try {
            System.err.println("setWriteGroups");
            boolean writegroups = true;
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            instance.setWriteGroups(writegroups);
            assertTrue(instance.isWriteGroups());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreUserServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void deleteFromDB() throws Exception {
        System.err.println("deleteFromDB");
        int id = Integer.parseInt(pm.executeQuery("select max(recordid) from XincoCoreUser").get(0).toString()) +1;
        XincoCoreUserServer instance = new XincoCoreUserServer(id,"test","test",
                "test","test","test",1,1,DateRange.startingNow().getStart().getTime());
        instance.setTransactionTime(DateRange.startingNow());
        
        boolean expResult = false;
        boolean result = instance.deleteFromDB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
