/**
 *Copyright 2004 blueCubs.com
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
 * Name:            XincoCoreUserServer
 *
 * Description:     user
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * Javier A. Ortiz 09/20/2006         Related changes made to XincoCoreUser (new fields) in the following methods:
 *                                    write2DB, XincoCoreUserServer, XincoCoreUserServer (x 2), getXincoCoreUsers
 * Javier A. Ortiz 11/06/2006         Moved the logic of locking an account due to login attempts from the XincoAdminServlet
 * Alexander Manes 11/12/2006         Moved the new user features to core class
 * Javier A. Ortiz 11/20/2006         Undo previous changes and corrected a bug that increased twice
 *                                    the attempts in the DB when wrong password was used
 *************************************************************
 */
package com.bluecubs.xinco.core.server.persistence;

import com.bluecubs.xinco.core.exception.XincoException;
import com.bluecubs.xinco.core.exception.XincoSettingException;
import com.bluecubs.xinco.persistence.XincoCoreGroup;
import com.bluecubs.xinco.persistence.XincoCoreUser;
import com.bluecubs.xinco.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.persistence.audit.XincoCoreUserT;
import com.bluecubs.xinco.tools.MD5;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditableDAO;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.oness.common.model.temporal.DateRange;

/**
 * Status list (in DB)
 * 1 = unlocked
 * 2 = locked
 * 3 = aged password
 * Temporary statuses
 * -1 = aged password modified, ready to turn unlocked
 * @author Alexander Manes
 */
public class XincoCoreUserServer extends XincoCoreUser implements AuditableDAO, PersistenceServerObject {

    private boolean hashPassword = true;
    private boolean increaseAttempts = false;
    private Vector xinco_core_groups;
    private boolean change = false,  writegroups = false;
    private static List result;

    /**
     * Create user object and login
     * @param username
     * @param password
     * @throws com.bluecubs.xinco.core.exception.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreUserServer(String username, String password) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        XincoCoreUser temp;
        try {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Logging as user: " + username + ", password: " + password);
            }
            result = pm.executeQuery("SELECT p FROM XincoCoreUser p WHERE p.username='" +
                    username + "' AND p.userpassword='" + MD5.encrypt(password) + "' AND p.statusNumber <> 2");
            //throw exception if no result found
            if (result.size() > 0) {
                temp = (XincoCoreUser) result.get(0);
                setId(temp.getId());
                setUsername(temp.getUsername());
                //bug fix: previously hashing the already hashed password
                hashPassword = true;
                setUserpassword(password);
                setName(temp.getName());
                setFirstname(temp.getFirstname());
                setEmail(temp.getEmail());
                setLastModified(temp.getLastModified());
                int status = 0;
                if (temp.getStatusNumber() != 2) {
                    Calendar cal2 = GregorianCalendar.getInstance(), now = GregorianCalendar.getInstance();
                    cal2.setTime(temp.getLastModified());
                    long diffMillis = now.getTimeInMillis() - cal2.getTimeInMillis();
                    long diffDays = diffMillis / (24 * 60 * 60 * 1000);
                    long age = XincoSettingServer.getSetting("password.aging").getIntValue();
                    if (diffDays >= age) {
                        status = 3;
                    } else {
                        status = 1;
                    }
                    setAttempts(0);
                } else {
                    setAttempts(temp.getAttempts());
                }
                setStatusNumber(status);
                if (isChange()) {
                    //Only save if something changed!
                    write2DB();
                }
                setChange(false);
            } else {
                parameters.clear();
                parameters.put("username", username);
                result = pm.namedQuery("XincoCoreUser.findByUsername", parameters);
                //The username is valid but wrong password. Increase the login attempts.
                if (result.size() > 0) {
                    increaseAttempts = true;
                    setAttempts(((XincoCoreUser) result.get(0)).getAttempts());
                    if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                        Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Username exists but wrong password.");
                    }
                }
                throw new XincoException();
            }
            fillXincoCoreGroups();
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            if (getXincoCoreGroups() != null) {
                getXincoCoreGroups().removeAllElements();
            }
            try {
                result = pm.executeQuery("SELECT p FROM XincoCoreUser p WHERE p.username='" +
                        username + "' AND p.statusNumber <> 2");
                //increase number of attempts
                if (result.size() > 0) {
                    temp = (XincoCoreUser) result.get(0);
                    setId(temp.getId());
                    setUsername(temp.getUsername());
                    setUserpassword(temp.getUserpassword());
                    setName(temp.getName());
                    setFirstname(temp.getFirstname());
                    setEmail(temp.getEmail());
                    setLastModified(temp.getLastModified());
                    //bug fix: Don't rehash the pasword!
                    hashPassword = false;
                    //Increase attempts after a unsuccessfull login.
                    setIncreaseAttempts(true);
                    setChange(false);
                    write2DB();
                }
            } catch (Throwable ex) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create user object for data structures
     * @param attrID
     * @throws com.bluecubs.xinco.core.exception.XincoException 
     */
    @SuppressWarnings("unchecked")
    public XincoCoreUserServer(int attrID) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        try {
            HashMap temp = new HashMap();
            temp.put("id", attrID);
            result = pm.namedQuery("XincoCoreUser.findById", temp);
            //throw exception if no result found
            int RowCount = 0;
            if (result.size() > 0) {
                RowCount++;
                XincoCoreUser user = (XincoCoreUser) result.get(0);
                setId(user.getId());
                setUsername(user.getUsername());
                setUserpassword(user.getUserpassword());
                setName(user.getName());
                setFirstname(user.getFirstname());
                setEmail(user.getEmail());
                setStatusNumber(user.getStatusNumber());
                setAttempts(user.getAttempts());
                setLastModified(user.getLastModified());
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
            fillXincoCoreGroups();
        } catch (Throwable e) {
            getXincoCoreGroups().removeAllElements();
            throw new XincoException();
        }
    }

    /**
     * Create user object for data structures
     * @param attrID
     * @param attrUN
     * @param attrUPW
     * @param attrN
     * @param attrFN
     * @param attrE
     * @param attrSN
     * @param attrAN
     * @param attrTS
     * @throws com.bluecubs.xinco.core.exception.XincoException
     */
    public XincoCoreUserServer(int attrID, String attrUN, String attrUPW, String attrN,
            String attrFN, String attrE, int attrSN, int attrAN, Date attrTS) throws XincoException {
        pm.setShowQueries(XincoSettingServer.getSetting("setting.printDBTransactions.enable").getBoolValue());
        try {
            setId(attrID);
            setUsername(attrUN);
            setUserpassword(attrUPW);
            setName(attrN);
            setFirstname(attrFN);
            setEmail(attrE);
            setStatusNumber(attrSN);
            setAttempts(attrAN);
            setLastModified(attrTS);
            fillXincoCoreGroups();
        } catch (Throwable e) {
            getXincoCoreGroups().removeAllElements();
            throw new XincoException();
        }
    }

    @SuppressWarnings("unchecked")
    private void fillXincoCoreGroups() throws XincoException {
        setXincoCoreGroups(new Vector());
        try {
            parameters.clear();
            parameters.put("xincoCoreUserId", this.getId());
            result = pm.namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", parameters);
            while (!result.isEmpty()) {
                parameters.clear();
                parameters.put("id", ((XincoCoreUserHasXincoCoreGroup) result.get(0)).getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
                getXincoCoreGroups().add((XincoCoreGroup) pm.namedQuery("XincoCoreGroup.findById", parameters).get(0));
                result.remove(0);
            }
        } catch (Throwable e) {
            getXincoCoreGroups().removeAllElements();
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    @SuppressWarnings("unchecked")
    private void writeXincoCoreGroups() throws XincoException {
        boolean found = false;
        int i = -1;
        int place = 0;
        parameters.clear();
        parameters.put("xincoCoreUserId", getId());
        result = pm.namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", parameters);
        while (!result.isEmpty()) {
            for (int j = 0; j < getXincoCoreGroups().size(); j++) {
                if (((XincoCoreGroup) getXincoCoreGroups().elementAt(j)).getId() ==
                        ((XincoCoreUserHasXincoCoreGroup) result.get(0)).getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId()) {
                    found = true;
                    place = j;
                }
            }
            //The record exists
            if (found) {
                //Change detected
                if (getStatusNumber() != ((XincoCoreUserHasXincoCoreGroup) result.get(0)).getStatusNumber()) {
                    XincoCoreGroupServer t = new XincoCoreGroupServer(((XincoCoreUserHasXincoCoreGroup) result.get(0)).getXincoCoreUserHasXincoCoreGroupPK().getXincoCoreGroupId());
                    t.setStatusNumber(getStatusNumber());
                    t.setChangerID(getChangerID());
                    t.write2DB();
                }
            } //New record
            else {
                new XincoCoreGroupServer(((XincoCoreGroupServer) getXincoCoreGroups().elementAt(i)).getId()).write2DB();
            }
            found = false;
            place =
                    0;
            result.remove(0);
        }
    }

    /**
     * Create complete list of users
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreUsers() {
        Vector coreUsers = new Vector();
        try {
            List temp = pm.executeQuery("SELECT p FROM XincoCoreUser p ORDER BY p.username");
            while (!temp.isEmpty()) {
                XincoCoreUser user = (XincoCoreUser) temp.get(0);
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO,
                            "Adding user: " + user + " to de user list");
                }
                coreUsers.add(user);
                temp.remove(0);
            }
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            coreUsers.removeAllElements();
        }
        return coreUsers;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isHashPassword() {
        return hashPassword;
    }

    /**
     * SetHashPassword
     * @param hashPassword
     */
    public void setHashPassword(boolean hashPassword) {
        this.hashPassword = hashPassword;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isIncreaseAttempts() {
        return increaseAttempts;
    }

    /**
     * SetIncreaseAttempts
     * @param increaseAttempts
     */
    public void setIncreaseAttempts(boolean increaseAttempts) {
        this.increaseAttempts = increaseAttempts;
    }

    /**
     * Checks if password is usable based on the password rules defined in the DB
     * @param newPass
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean isPasswordUsable(String newPass) {
        boolean passwordIsUsable = false;
        try {
            /*Bug fix: The password was only verified against past passwords not current password.
             *The current passwords is not usable after the first change when it was added to the
             *audit trail table.
             */
            //Now check if password is not the same as the current password
            result = pm.executeQuery("select p from XincoCoreUser p where p.id=" +
                    getId() + " and p.userpassword='" + MD5.encrypt(newPass) + "'");
            //Here we'll catch if the password is the same as the actual
            if (result.size() == 0) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    System.out.println("Password different than actual");
                }
                //If password is not current then check against audit trail
                // compute max mod date
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -XincoSettingServer.getSetting("password.unusable_period").getIntValue());
                parameters.clear();
                parameters.put("maxModDate", cal.getTime());
                result =
                        pm.createdQuery("from XincoCoreUserT p where p.id=" +
                        getId() + " and p.lastModified >= :maxModDate", parameters);
                //Here we'll catch if the password have been used in the unusable period
                if (result.size() == 0) {
                    if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                        System.out.println("Password not used within the unusable period-" +
                                XincoSettingServer.getSetting("password.unusable_period").getIntValue());
                    }

                    passwordIsUsable = true;
                } else {
                    if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                        System.out.println("Password used within the unusable period-" +
                                XincoSettingServer.getSetting("password.unusable_period").getIntValue());
                    }

                    passwordIsUsable = false;
                }
//End bug fix
            }
            return passwordIsUsable;
        } catch (Throwable ex) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Vector getXincoCoreGroups() {
        return xinco_core_groups;
    }

    public void setXincoCoreGroups(Vector xinco_core_groups) {
        this.xinco_core_groups = xinco_core_groups;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isWriteGroups() {
        return writegroups;
    }

    public void setWriteGroups(boolean writegroups) {
        this.writegroups = writegroups;
    }

    @Override
    public AbstractAuditableObject findById(
            HashMap parameters) {
        result = pm.namedQuery("XincoCoreUser.findById", parameters);
        if (result.size() > 0) {
            XincoCoreUser temp = (XincoCoreUser) result.get(0);
            temp.setTransactionTime(getTransactionTime());
            temp.setChangerID(getChangerID());
            return temp;
        } else {
            return null;
        }

    }

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws XincoSettingException {
        int counter = 0;
        String sql = "SELECT x FROM XincoCoreUser x WHERE ";
        if (parameters.containsKey("username")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by username");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.username = :username";
            counter++;
        }
        // Ignored Password search on purpose as it might be a security flaw.
        if (parameters.containsKey("name")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by name");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.name = :name";
            counter++;
        }
        if (parameters.containsKey("firstname")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by firstname");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.firstname = :firstname";
            counter++;
        }
        if (parameters.containsKey("email")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by email");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.email = :email";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        if (parameters.containsKey("attempts")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by attempts");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attempts = :attempts";
            counter++;
        }
        if (parameters.containsKey("lastModified")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Searching by lastModified");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.lastModified = :lastModified";
            counter++;
        }
        result = pm.createdQuery(sql, parameters);
        if (result.size() > 0) {
            XincoCoreUser temp[] = new XincoCoreUser[result.size()];
            int i = 0;
            while (!result.isEmpty()) {
                temp[i] = (XincoCoreUser) result.get(0);
                temp[i].setTransactionTime(getTransactionTime());
                i++;
                result.remove(0);
            }
            return temp;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    public int getNewID() {
        return new XincoIDServer("xinco_core_user").getNewTableID();
    }

    public boolean write2DB() {
        try {
            Date ts = null;
            XincoCoreUser temp = new XincoCoreUser();
            temp.setId(getId());
            temp.setUsername(getUsername());
            temp.setUserpassword(getUserpassword());
            temp.setName(getName());
            temp.setFirstname(getFirstname());
            temp.setEmail(getEmail());
            temp.setStatusNumber(getStatusNumber());
            temp.setChangerID(getChangerID());
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Writing user: " + temp +
                        " to the database! (" + this + ")");
            }
            if (getStatusNumber() == 4) {
                //Changed from aged out to password changed. Clear status
                temp.setStatusNumber(1);
                temp.setAttempts(0);
                setChange(true);
                temp.setReason("audit.user.account.aged");
                ts = new Timestamp(System.currentTimeMillis());
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "User's password was aged and changed!");
                }
            }
            //Increase login attempts
            if (increaseAttempts) {
                temp.setAttempts(getAttempts() + 1);
                increaseAttempts = false;
            }
            //Lock account if needed. Can't lock main admin.
            if (getAttempts() > XincoSettingServer.getSetting("password.attempts").getIntValue() && getId() > 1) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Account is being locked!");
                }
                temp.setStatusNumber(2);
            }
            setTransactionTime(DateRange.startingNow());
            if (getId() > 0) {
                if (isChange()) {
                    ts = new Date(System.currentTimeMillis());
                    setChange(false);
                } else {
                    ts = getLastModified();
                }
                temp.setLastModified(ts);
                //Sometimes password got re-hashed
                if (hashPassword) {
                    temp.setUserpassword(MD5.encrypt(getUserpassword().replaceAll("'", "\\\\'")));
                } else {
                    temp.setUserpassword(getUserpassword().replaceAll("'", "\\\\'"));
                }
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Updating user: " +
                            temp + " (Status: " + temp.getStatusNumber() + ")");
                }
                temp = ((XincoCoreUser) AuditingDAOHelper.update(this, new XincoCoreUser(getId())));
                setId(temp.getId());
            } else {
                temp.setLastModified(new Timestamp(System.currentTimeMillis()));
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Creating new user: " + temp);
                }
                temp = ((XincoCoreUser) AuditingDAOHelper.create(this, temp));
                setId(temp.getId());
            }
            if (isWriteGroups()) {
                if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                    Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Writing xinco core groups for user: " + temp);
                }
                writeXincoCoreGroups();
            }
            setChange(false);
            setWriteGroups(false);
            setReason("");
            return true;
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId());
            return true;
        } catch (Throwable e) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[id=" + getId() +
                ",attempts=" + getAttempts() + ",email=" + getEmail() +
                ",first name=" + getFirstname() + ",name=" + getName() +
                ",last modified=" + getLastModified() +
                ",status number=" + getStatusNumber() +
                ",username=" + getUsername() + "]";
    }

    public AbstractAuditableObject create(AbstractAuditableObject value) {
        XincoCoreUser temp = (XincoCoreUser) value,
                newValue = new XincoCoreUser();
        boolean exists = false;
        if (!temp.isCreated()) {
            newValue.setId(temp.getId());
            newValue.setRecordId(temp.getRecordId());
            if (temp.getId() > 0) {
                //An object for updating
                exists = true;
            } else {
                //A new object
                exists = false;
            }
        } else {
            newValue.setId(temp.getRecordId());
        }
        newValue.setAttempts(temp.getAttempts());
        newValue.setEmail(temp.getEmail());
        newValue.setFirstname(temp.getFirstname());
        newValue.setLastModified(temp.getLastModified());
        newValue.setName(temp.getName());
        newValue.setStatusNumber(temp.getStatusNumber());
        newValue.setUsername(temp.getUsername());
        newValue.setUserpassword(temp.getUserpassword());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, exists, true);
        return temp;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) {
        XincoCoreUser val = (XincoCoreUser) value;
        XincoCoreUserT temp = new XincoCoreUserT();
        if (!value.isCreated()) {
            temp.setId(val.getId());
        } else {
            temp.setId(val.getRecordId());
        }
        temp.setRecordId(val.getRecordId());
        temp.setAttempts(val.getAttempts());
        temp.setEmail(val.getEmail());
        temp.setFirstname(val.getFirstname());
        temp.setLastModified(val.getLastModified());
        temp.setName(val.getName());
        temp.setUsername(val.getUsername());
        temp.setUserpassword(val.getUserpassword());
        temp.setStatusNumber(val.getStatusNumber());
        try {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.INFO, "Updating user: " + val);
            }
        } catch (XincoSettingException ex) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.persist(val, true, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
        return val;
    }

    public void delete(AbstractAuditableObject value) {
        XincoCoreUser val = (XincoCoreUser) value;
        XincoCoreUserT temp = new XincoCoreUserT();
        temp.setRecordId(val.getRecordId());
        temp.setId(val.getId());
        temp.setUsername(val.getUsername());
        temp.setUserpassword(val.getUserpassword());
        temp.setAttempts(val.getAttempts());
        temp.setEmail(val.getEmail());
        temp.setFirstname(val.getFirstname());
        temp.setLastModified(val.getLastModified());
        temp.setName(val.getName());
        temp.setStatusNumber(val.getStatusNumber());
        pm.startTransaction();
        pm.persist(temp, false, false);
        pm.delete(val, false);
        getModifiedRecordDAOObject().saveAuditData();
        pm.commitAndClose();
    }
}
