/**
 *Copyright 2010 blueCubs.com
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

import com.bluecubs.xinco.core.XincoCoreUser;
import com.bluecubs.xinco.core.server.persistence.XincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserHasXincoCoreGroup;
import com.bluecubs.xinco.core.server.persistence.XincoCoreUserT;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserHasXincoCoreGroupJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreUserJpaController;
import java.util.Vector;
import com.bluecubs.xinco.tools.MD5;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
//Status list (in DB)
//1 = unlocked
//2 = locked
//3 = aged password
//Temporary statuses
//-1 = aged password modified, ready to turn unlocked
public class XincoCoreUserServer extends XincoCoreUser {

    private static final long serialVersionUID = 1L;
    private boolean hashPassword = true;
    private boolean increaseAttempts = false;
    private ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages"), settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
    private java.sql.Timestamp lastModified;
    private int attempts;
    private static List result;
    private static HashMap parameters = new HashMap();

    protected XincoCoreUserServer() {
    }

    public XincoCoreUserServer(com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu) {
        setId(xcu.getId());
        setUsername(xcu.getUsername());
        //previously hashing the already hashed password
        hashPassword = false;
        setUserpassword(xcu.getUserpassword());
        setName(xcu.getName());
        setFirstname(xcu.getFirstname());
        setEmail(xcu.getEmail());
        setStatus_number(xcu.getStatusNumber());
        setAttempts(xcu.getAttempts());
        setLastModified(new Timestamp(xcu.getLastModified().getTime()));
        try {
            fillXincoCoreGroups();
        } catch (XincoException ex) {
            Logger.getLogger(XincoCoreUserServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void fillXincoCoreGroups() throws XincoException {
        setXinco_core_groups(new Vector());
        parameters.clear();
        parameters.put("xincoCoreUserId", getId());
        result = XincoDBManager.namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", parameters);
        try {
            for (Object o : result) {
                getXinco_core_groups().addElement(new XincoCoreGroupServer(((XincoCoreUserHasXincoCoreGroup) o).getXincoCoreGroup()));
            }
        } catch (Exception e) {
            getXinco_core_groups().removeAllElements();
            throw new XincoException();
        }
    }

    private void writeXincoCoreGroups() throws XincoException {
        int i = -1;
        try {
            parameters.clear();
            parameters.put("xincoCoreUserId", getId());
            result = XincoDBManager.namedQuery("XincoCoreUserHasXincoCoreGroup.findByXincoCoreUserId", parameters);
            XincoCoreUserHasXincoCoreGroupJpaController controller = new XincoCoreUserHasXincoCoreGroupJpaController();
            try {
                for (Object o : result) {
                    controller.destroy(((XincoCoreUserHasXincoCoreGroup) o).getXincoCoreUserHasXincoCoreGroupPK());
                }
            } catch (Exception e) {
                setXinco_core_groups(new Vector());
                throw new XincoException();
            }
            for (i = 0; i < getXinco_core_groups().size(); i++) {
                XincoCoreGroupServer xcgs=(XincoCoreGroupServer) getXinco_core_groups().elementAt(i);
                parameters.clear();
                parameters.put("id", xcgs.getId());
                XincoDBManager.namedQuery("XincoCoreGroup.findById",parameters);
                controller.create(new XincoCoreUserHasXincoCoreGroup(
                        getId(), (XincoCoreGroup) XincoDBManager.namedQuery(null,parameters).get(0), 1));
            }
        } catch (Exception e) {
            throw new XincoException(e.getLocalizedMessage());
        }
    }

    //create user object and login
    public XincoCoreUserServer(String attrUN, String attrUPW) throws XincoException {
        GregorianCalendar cal = null;
        try {
            result = XincoDBManager.createdQuery("SELECT xcu FROM XincoCoreUser xcu WHERE xcu.username='"
                    + attrUN + "' AND xcu.userpassword='" + MD5.encrypt(attrUPW) + "' AND xcu.statusNumber <> 2");
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0);
                setId(xcu.getId());
                setUsername(xcu.getUsername());
                //previously hashing the already hashed password
                hashPassword = false;
                setUserpassword(attrUPW);
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
                setStatus_number(status);
                setLastModified(new Timestamp(xcu.getLastModified().getTime()));
                try {
                    fillXincoCoreGroups();
                } catch (XincoException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(XincoCoreUserServer.class.getSimpleName()).log(Level.SEVERE, null, ex);
                }
            } else {
                parameters.clear();
                parameters.put("username", attrUN);
                result = XincoDBManager.namedQuery("XincoCoreUser.findByUsername", parameters);
                //The username is valid but wrong password. Increase the login attempts.
                if (result.size() > 0) {
                    increaseAttempts = true;
                    com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu =
                            (com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0);
                    setAttempts(xcu.getAttempts());
                    write2DB();
                }
                throw new XincoException();
            }
            fillXincoCoreGroups();
        } catch (Exception e) {
            e.printStackTrace();
            if (getXinco_core_groups() != null) {
                getXinco_core_groups().removeAllElements();
            }
            try {
                result = XincoDBManager.createdQuery("SELECT xcu FROM XincoCoreUser xcu WHERE xcu.username='"
                        + attrUN + "' AND xcu.statusNumber <> 2");
                //increase number of attempts
                if (result.size() > 0) {
                    com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu =
                            (com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0);
                    setId(xcu.getId());
                    setUsername(xcu.getUsername());
                    //Don't rehash the pasword!
                    hashPassword = false;
                    setUserpassword(xcu.getUserpassword());
                    setName(xcu.getName());
                    setFirstname(xcu.getFirstname());
                    setEmail(xcu.getEmail());
                    setStatus_number(xcu.getStatusNumber());
                    //Increase attempts after a unsuccessfull login.
                    setIncreaseAttempts(true);
                    setLastModified(new Timestamp(xcu.getLastModified().getTime()));
                    setChange(false);
                    write2DB();
                }
            } catch (XincoException ex) {
                ex.printStackTrace();
                e.printStackTrace();
                throw new XincoException();
            }
        }
    }

//create user object for data structures
    public XincoCoreUserServer(int attrID) throws XincoException {
        try {
            parameters.clear();
            parameters.put("id", attrID);
            result = XincoDBManager.namedQuery("XincoCoreUser.findById", parameters);
            //throw exception if no result found
            if (result.size() > 0) {
                com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu =
                        (com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0);
                setId(xcu.getId());
                setUsername(xcu.getUsername());
                //previously hashing the already hashed password
                hashPassword = false;
                setUserpassword(xcu.getUserpassword());
                setName(xcu.getName());
                setFirstname(xcu.getFirstname());
                setEmail(xcu.getEmail());
                setStatus_number(xcu.getStatusNumber());
                setAttempts(xcu.getAttempts());
                setLastModified(new Timestamp(xcu.getLastModified().getTime()));
            } else {
                throw new XincoException();
            }
            fillXincoCoreGroups();
        } catch (Exception e) {
            e.printStackTrace();
            getXinco_core_groups().removeAllElements();
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
            setStatus_number(attrSN);
            setAttempts(attrAN);
            setLastModified(attrTS);
            fillXincoCoreGroups();
            setHashPassword(true);
        } catch (Exception e) {
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
    public java.sql.Timestamp getLastModified() {
        return lastModified;
    }

    //write to db
    public int write2DB() throws XincoException {
        xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        Timestamp ts = null;
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
            if (getAttempts() > Integer.parseInt(settings.getString("password.attempts"))
                    && getId() > 1) {
                setStatus_number(2);
            }
            XincoCoreUserJpaController controller = new XincoCoreUserJpaController();
            if (getId() > 0) {
                if (isChange()) {
                    setChangerID(getId());
                    ts = new Timestamp(System.currentTimeMillis());
                    setLastModified(ts);
                    setChange(false);
                } else {
                    ts = getLastModified();
                }
                setLastModified(ts);
                //Sometimes password got re-hashed
                String password = "";
                if (hashPassword) {
                    password = MD5.encrypt(getUserpassword().replaceAll("'", "\\\\'"));
                } else {
                    password = getUserpassword().replaceAll("'", "\\\\'");
                }
                com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu =
                        controller.findXincoCoreUser(getId());
                xcu.setAttempts(getAttempts());
                xcu.setEmail(getEmail().replaceAll("'", "\\\\'"));
                xcu.setFirstname(getFirstname().replaceAll("'", "\\\\'"));
                xcu.setLastModified(getLastModified());
                xcu.setName(getName().replaceAll("'", "\\\\'"));
                xcu.setStatusNumber(getStatus_number());
                xcu.setUsername(getUsername().replaceAll("'", "\\\\'"));
                xcu.setUserpassword(password);
                xcu.setModificationReason(getReason() == null ? "audit.general.modified" : getReason());
                xcu.setModifierId(getChangerID());
                xcu.setModificationTime(new Timestamp(new Date().getTime()));
                controller.edit(xcu);
            } else {
                ts = new Timestamp(System.currentTimeMillis());
                com.bluecubs.xinco.core.server.persistence.XincoCoreUser xcu =
                        new com.bluecubs.xinco.core.server.persistence.XincoCoreUser(
                        getUsername().replaceAll("'", "\\\\'"), getUserpassword(), getName().replaceAll("'", "\\\\'"),
                        getFirstname().replaceAll("'", "\\\\'"), getEmail().replaceAll("'", "\\\\'"),
                        getStatus_number(), getAttempts(), getLastModified());
                xcu.setModificationReason(getReason());
                xcu.setModifierId(getChangerID());
                xcu.setModificationTime(new Timestamp(new Date().getTime()));
                controller.create(xcu);
                setId(xcu.getId());
            }
            if (isWriteGroups()) {
                writeXincoCoreGroups();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException();
        }
        setChange(false);
        setWriteGroups(false);
        setReason("");
        return getId();
    }

//create complete list of users
    public static Vector getXincoCoreUsers() {
        Vector coreUsers = new Vector();
        try {
            result = XincoDBManager.createdQuery("Select x from XincoCoreUser x order by x.username");
            for (Object o : result) {
                coreUsers.addElement(new XincoCoreUserServer((com.bluecubs.xinco.core.server.persistence.XincoCoreUser) o));
            }
        } catch (Exception e) {
            coreUsers.removeAllElements();
        }
        return coreUsers;
    }

    public boolean isHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(boolean hashPassword) {
        this.hashPassword = hashPassword;
    }

    public boolean isIncreaseAttempts() {
        return increaseAttempts;
    }

    public void setIncreaseAttempts(boolean increaseAttempts) {
        this.increaseAttempts = increaseAttempts;
    }

    public boolean isPasswordUsable(String newPass, boolean hash) {
        int id = 0;
        boolean passwordIsUsable = true;
        try {
            /*Bug fix: The password was only verified against past passwords not current password.
             *The current passwords is not usable after the first change when it was added to the
             *audit trail table.
             */
            //Now check if password is not the same as the current password
            result = XincoDBManager.createdQuery("Select x from XincoCoreUser x where x.id=" + getId()
                    + " and x.userpassword='" + (hash ? MD5.encrypt(newPass) : newPass) + "'");
            if (result.size() > 0) {
                return false;
            }
            //Here we'll catch if the password have been used in the unusable period (use id in case the username was modified)
            result = XincoDBManager.createdQuery("Select x from XincoCoreUser x where x.id=" + getId());
            id = ((com.bluecubs.xinco.core.server.persistence.XincoCoreUser) result.get(0)).getId();
            result = XincoDBManager.createdQuery("Select x from XincoCoreUserT x where x.id=" + id
                    + " and x.userpassword='" + (hash ? MD5.encrypt(newPass) : newPass) + "'");
            for (Object o : result) {
                //Now check the aging
                XincoCoreUserT user = (XincoCoreUserT) o;
                long diff = System.currentTimeMillis() - user.getLastModified().getTime();
                if (diff / (1000 * 60 * 60 * 24) > Integer.parseInt(settings.getString("password.unusable_period"))) {
                    return false;
                }
            }
            //---------------------------
        } catch (XincoException ex) {
            ex.printStackTrace();
            passwordIsUsable = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            passwordIsUsable = false;
        }
        return passwordIsUsable;
    }

    public boolean isPasswordUsable(String newPass) {
        return isPasswordUsable(newPass, true);
    }
}
