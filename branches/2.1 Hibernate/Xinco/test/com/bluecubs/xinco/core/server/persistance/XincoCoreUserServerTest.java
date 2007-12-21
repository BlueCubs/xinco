/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluecubs.xinco.core.server.persistance;

import com.bluecubs.xinco.core.XincoException;
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
            System.err.println("Test setLastModified");
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
            System.err.println("Test getLastModified");
            XincoCoreUserServer instance = new XincoCoreUserServer("admin", "admin");
            Date result = instance.getLastModified();
            assertTrue(result != null);
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
        System.err.println("Test getXincoCoreUsers");
        Vector result = XincoCoreUserServer.getXincoCoreUsers();
        assertTrue(result != null);
    }

    /**
     * Test of isHashPassword method, of class XincoCoreUserServer.
     */
    @Test
    public void isHashPassword() {
        try {
            System.err.println("Test isHashPassword");
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
            System.err.println("Test setHashPassword");
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
            System.err.println("Test isIncreaseAttempts");
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
            System.err.println("Test setIncreaseAttempts");
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
            System.err.println("Test isPasswordUsable");
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
}
