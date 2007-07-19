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

import java.sql.*;
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import com.bluecubs.xinco.general.AuditTrail;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

//Status list (in DB)
//1 = unlocked
//2 = locked
//3 = aged password
//Temporary statuses
//-1 = aged password modified, ready to turn unlocked

public class XincoCoreUserServer extends XincoCoreUser {
    private String sql;
    private boolean hashPassword = true;
    private boolean increaseAttempts = false;
    private ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    private XincoSettingServer settings=null;
    private java.sql.Timestamp lastModified;
    private int attempts;
    private AuditTrail audit= new AuditTrail();
    private ResultSet rs=null;
    
    private void fillXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        setXinco_core_groups(new Vector());
        try {
            Statement stmt = DBM.getConnection().createStatement();
            String sql="SELECT * FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId();
            if(getSettings().getSetting("general.setting.enable.lockidle").isBool_value())
                System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                getXinco_core_groups().addElement(new XincoCoreGroupServer(rs.getInt("xinco_core_group_id"), DBM));
            }
            stmt.close();
        } catch (Exception e) {
            getXinco_core_groups().removeAllElements();
            if(DBM.getSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                e.printStackTrace();
            throw new XincoException();
        }
    }
    
    private void writeXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        Statement stmt;
        String sql=null;
        ResultSet rs2=null;
        boolean found=false;
        int i=-1;
        int place=0;
        try {
            stmt = DBM.getConnection().createStatement();
            rs=stmt.executeQuery("select * from xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
            while(rs.next()){
                stmt = DBM.getConnection().createStatement();
                for(int j =0;j<getXinco_core_groups().size();j++){
                    if(((XincoCoreGroupServer)getXinco_core_groups().elementAt(j)).getId()==rs.getInt("xinco_core_group_id")){
                        found=true;
                        place=j;
                    }
                }
                //The record exists
                if(found){
                    //Change detected
                    if(getStatus_number()!=rs.getInt("status_number")){
                        stmt.executeUpdate("update xinco_core_user_has_xinco_core_group set status_number="+
                                getStatus_number()+" where xinco_core_user_id="+ getId()+", xinco_core_group_id="+
                                ((XincoCoreGroupServer)getXinco_core_groups().elementAt(place)).getId());
                        audit.updateAuditTrail("xinco_core_user_has_xinco_core_group",new String [] {"xinco_core_user_id ="+getId(),
                        "xinco_core_group_id="+((XincoCoreGroupServer)getXinco_core_groups().elementAt(place)).getId()},
                                DBM,"audit.general.modified",getChangerID());
                    }
                }
                //New record
                else{
                    sql="INSERT INTO xinco_core_user_has_xinco_core_group VALUES (" + getId() +
                            ", " + ((XincoCoreGroupServer)getXinco_core_groups().elementAt(i)).getId() +
                            ", " + 1 + ")";
                    stmt.executeUpdate(sql);
                    audit.updateAuditTrail("xinco_core_user_has_xinco_core_group",new String [] {"xinco_core_user_id ="+getId(),
                    "xinco_core_group_id="+((XincoCoreGroupServer)getXinco_core_groups().elementAt(place)).getId()},
                            DBM,"audit.general.created",getChangerID());
                }
                found = false;
                place=0;
            }
            stmt.close();
//            stmt.executeUpdate("DELETE FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
//            stmt.close();
//            for (i=0; i<getXinco_core_groups().size(); i++) {
//                stmt = DBM.con.createStatement();
//                sql="INSERT INTO xinco_core_user_has_xinco_core_group VALUES (" + getId() +
//                        ", " + ((XincoCoreGroupServer)getXinco_core_groups().elementAt(i)).getId() +
//                        ", " + 1 + ")";
//                stmt.executeUpdate(sql);
//
//                stmt.close();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException();
        }
    }
    
    //create user object and login
    public XincoCoreUserServer(String attrUN, String attrUPW, XincoDBManager DBM) throws XincoException {
        Statement stmt = null;
        ResultSet rs = null;
        GregorianCalendar cal = null;
        getSettings();
        try {
            stmt = DBM.getConnection().createStatement();
            String sql="SELECT * FROM xinco_core_user WHERE username='" +
                    attrUN + "' AND userpassword=MD5('" + attrUPW + "') AND status_number <> 2";
            if(DBM.getSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            rs = stmt.executeQuery(sql);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setUsername(rs.getString("username"));
                //previously hashing the already hashed password
                hashPassword = true;
                setUserpassword(attrUPW);
                setName(rs.getString("name"));
                setFirstname(rs.getString("firstname"));
                setEmail(rs.getString("email"));
                setLastModified(rs.getTimestamp("last_modified"));
                int status=0;
                if(rs.getInt("status_number")!=2){
                    Calendar cal2 = GregorianCalendar.getInstance(),now= GregorianCalendar.getInstance();
                    cal2.setTime(rs.getTimestamp("last_modified"));
                    long diffMillis = now.getTimeInMillis()-cal2.getTimeInMillis();
                    long diffDays = diffMillis/(24*60*60*1000);
                    long age = getSettings().getSetting("password.aging").getInt_value();
                    if(diffDays >= age){
                        status=3;
                    } else{
                        status=1;
                    }
                    setAttempts(0);
                } else
                    setAttempts(rs.getInt("attempts"));
                setStatus_number(status);
                write2DB(DBM);
            }
            if (RowCount < 1) {
                sql="SELECT * FROM xinco_core_user WHERE username='" +
                        attrUN + "'";
                if(DBM.getSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                rs = stmt.executeQuery(sql);
                //The username is valid but wrong password. Increase the login attempts.
                if(rs.next()){
                    increaseAttempts = true;
                    setAttempts(rs.getInt("attempts"));
                }
                throw new XincoException();
            }
            fillXincoCoreGroups(DBM);
            stmt.close();
        } catch (Exception e) {
            if (getXinco_core_groups() != null) {
                getXinco_core_groups().removeAllElements();
            }
            try {
                XincoDBManager dbm=null;
                try {
                    dbm = new XincoDBManager();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String sql="SELECT * FROM xinco_core_user WHERE username='" +
                        attrUN + "' AND status_number <> 2";
                stmt = DBM.getConnection().createStatement();
                if(DBM.getSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                rs = stmt.executeQuery(sql);
                //increase number of attempts
                if(rs.next()){
                    setId(rs.getInt("id"));
                    setUsername(rs.getString("username"));
                    //Don't rehash the pasword!
                    hashPassword = false;
                    setUserpassword(rs.getString("userpassword"));
                    setName(rs.getString("name"));
                    setFirstname(rs.getString("firstname"));
                    setEmail(rs.getString("email"));
                    setStatus_number(rs.getInt("status_number"));
                    //Increase attempts after a unsuccessfull login.
                    setIncreaseAttempts(true);
                    setLastModified(rs.getTimestamp("last_modified"));
                    setChange(false);
                    write2DB(dbm);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw new XincoException();
        }
    }
    
//create user object for data structures
    public XincoCoreUserServer(int attrID, XincoDBManager DBM) throws XincoException {
        GregorianCalendar cal = null;
        getSettings();
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user WHERE id=" + attrID);
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
            stmt.close();
            fillXincoCoreGroups(DBM);
        } catch (Exception e) {
            getXinco_core_groups().removeAllElements();
            throw new XincoException();
        }
    }
    
//create user object
    public XincoCoreUserServer(int attrID, String attrUN, String attrUPW, String attrN,
            String attrFN, String attrE, int attrSN,int attrAN, java.sql.Timestamp attrTS,
            XincoDBManager DBM) throws XincoException {
        getSettings();
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
    public java.lang.Object getLastModified() {
        return lastModified;
    }
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        String sql="";
        xerb= ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
        Timestamp ts=null;
        boolean isNew=false;
        try {
            Statement stmt;
            if(getStatus_number()==4){
                //Changed from aged out to password changed. Clear status
                setStatus_number(1);
                setAttempts(0);
                setChange(true);
                setReason("audit.user.account.aged");
                ts= new Timestamp(System.currentTimeMillis());
            }
            //Increase login attempts
            if(increaseAttempts){
                setAttempts(getAttempts()+1);
                increaseAttempts = false;
            }
            //Lock account if needed. Can't lock main admin.
            if(getAttempts()>getSettings().getSetting("password.aging").getInt_value() &&
                    getId() > 1){
                setStatus_number(2);
            }
            if (getId() > 0) {
                stmt = DBM.getConnection().createStatement();
                if(isChange()){
                    audit.updateAuditTrail("xinco_core_user",new String [] {"id ="+getId()},
                            DBM,getReason(),getChangerID());
                    ts= new Timestamp(System.currentTimeMillis());
                    setLastModified(ts);
                    setChange(false);
                } else
                    ts= (Timestamp)getLastModified();
                setLastModified(ts);
                //Sometimes password got re-hashed
                String password="";
                if(hashPassword)
                    password="userpassword=MD5('" +
                            getUserpassword().replaceAll("'","\\\\'") + "')";
                else
                    password="userpassword='" +
                            getUserpassword().replaceAll("'","\\\\'") + "'";
                stmt.executeUpdate("UPDATE xinco_core_user SET username='" +
                        getUsername().replaceAll("'","\\\\'") + "', "+password+", name='" +
                        getName().replaceAll("'","\\\\'") + "', firstname='" +
                        getFirstname().replaceAll("'","\\\\'") + "', email='" +
                        getEmail().replaceAll("'","\\\\'") + "', status_number=" +
                        getStatus_number() + ", attempts="+getAttempts() +
                        ", last_modified='"+getLastModified()+"'"+
                        " WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_user"));
                isNew=true;
                ts= new Timestamp(System.currentTimeMillis());
                stmt = DBM.getConnection().createStatement();
                sql="INSERT INTO xinco_core_user VALUES (" + getId() +
                        ", '" + getUsername().replaceAll("'","\\\\'") +
                        "', MD5('" + getUserpassword().replaceAll("'","\\\\'") +
                        "'), '" + getName().replaceAll("'","\\\\'") + "', '" +
                        getFirstname().replaceAll("'","\\\\'") + "', '" +
                        getEmail().replaceAll("'","\\\\'") + "', " +
                        getStatus_number() +", "+ getAttempts() +", '"+ts.toString()+"')";
                stmt.executeUpdate(sql);
                stmt.close();
            }
            if(isWriteGroups())
                writeXincoCoreGroups(DBM);
            DBM.getConnection().commit();
            if(isNew){
                audit.updateAuditTrail("xinco_core_user",new String [] {"id ="+getId()},
                        DBM,getReason(),getChangerID());
                isNew=false;
            }
        } catch (Exception e) {
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
    
//create complete list of users
    public static Vector getXincoCoreUsers(XincoDBManager DBM) {
        Vector coreUsers = new Vector();
        GregorianCalendar cal = null;
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user ORDER BY username");
            while (rs.next()) {
                coreUsers.addElement(new XincoCoreUserServer(rs.getInt("id"),
                        rs.getString("username"), rs.getString("userpassword"),
                        rs.getString("name"), rs.getString("firstname"),
                        rs.getString("email"), rs.getInt("status_number"),
                        rs.getInt("attempts"), rs.getTimestamp("last_modified"), DBM));
            }
            stmt.close();
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
    
    public boolean isPasswordUsable(String newPass){
        ResultSet rs=null;
        String sql=null;
        int id=0;
        boolean passwordIsUsable=false;
        try {
            XincoDBManager DBM=null;
            try {
                DBM = new XincoDBManager();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Statement stmt=DBM.getConnection().createStatement();
            sql="select id from xinco_core_user where username='"+getUsername()+"'";
            rs=stmt.executeQuery(sql);
            rs.next();
            id = rs.getInt(1);
            rs=stmt.executeQuery("select userpassword from xinco_core_user_t where id=" +
                    id+" and DATEDIFF(NOW(),last_modified) <= "+
                    getSettings().getSetting("password.aging").getInt_value() + " and MD5('"+
                    newPass+"') = userpassword");
            //Here we'll catch if the password have been used in the unusable period
            rs.next();
            rs.getString(1);
            //---------------------------
        } catch (SQLException ex) {
            passwordIsUsable=true;
        }
        return passwordIsUsable;
    }
    
    private XincoSettingServer getSettings(){
        if(settings==null)
            settings = new XincoSettingServer();
        return settings;
    }
}
