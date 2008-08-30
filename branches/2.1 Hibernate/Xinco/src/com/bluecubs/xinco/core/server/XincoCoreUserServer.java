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
 * Javier A. Ortiz 01/08/2007         
 *************************************************************
 */
package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import java.util.Vector;

import com.bluecubs.xinco.core.hibernate.audit.XincoAuditableDAO;
import com.bluecubs.xinco.core.persistence.XincoCoreUserT;
import com.bluecubs.xinco.core.persistence.XincoCoreUser;
import com.bluecubs.xinco.core.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.tools.MD5;
import com.dreamer.Hibernate.Audit.AbstractAuditableObject;
import com.dreamer.Hibernate.Audit.AuditingDAOHelper;
import com.dreamer.Hibernate.Audit.PersistenceServerObject;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import net.sf.oness.common.model.temporal.DateRange;
import java.util.logging.Level;
import java.util.logging.Logger;

//Status list (in DB)
//1 = unlocked
//2 = locked
//3 = aged password
//Temporary statuses
//-1 = aged password modified, ready to turn unlocked
public class XincoCoreUserServer extends XincoCoreUser implements XincoAuditableDAO, PersistenceServerObject {

    private static final long serialVersionUID = -1768571785372297089L;
    private String sql;
    private boolean change = true,  writeGroups = false;
    private ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages"),  settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
    private int attempts;
    private Vector xinco_core_groups;
    private static List result;

    //create user object and login
    @SuppressWarnings("unchecked")
    public XincoCoreUserServer(String attrUN, String attrUPW) throws XincoException {
        try {
            parameters.clear();
            parameters.put("username", attrUN);
            parameters.put("userpassword", MD5.encrypt(attrUPW));
            result = pm.createdQuery("SELECT x FROM XincoCoreUser x WHERE " +
                    "x.username = :username and x.userpassword = :userpassword " +
                    "and x.statusNumber != 2", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreUser xcu = (XincoCoreUser) result.get(0);
                setId(xcu.getId());
                setUsername(xcu.getUsername());
                //previously hashing the already hashed password
                setUserpassword(MD5.encrypt(attrUPW));
                setName(xcu.getName());
                setFirstname(xcu.getFirstname());
                setEmail(xcu.getEmail());
                int status = 0;
                if (xcu.getStatusNumber() != 2) {
                    Calendar cal2 = GregorianCalendar.getInstance(), now = GregorianCalendar.getInstance();
                    cal2.setTime(xcu.getLastModified());
                    long diffMillis = now.getTimeInMillis() - cal2.getTimeInMillis();
                    long diffDays = diffMillis / (24 * 60 * 60 * 1000);
                    long age = Long.parseLong(settings.getString("password.aging"));
                    if (diffDays >= age) {
                        status = 3;
                    } else {
                        status = 1;
                    }
                    setAttempts(0);
                } else {
                    setAttempts(xcu.getAttempts());
                }
                setStatusNumber(status);
                setLastModified(xcu.getLastModified());
                write2DB();
            } else {
                parameters.clear();
                parameters.put("username", attrUN);
                result = pm.createdQuery("SELECT x FROM XincoCoreUser x WHERE " +
                        "x.username = :username", parameters);
                //The username is valid but wrong password. Increase the login attempts.
                if (!result.isEmpty()) {
                    setAttempts(getAttempts() + 1);
                }
                throw new XincoException();
            }
            fillXincoCoreGroups();
        } catch (Exception e) {
            if (getXincoCoreGroups() != null) {
                getXincoCoreGroups().removeAllElements();
            }
            parameters.clear();
            parameters.put("username", attrUN);
            result = pm.createdQuery("SELECT x FROM XincoCoreUser x WHERE " +
                    "x.username = :username and" +
                    " x.statusNumber != 2", parameters);
            //increase number of attempts
            if (!result.isEmpty()) {
                XincoCoreUser xcu = (XincoCoreUser) result.get(0);
                setId(xcu.getId());
                setUsername(xcu.getUsername());
                setUserpassword(xcu.getUserpassword());
                setName(xcu.getName());
                setFirstname(xcu.getFirstname());
                setEmail(xcu.getEmail());
                setStatusNumber(xcu.getStatusNumber());
                //Increase attempts after a unsuccessfull login.
                setAttempts(xcu.getAttempts() + 1);
                setLastModified(xcu.getLastModified());
                setChange(false);
                write2DB();
            }
            throw new XincoException(e.getLocalizedMessage());
        }
    }

//create user object for data structures
    @SuppressWarnings("unchecked")
    public XincoCoreUserServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = pm.namedQuery("XincoCoreUser.findById", parameters);
            //throw exception if no result found
            if (!result.isEmpty()) {
                XincoCoreUser xcu = (XincoCoreUser) result.get(0);
                setId(xcu.getId());
                setUsername(xcu.getUsername());
                setUserpassword(xcu.getUserpassword());
                setName(xcu.getName());
                setFirstname(xcu.getFirstname());
                setEmail(xcu.getEmail());
                setStatusNumber(xcu.getStatusNumber());
                setAttempts(xcu.getAttempts());
                setLastModified(xcu.getLastModified());
            } else {
                throw new XincoException();
            }
            fillXincoCoreGroups();
        } catch (Exception e) {
            getXincoCoreGroups().removeAllElements();
            throw new XincoException();
        }
    }

//create user object
    public XincoCoreUserServer(int attrID, String attrUN, String attrUPW, String attrN,
            String attrFN, String attrE, int attrSN, int attrAN, java.sql.Timestamp attrTS) throws XincoException {
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
        } catch (Exception e) {
            getXincoCoreGroups().removeAllElements();
            throw new XincoException();
        }
    }

    @SuppressWarnings("unchecked")
    private void fillXincoCoreGroups() throws XincoException {
        setXincoCoreGroups(new Vector());
        try {
            parameters.clear();
            parameters.put("xincoCoreUserId", getId());
            result = pm.namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", parameters);
            while (!result.isEmpty()) {
                getXincoCoreGroups().addElement((XincoCoreUserHasXincoCoreGroup) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            getXincoCoreGroups().removeAllElements();
            throw new XincoException(e.getLocalizedMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void writeXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        sql = null;
        int i = -1;
        try {
            parameters.clear();
            parameters.put("id", getId());
            result = DBM.namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", parameters);
            while (!result.isEmpty()) {
                XincoCoreUserHasXincoCoreGroupServer xcuhxcg = ((XincoCoreUserHasXincoCoreGroupServer) result.get(0));
                xcuhxcg.deleteFromDB();
                result.remove(0);
            }
            for (i = 0; i < getXincoCoreGroups().size(); i++) {
                XincoCoreUserHasXincoCoreGroupServer xcuhxcg = new XincoCoreUserHasXincoCoreGroupServer(getId(),
                        ((XincoCoreGroupServer) getXincoCoreGroups().elementAt(i)).getId(), 1);
                xcuhxcg.write2DB();
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }

    /**
     * Gets the attempts value for this XincoCoreUser.
     *
     * @return attempts
     */
    @Override
    public int getAttempts() {
        return attempts;
    }

    /**
     * Sets the attempts value for this XincoCoreUser.
     *
     * @param attempts
     */
    @Override
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

//create complete list of users
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreUsers() {
        Vector coreUsers = new Vector();
        try {
            result = pm.createdQuery("SELECT x FROM XincoCoreUser x ORDER BY x.username");
            while (!result.isEmpty()) {
                coreUsers.addElement((XincoCoreUser) result.get(0));
                result.remove(0);
            }
        } catch (Exception e) {
            coreUsers.removeAllElements();
        }
        return coreUsers;
    }

    @SuppressWarnings("unchecked")
    public boolean isPasswordUsable(String newPass) {
        int tempId = 0;
        boolean passwordIsUsable = false;
        //Reject empty passwords as well
        if (newPass.trim().isEmpty()) {
            return false;
        }
        try {
            /*Bug fix: The password was only verified against past passwords not current password.
             *The current passwords is not usable after the first change when it was added to the
             *audit trail table.
             */
            //Now check if password is not the same as the current password
            result = pm.createdQuery("select x from XincoCoreUser x where x.id=" +
                    getId() + " and x.userpassword='" + MD5.encrypt(newPass) + "'");
            if (!result.isEmpty()) {
                return false;
            }
            //Here we'll catch if the password have been used in the unusable period
            parameters.clear();
            parameters.put("username", getUsername());
            result = pm.createdQuery("XincoCoreUser.findByUsername");
            tempId = ((XincoCoreUser) result.get(0)).getId();
            result = pm.createdQuery("select x from XincoCoreUserT where t.id=" +
                    tempId + " and DATEDIFF('dd',NOW(),x.lastModified) <= " +
                    settings.getString("password.unusable_period") + " and " +
                    MD5.encrypt(newPass) + " = x.userpassword");
            result.get(0);
        //---------------------------
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
            passwordIsUsable = true;
        } catch (Exception ex) {
            passwordIsUsable = true;
        }
        return passwordIsUsable;
    }

    /**
     * @return the xinco_core_groups
     */
    public Vector getXincoCoreGroups() {
        return xinco_core_groups;
    }

    /**
     * @param xinco_core_groups the xinco_core_groups to set
     */
    public void setXincoCoreGroups(Vector xinco_core_groups) {
        this.xinco_core_groups = xinco_core_groups;
    }

    public AbstractAuditableObject findById(HashMap parameters) throws Exception {
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

    public AbstractAuditableObject[] findWithDetails(HashMap parameters) throws Exception {
        int counter = 0;
        sql = "SELECT x FROM XincoCoreUser x WHERE ";
        if (parameters.containsKey("id")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by id");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.id = :id";
            counter++;
        }
        if (parameters.containsKey("username")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by username");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.username = :username";
            counter++;
        }
        if (parameters.containsKey("userpassword")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by userpassword");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.userpassword = :userpassword";
            counter++;
        }
        if (parameters.containsKey("name")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by name");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.name = :name";
            counter++;
        }
        if (parameters.containsKey("firstname")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by firstname");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.firstname = :firstname";
            counter++;
        }
        if (parameters.containsKey("email")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by email");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.email = :email";
            counter++;
        }
        if (parameters.containsKey("statusNumber")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by statusNumber");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.statusNumber = :statusNumber";
            counter++;
        }
        if (parameters.containsKey("attempts")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by attempts");
            }
            if (counter > 0) {
                sql += " and ";
            }
            sql += "x.attempts = :attempts";
            counter++;
        }
        if (parameters.containsKey("lastModified")) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Searching by lastModified");
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

    @SuppressWarnings("static-access")
    public AbstractAuditableObject create(AbstractAuditableObject value) throws Exception {
        XincoCoreUser temp;
        XincoCoreUser newValue = new XincoCoreUser();
        temp = (XincoCoreUser) value;
        newValue.setId(temp.getRecordId());
        newValue.setUsername(getUsername());
        newValue.setUserpassword(getUserpassword());
        newValue.setName(getName());
        newValue.setFirstname(getFirstname());
        newValue.setEmail(getEmail());
        newValue.setStatusNumber(getStatusNumber());
        newValue.setLastModified(getLastModified());

        newValue.setRecordId(temp.getRecordId());
        newValue.setCreated(temp.isCreated());
        newValue.setChangerID(temp.getChangerID());
        newValue.setTransactionTime(getTransactionTime());
        pm.persist(newValue, false, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO,
                    "New value created: " + newValue);
        }
        return newValue;
    }

    public AbstractAuditableObject update(AbstractAuditableObject value) throws Exception {
        XincoCoreUser val = (XincoCoreUser) value;
        pm.persist(val, true, true);
        if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO,
                    "Value updated: " + val);
        }
        return val;
    }

    @SuppressWarnings({"unchecked", "static-access"})
    public boolean delete(AbstractAuditableObject value) throws Exception {
        try {
            XincoCoreUser val = (XincoCoreUser) value;
            XincoCoreUserT temp = new XincoCoreUserT();

            temp.setRecordId(val.getRecordId());
            temp.setId(val.getId());


            temp.setUsername(val.getUsername());
            temp.setUserpassword(val.getUserpassword());
            temp.setName(val.getName());
            temp.setFirstname(val.getFirstname());
            temp.setEmail(val.getEmail());
            temp.setStatusNumber(val.getStatusNumber());
            temp.setLastModified(val.getLastModified());

            pm.startTransaction();
            pm.persist(temp, false, false);
            pm.delete(val, false);
            setModifiedRecordDAOObject(value.getModifiedRecordDAOObject());
            //Make sure all audit data is stored properly. If not undo everything
            if (!getModifiedRecordDAOObject().saveAuditData()) {
                throw new XincoException(rb.getString("error.audit_data.invalid"));
            }
            setId(val.getId());
            return pm.commitAndClose();
        } catch (Throwable ex) {
            pm.rollback();
            Logger.getLogger(XincoCoreACEServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap getParameters() {
        HashMap temp = new HashMap();
        temp.put("id", getId());
        return temp;
    }

    /**
     * Get a new newID
     * @param a
     * @return New last ID
     */
    @SuppressWarnings("unchecked")
    public int getNewID(boolean a) {
        return new XincoIDServer("Xinco_Core_User").getNewTableID(a);
    }

    @SuppressWarnings("unchecked")
    public boolean write2DB() {
        try {
            sql = "";
            xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
            try {
                if (getStatusNumber() == 4) {
                    //Changed from aged out to password changed. Clear status
                    setStatusNumber(1);
                    setAttempts(0);
                    setChange(true);
                    setReason("audit.user.account.aged");
                    setLastModified(new Timestamp(System.currentTimeMillis()));
                }
                //Lock account if needed. Can't lock main admin.
                if (getAttempts() > Integer.parseInt(settings.getString("password.attempts")) &&
                        getId() > 1) {
                    setStatusNumber(2);
                }
                if (getId() > 0) {
                    if (isChange()) {
                        setLastModified(new Timestamp(System.currentTimeMillis()));
                        AuditingDAOHelper.update(this, new XincoCoreUser());
                    }else{

                    }
                    setChange(false);
                } else {
                    XincoCoreUser temp = new XincoCoreUser();
                    setId(getNewID(true));
                    setUsername(getUsername());
                    setUserpassword(getUserpassword());
                    setName(getName());
                    setFirstname(getFirstname());
                    setEmail(getEmail());
                    setAttempts(getAttempts());
                    setStatusNumber(getStatusNumber());
                    setLastModified(getLastModified());
                    temp = (XincoCoreUser) AuditingDAOHelper.create(this, temp);
                    setId(temp.getId());
                    if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                        Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.INFO, "Assigned id: " + getId());
                    }
                }
                if (isWriteGroups()) {
                    writeXincoCoreGroups((XincoDBManager) pm);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new XincoException();
            }
            setChange(false);
            setWriteGroups(false);
            setReason("");
            return true;
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteFromDB() {
        setTransactionTime(DateRange.startingNow());
        try {
            AuditingDAOHelper.delete(this, getId(), getChangerID());
            return true;
        } catch (Throwable e) {
            if (XincoSettingServer.getSetting("setting.enable.developermode").getBoolValue()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            return false;
        }
    }

    /**
     * @return the change
     */
    public boolean isChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(boolean change) {
        this.change = change;
    }

    /**
     * @return the writeGroups
     */
    public boolean isWriteGroups() {
        return writeGroups;
    }

    /**
     * @param writeGroups the writeGroups to set
     */
    public void setWriteGroups(boolean writeGroups) {
        this.writeGroups = writeGroups;
    }
}
