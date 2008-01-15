/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.server.XincoPersistanceManager;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.persistence.audit.XincoCoreUserT;
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
 * @author Javier A. Ortiz
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
     * Test of getXincoCoreUsers method, of class XincoCoreUserServer.
     */
    @Test
    public void getXincoCoreUsers() {
        System.err.println("Test getXincoCoreUsers");
        Vector result = XincoCoreUserServer.getXincoCoreUsers();
        assertTrue(result != null);
    }

    /**
     * Test of isPasswordUsable method, of class XincoCoreUserServer.
     */
    @Test
    public void isPasswordUsable() {
        try {
            System.err.println("Test isPasswordUsable");
            String newPass = "abcd";
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            assertTrue(instance.isPasswordUsable(newPass));
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
            System.err.println("Test isPasswordUsable (vs. audit trail)");
            String newPass = "abcd";
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            instance.setTransactionTime(DateRange.startingNow());
            int id = Integer.parseInt(pm.executeQuery("select count(t.recordId) from XincoCoreUserT t").get(0).toString()) + 1;
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
            assertFalse(result);
            pm.delete(t, true);
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test of write2DB and deleteFromDB method, of class XincoCoreUserServer.
     * @throws java.lang.Exception 
     */
    @Test
    public void writeAndDeleteFromDB() throws Exception {
        try {
            System.err.println("Test deleteFromDB");
            XincoCoreUserServer instance = new XincoCoreUserServer(0, "test", "test",
                    "test", "test", "test", 1, 1, DateRange.startingNow().getStart().getTime());
            instance.setTransactionTime(DateRange.startingNow());
            instance.setChangerID(1);
            instance.setCreated(true);
            assertTrue(instance.write2DB());
            assertTrue(instance.deleteFromDB());
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }
}
