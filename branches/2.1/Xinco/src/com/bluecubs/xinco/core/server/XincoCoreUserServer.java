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
package com.bluecubs.xinco.core.server;

import java.sql.*;
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Status list (in DB)
 * 1 = unlocked
 * 2 = locked
 * 3 = aged password
 * Temporary statuses
 * -1 = aged password modified, ready to turn unlocked
 * @author Alexander Manes
 */
public class XincoCoreUserServer extends XincoCoreUser {

    private String sql;
    private boolean hashPassword = true;
    private boolean increaseAttempts = false;
    private ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages"),  settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
    private java.sql.Timestamp lastModified;
    private int attempts;
    private XincoCoreAuditTrail audit = new XincoCoreAuditTrail();
    private ResultSet rs = null;

    @SuppressWarnings("unchecked")
    private void fillXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        setXinco_core_groups(new Vector());
        try {
            sql = "SELECT * FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId();
            rs = DBM.executeQuery(sql);
            while (rs.next()) {
                getXinco_core_groups().addElement(new XincoCoreGroupServer(rs.getInt("xinco_core_group_id"), DBM));
            }
        } catch (Throwable e) {
            getXinco_core_groups().removeAllElements();
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                e.printStackTrace();
            }
            throw new XincoException();
        }
    }

    private void writeXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        sql = null;
        ResultSet rs2 = null;
        boolean found = false;
        int i = -1;
        int place = 0;
        try {
            rs = DBM.executeQuery("select * from xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
            while (rs.next()) {
                for (int j = 0; j < getXinco_core_groups().size(); j++) {
                    if (((XincoCoreGroupServer) getXinco_core_groups().elementAt(j)).getId() == rs.getInt("xinco_core_group_id")) {
                        found = true;
                        place = j;
                    }
                }
                //The record exists
                if (found) {
                    //Change detected
                    if (getStatus_number() != rs.getInt("status_number")) {
                        DBM.executeUpdate("update xinco_core_user_has_xinco_core_group set status_number=" +
                                getStatus_number() + " where xinco_core_user_id=" + getId() + " and xinco_core_group_id=" +
                                ((XincoCoreGroupServer) getXinco_core_groups().elementAt(place)).getId());
                        audit.updateAuditTrail("xinco_core_user_has_xinco_core_group", new String[]{"xinco_core_user_id =" + getId(),
                            "xinco_core_group_id=" + ((XincoCoreGroupServer) getXinco_core_groups().elementAt(place)).getId()
                        }, DBM, "audit.general.modified", getChangerID());
                    }
                } //New record
                else {
                    sql = "INSERT INTO xinco_core_user_has_xinco_core_group VALUES (" + getId() +
                            ", " + ((XincoCoreGroupServer) getXinco_core_groups().elementAt(i)).getId() +
                            ", " + 1 + ")";
                    DBM.executeUpdate(sql);
                    audit.updateAuditTrail("xinco_core_user_has_xinco_core_group", new String[]{"xinco_core_user_id =" + getId(),
                        "xinco_core_group_id=" + ((XincoCoreGroupServer) getXinco_core_groups().elementAt(place)).getId()
                    }, DBM, "audit.general.created", getChangerID());
                }
                found = false;
                place = 0;
            }
        } catch (Throwable e) {
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                e.printStackTrace();
            }
            throw new XincoException();
        }
    }

    /**
     * Create user object and login
     * @param username
     * @param password
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreUserServer(String username, String password, XincoDBManager DBM) throws XincoException {
        rs = null;
        GregorianCalendar cal = null;
        try {
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.WARNING, "Logging as user: " + username + ", password: " + password);
            }
            sql = "SELECT * FROM xinco_core_user WHERE username='" +
                    username + "' AND userpassword=MD5('" + password + "') AND status_number <> 2";
            rs = DBM.executeQuery(sql);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setUsername(rs.getString("username"));
                //bug fix: previously hashing the already hashed password
                hashPassword = true;
                setUserpassword(password);
                setName(rs.getString("name"));
                setFirstname(rs.getString("firstname"));
                setEmail(rs.getString("email"));
                setLastModified(rs.getTimestamp("last_modified"));
                int status = 0;
                if (rs.getInt("status_number") != 2) {
                    Calendar cal2 = GregorianCalendar.getInstance(), now = GregorianCalendar.getInstance();
                    cal2.setTime(rs.getTimestamp("last_modified"));
                    long diffMillis = now.getTimeInMillis() - cal2.getTimeInMillis();
                    long diffDays = diffMillis / (24 * 60 * 60 * 1000);
                    long age = DBM.getSetting("password.aging").getInt_value();
                    if (diffDays >= age) {
                        status = 3;
                    } else {
                        status = 1;
                    }
                    setAttempts(0);
                } else {
                    setAttempts(rs.getInt("attempts"));
                }
                setStatus_number(status);
                write2DB(DBM);
            }
            if (RowCount < 1) {
                sql = "SELECT * FROM xinco_core_user WHERE username='" +
                        username + "'";
                rs = DBM.executeQuery(sql);
                //The username is valid but wrong password. Increase the login attempts.
                if (rs.next()) {
                    increaseAttempts = true;
                    setAttempts(rs.getInt("attempts"));
                    if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                        Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.WARNING, "Username exists but wrong password.");
                    }
                }
                throw new XincoException();
            }
            fillXincoCoreGroups(DBM);
        } catch (Throwable e) {
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            if (getXinco_core_groups() != null) {
                getXinco_core_groups().removeAllElements();
            }
            try {
                try {
                    DBM = new XincoDBManager();
                } catch (Exception ex) {
                    Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                sql = "SELECT * FROM xinco_core_user WHERE username='" +
                        username + "' AND status_number <> 2";
                ResultSet rs2 = DBM.executeQuery(sql);
                //increase number of attempts
                if (rs2.next()) {
                    setId(rs2.getInt("id"));
                    setUsername(rs2.getString("username"));
                    //bug fix: Don't rehash the pasword!
                    hashPassword = false;
                    setUserpassword(rs2.getString("userpassword"));
                    setName(rs2.getString("name"));
                    setFirstname(rs2.getString("firstname"));
                    setEmail(rs2.getString("email"));
                    setStatus_number(rs2.getInt("status_number"));
                    //Increase attempts after a unsuccessfull login.
                    setIncreaseAttempts(true);
                    setLastModified(rs2.getTimestamp("last_modified"));
                    setChange(false);
                    write2DB(DBM);
                }
                rs2.close();
            } catch (SQLException ex) {
                if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                    Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                Logger.getLogger(XincoCoreUserServer.class.getName()).log(Level.SEVERE, null, e);
            }
            throw new XincoException();
        }
    }

    /**
     * Create user object for data structures
     * @param attrID
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreUserServer(int attrID, XincoDBManager DBM) throws XincoException {
        GregorianCalendar cal = null;
        try {
            rs = DBM.executeQuery("SELECT * FROM xinco_core_user WHERE id=" + attrID);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setUsername(rs.getString("username"));
                setUserpassword(rs.getString("userpassword"));
                setName(rs.getString("name"));
                setFirstname(rs.getString("firstname"));
                setEmail(rs.getString("email"));
                setStatus_number(rs.getInt("status_number"));
                setAttempts(rs.getInt("attempts"));
                setLastModified(rs.getTimestamp("last_modified"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
            fillXincoCoreGroups(DBM);
        } catch (Throwable e) {
            getXinco_core_groups().removeAllElements();
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
     * @param DBM
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public XincoCoreUserServer(int attrID, String attrUN, String attrUPW, String attrN,
            String attrFN, String attrE, int attrSN, int attrAN, java.sql.Timestamp attrTS,
            XincoDBManager DBM) throws XincoException {
        try {
            setId(attrID);
            setUsername(attrUN);
            setUserpassword(attrUPW);
            setName(attrN);
            setFirstname(attrFN);
            setEmail(attrE);
            setStatus_number(attrSN);
            setAttempts(attrAN);
            setLastModified(attrTS);
            fillXincoCoreGroups(DBM);
        } catch (Throwable e) {
            getXinco_core_groups().removeAllElements();
            throw new XincoException();
        }
    }

    /**
     * Gets the attempts value for this XincoCoreUser.
     *
     * @return attempts
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Sets the attempts value for this XincoCoreUser.
     *
     * @param attempts
     */
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    /**
     * Sets the lastModified value for this XincoCoreUser.
     *
     * @param lastModified
     */
    public void setLastModified(java.sql.Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Gets the lastModified value for this XincoCoreUser.
     *
     * @return lastModified
     */
    public java.lang.Object getLastModified() {
        return lastModified;
    }

    /**
     * Write to DB
     * @param DBM
     * @return int
     * @throws com.bluecubs.xinco.core.XincoException
     */
    public int write2DB(XincoDBManager DBM) throws XincoException {
        sql = "";
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        Timestamp ts = null;
        boolean isNew = false;
        try {
            if (getStatus_number() == 4) {
                //Changed from aged out to password changed. Clear status
                setStatus_number(1);
                setAttempts(0);
                setChange(true);
                setReason("audit.user.account.aged");
                ts = new Timestamp(System.currentTimeMillis());
            }
            //Increase login attempts
            if (increaseAttempts) {
                setAttempts(getAttempts() + 1);
                increaseAttempts = false;
            }
            //Lock account if needed. Can't lock main admin.
            if (getAttempts() > DBM.getSetting("password.attempts").getInt_value() &&
                    getId() > 1) {
                setStatus_number(2);
            }
            if (getId() > 0) {
                if (isChange()) {
                    audit.updateAuditTrail("xinco_core_user", new String[]{"id =" + getId()},
                            DBM, getReason(), getChangerID());
                    ts = new Timestamp(System.currentTimeMillis());
                    setLastModified(ts);
                    setChange(false);
                } else {
                    ts = (Timestamp) getLastModified();
                }
                setLastModified(ts);
                //Sometimes password got re-hashed
                String password = "";
                if (hashPassword) {
                    password = "userpassword=MD5('" +
                            getUserpassword().replaceAll("'", "\\\\'") + "')";
                } else {
                    password = "userpassword='" +
                            getUserpassword().replaceAll("'", "\\\\'") + "'";
                }
                sql = "UPDATE xinco_core_user SET username='" +
                        getUsername().replaceAll("'", "\\\\'") + "', " + password + ", name='" +
                        getName().replaceAll("'", "\\\\'") + "', firstname='" +
                        getFirstname().replaceAll("'", "\\\\'") + "', email='" +
                        getEmail().replaceAll("'", "\\\\'") + "', status_number=" +
                        getStatus_number() + ", attempts=" + getAttempts() +
                        ", last_modified='" + getLastModified().toString() + "'" +
                        " WHERE id=" + getId();
                DBM.executeUpdate(sql);
            } else {
                setId(DBM.getNewID("xinco_core_user"));
                isNew = true;
                ts = new Timestamp(System.currentTimeMillis());
                sql = "INSERT INTO xinco_core_user VALUES (" + getId() +
                        ", '" + getUsername().replaceAll("'", "\\\\'") +
                        "', MD5('" + getUserpassword().replaceAll("'", "\\\\'") +
                        "'), '" + getName().replaceAll("'", "\\\\'") + "', '" +
                        getFirstname().replaceAll("'", "\\\\'") + "', '" +
                        getEmail().replaceAll("'", "\\\\'") + "', " +
                        getStatus_number() + ", " + getAttempts() + ", '" + ts.toString() + "')";
                DBM.executeUpdate(sql);
            }
            if (isWriteGroups()) {
                writeXincoCoreGroups(DBM);
            }
            if (isNew) {
                audit.updateAuditTrail("xinco_core_user", new String[]{"id =" + getId()},
                        DBM, getReason(), getChangerID());
                isNew = false;
            }
        } catch (Throwable e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            e.printStackTrace();
            throw new XincoException();
        }
        setChange(false);
        setWriteGroups(false);
        setReason("");
        return getId();
    }

    /**
     * Create complete list of users
     * @param DBM
     * @return Vector
     */
    @SuppressWarnings("unchecked")
    public static Vector getXincoCoreUsers(XincoDBManager DBM) {
        Vector coreUsers = new Vector();
        GregorianCalendar cal = null;
        try {
            ResultSet rs = DBM.executeQuery("SELECT * FROM xinco_core_user ORDER BY username");
            while (rs.next()) {
                coreUsers.addElement(new XincoCoreUserServer(rs.getInt("id"),
                        rs.getString("username"), rs.getString("userpassword"),
                        rs.getString("name"), rs.getString("firstname"),
                        rs.getString("email"), rs.getInt("status_number"),
                        rs.getInt("attempts"), rs.getTimestamp("last_modified"), DBM));
            }

        } catch (Throwable e) {
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
    public boolean isPasswordUsable(String newPass) {
        rs = null;
        XincoDBManager DBM = null;
        sql = null;
        boolean passwordIsUsable = false;
        try {
            try {
                DBM = new XincoDBManager();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            /*Bug fix: The password was only verified against past passwords not current password.
             *The current passwords is not usable after the first change when it was added to the
             *audit trail table.
             */
            //Now check if password is not the same as the current password
            sql = "select userpassword from xinco_core_user where id=" +
                    getId() + " and MD5('" + newPass + "') =userpassword";
            rs = DBM.executeQuery(sql);
            //Here we'll catch if the password is the same as the actual
            rs.next();
            rs.getString(1);
        //End bug fix
        } catch (Throwable ex) {
            if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                System.out.println("Password different than actual");
            }
            try {
                //If password is not current then check against audit trail
                sql = "select userpassword from xinco_core_user_t where id=" +
                        getId() + " and DATEDIFF(NOW(),last_modified) <= " +
                        DBM.getSetting("password.unusable_period").getInt_value() +
                        " and MD5('" + newPass + "') = userpassword";
                rs = DBM.executeQuery(sql);
                //Here we'll catch if the password have been used in the unusable period
                rs.next();
                rs.getString(1);

            } catch (Throwable e) {
                if (DBM.getSetting("setting.enable.developermode").isBool_value()) {
                    System.out.println("Password not used within the unusable period-" +
                            DBM.getSetting("password.unusable_period").getInt_value());
                }
                passwordIsUsable = true;
            }
        }
        return passwordIsUsable;
    }
}
