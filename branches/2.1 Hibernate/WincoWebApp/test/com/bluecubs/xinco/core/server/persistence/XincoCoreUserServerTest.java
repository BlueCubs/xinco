/**
 *Copyright 2008 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            Test
 *
 * Description:     Test
 *
 * Original Author: Javier A. Ortiz
 * Date:            2008
 *
 * Modifications:
 *
 * Who?             When?             What?
 * 
 *************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.persistence.XincoCoreUser;
import com.bluecubs.xinco.persistence.audit.XincoCoreUserT;
import com.dreamer.Hibernate.PersistenceManager;
import java.util.HashMap;
import java.util.List;
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

    private PersistenceManager pm = new PersistenceManager();

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
     * Test of isPasswordUsable method (when password is in the audit trail), of class XincoCoreUserServer, with an old password in the audit trail.
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
     * Test the action of locking a user.
     * @throws java.lang.Exception 
     */
    @Test
    public void write2DB() throws Exception {
        try {
            System.err.println("Test write2DB");
            XincoCoreUserServer instance = new XincoCoreUserServer(0, "test", "test",
                    "test", "test", "test", 1, 1, DateRange.startingNow().getStart().getTime());
            assertTrue(instance.write2DB());
            assertTrue(lockUser(instance.getId()));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
        }
    }

    /**
     * Test the action of locking a user.
     * @param id
     * @return
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("unchecked")
    public boolean lockUser(int id) throws Exception {
        try {
            System.err.println("Test lock user");
            XincoCoreUserServer instance = new XincoCoreUserServer(id);
            instance.setStatusNumber(2);
            assertTrue(instance.write2DB());
            HashMap parameters = new HashMap();
            parameters.put("id", 20000);
            List result = pm.namedQuery("", parameters);
            assertTrue(result.size() > 0);
            assertTrue(((XincoCoreUser) result.get(0)).getStatusNumber() == 2);
            return (deleteFromDB(id));
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
            return false;
        }
    }

    /**
     * Test of deleteFromDB method, of class XincoCoreUserServer.
     * @param id 
     * @return 
     * @throws java.lang.Exception
     */
    public boolean deleteFromDB(int id) throws Exception {
        try {
            System.err.println("Test deleteFromDB");
            XincoCoreUserServer instance = new XincoCoreUserServer(20000);
            assertTrue(instance.deleteFromDB());
            return true;
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServerTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Something went wrong.");
            return false;
        }
    }
}
