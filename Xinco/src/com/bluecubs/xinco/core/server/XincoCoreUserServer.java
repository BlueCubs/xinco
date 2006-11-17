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
 *
 *************************************************************
 */

package com.bluecubs.xinco.core.server;

import java.sql.*;
import java.util.Vector;

import com.bluecubs.xinco.core.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class XincoCoreUserServer extends XincoCoreUser {
    private String sql;
    private boolean hashPassword = true;
    private boolean increaseAttempts = false;
    private ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
    private java.util.GregorianCalendar lastModified;
    private int attempts;
    
    private void fillXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        setXinco_core_groups(new Vector());
        try {
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
            while (rs.next()) {
                getXinco_core_groups().addElement(new XincoCoreGroupServer(rs.getInt("xinco_core_group_id"), DBM));
            }
            stmt.close();
        } catch (Exception e) {
            getXinco_core_groups().removeAllElements();
            throw new XincoException();
        }
    }
    
    private void writeXincoCoreGroups(XincoDBManager DBM) throws XincoException {
        Statement stmt;
        String sql=null;
        int i=-1;
        try {
            stmt = DBM.con.createStatement();
            stmt.executeUpdate("DELETE FROM xinco_core_user_has_xinco_core_group WHERE xinco_core_user_id=" + getId());
            stmt.close();
            for (i=0; i<getXinco_core_groups().size(); i++) {
                stmt = DBM.con.createStatement();
                sql="INSERT INTO xinco_core_user_has_xinco_core_group VALUES (" + getId() +
                        ", " + ((XincoCoreGroupServer)getXinco_core_groups().elementAt(i)).getId() +
                        ", " + 1 + ")";
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new XincoException();
        }
    }
    
    //create user object and login
    public XincoCoreUserServer(String attrUN, String attrUPW, XincoDBManager DBM) throws XincoException {
        Statement stmt = null;
        ResultSet rs = null;
        GregorianCalendar cal = null;
        try {
            stmt = DBM.con.createStatement();
            String sql="SELECT * FROM xinco_core_user WHERE username='" +
                    attrUN + "' AND userpassword=MD5('" + attrUPW + "')";// AND status_number=1";
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                System.out.println("Record found...");
                RowCount++;
                setId(rs.getInt("id"));
                setUsername(rs.getString("username"));
                //previously hashing the already hashed password
                hashPassword = true;
                setUserpassword(attrUPW);
                setName(rs.getString("name"));
                setFirstname(rs.getString("firstname"));
                setEmail(rs.getString("email"));
                int status=0;
                if(rs.getInt("status_number")!=2){
                    Calendar cal2 = GregorianCalendar.getInstance(),now= GregorianCalendar.getInstance();
                    cal2.setTime(rs.getTimestamp("last_modified"));
                    System.out.println(rs.getDate("last_modified"));
                    System.out.println(rs.getTimestamp("last_modified"));
                    long diffMillis = now.getTimeInMillis()-cal2.getTimeInMillis();
                    System.out.println(diffMillis);
                    long diffDays = diffMillis/(24*60*60*1000);
                    System.out.println(diffDays);
                    System.out.println(xerb.getString("password.aging"));
                    long age = Long.parseLong(xerb.getString("password.aging"));
                    System.out.println(age);
                    if(diffDays >= age){
                        status =2;
                        System.out.println("Password aged out...");
                    } else{
                        status=rs.getInt("status_number");
                        System.out.println("Password not aged out...");
                    }
                } 
                setStatus_number(status);
                //Reset login attempts after a successfull login.
                if(rs.getInt("status_number")!=2){
                    setAttempts(0);
                } else
                    setAttempts(rs.getInt("attempts"));
                cal = new GregorianCalendar();
                cal.setTime(rs.getTimestamp("last_modified"));
                setLastModified(cal);
                write2DB(DBM);
            }
            if (RowCount < 1) {
                sql="SELECT * FROM xinco_core_user WHERE username='" +
                        attrUN + "'";
                rs = stmt.executeQuery(sql);
                //The username is valid but wrong password. Increase the login attempts.
                if(rs.next()){
                    increaseAttempts = true;
                }
                System.out.println("Throwing exception...");
                throw new XincoException();
            }
            stmt.close();
            fillXincoCoreGroups(DBM);
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
                        attrUN + "' AND status_number=1";
                stmt = DBM.con.createStatement();
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
                    setAttempts(rs.getInt("attempts")+1);
                    cal = new GregorianCalendar();
                    cal.setTime(rs.getTimestamp("last_modified"));
                    setLastModified(cal);
                    write2DB(dbm);
                }
            } catch (XincoException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new XincoException();
        }
    }
    
//create user object for data structures
    public XincoCoreUserServer(int attrID, XincoDBManager DBM) throws XincoException {
        GregorianCalendar cal = null;
        try {
            Statement stmt = DBM.con.createStatement();
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
                cal = new GregorianCalendar();
                cal.setTime(rs.getDate("last_modified"));
                setLastModified(cal);
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
            String attrFN, String attrE, int attrSN,int attrAN, java.util.GregorianCalendar attrTS,
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
    public void setLastModified(java.util.GregorianCalendar lastModified) {
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
        try {
            Statement stmt;
            if (getId() > 0) {
                stmt = DBM.con.createStatement();
                if(isChange())
                    updateAuditTrail("xinco_core_user",new String [] {"id ="+getId()},DBM,getReason());
                xerb= ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
                //Increase login attempts
                if(increaseAttempts){
                    setAttempts(getAttempts()+1);
                    increaseAttempts = false;
                }
                //Lock account if needed. Can't lock main admin.
                if(getAttempts()>Integer.parseInt(xerb.getString("password.attempts")) &&
                        getId() > 1){
                    setStatus_number(2);
                }
                //Sometimes password got re-hashed
                String password="";
                if(hashPassword)
                    password="userpassword=MD5('" +
                            getUserpassword().replaceAll("'","\\\\'") + "')";
                else
                    password="userpassword='" +
                            getUserpassword().replaceAll("'","\\\\'") + "'";
                
                sql="UPDATE xinco_core_user SET username='" +
                        getUsername().replaceAll("'","\\\\'") + "', "+password+", name='" +
                        getName().replaceAll("'","\\\\'") + "', firstname='" +
                        getFirstname().replaceAll("'","\\\\'") + "', email='" +
                        getEmail().replaceAll("'","\\\\'") + "', status_number=" +
                        getStatus_number() + ", attempts="+getAttempts() +
                        ", last_modified='"+
                        ((GregorianCalendar)getLastModified()).get(GregorianCalendar.YEAR)+
                        "-"+(((GregorianCalendar)getLastModified()).get(GregorianCalendar.MONTH)+1)+
                        "-"+((GregorianCalendar)getLastModified()).get(GregorianCalendar.DAY_OF_MONTH)+"'"+
                        " WHERE id=" + getId();
                System.out.println(sql);
                stmt.executeUpdate(sql);
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_user"));
                stmt = DBM.con.createStatement();
                sql="INSERT INTO xinco_core_user VALUES (" + getId() +
                        ", '" + getUsername().replaceAll("'","\\\\'") +
                        "', MD5('" + getUserpassword().replaceAll("'","\\\\'") +
                        "'), '" + getName().replaceAll("'","\\\\'") + "', '" +
                        getFirstname().replaceAll("'","\\\\'") + "', '" +
                        getEmail().replaceAll("'","\\\\'") + "', " +
                        getStatus_number() +", "+ getAttempts() +", '"+((GregorianCalendar)getLastModified()).get(GregorianCalendar.YEAR)+"-"+(((GregorianCalendar)getLastModified()).get(GregorianCalendar.MONTH)+1)+"-"+((GregorianCalendar)getLastModified()).get(GregorianCalendar.DAY_OF_MONTH)+"')";
                stmt.executeUpdate(sql);
                stmt.close();
            }
            if(isWriteGroups())
                writeXincoCoreGroups(DBM);
            DBM.con.commit();
        } catch (Exception e) {
            try {
                DBM.con.rollback();
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
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_user ORDER BY username");
            while (rs.next()) {
                cal = new GregorianCalendar();
                cal.setTime(rs.getDate("last_modified"));
                coreUsers.addElement(new XincoCoreUserServer(rs.getInt("id"),
                        rs.getString("username"), rs.getString("userpassword"),
                        rs.getString("name"), rs.getString("firstname"),
                        rs.getString("email"), rs.getInt("status_number"),
                        rs.getInt("attempts"), cal, DBM));
            }
            stmt.close();
        } catch (Exception e) {
            coreUsers.removeAllElements();
        }
        return coreUsers;
    }
    
    public void updateAuditTrail(String table,String [] keys, XincoDBManager DBM,String reason){
        try {
            //"Copy and Paste" the original record in the audit tables
            String where="";
            for(int i=0;i<keys.length;i++){
                where+=keys[i];
                if(i<keys.length-1)
                    where+=" and ";
            }
            Statement stmt = DBM.con.createStatement();
            int record_ID=0;
            String sql="select * from "+table+" where "+where;
            ResultSet rs = stmt.executeQuery(sql);
            try {
                record_ID=DBM.getNewID("xinco_core_user_modified_record");
                sql="insert into "+table+"_t values("+record_ID+", ";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(rs.next()) {
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                    sql+="'"+rs.getString(i)+"'";
                    if(i<rs.getMetaData().getColumnCount())
                        sql+=", ";
                    else
                        sql+=")";
                }
            }
            stmt.executeUpdate(sql);
            sql="insert into `xinco_core_user_modified_record` (`id`, `record_id`, `mod_Time`, " +
                    "`mod_Reason`) values ("+getId()+", "+record_ID+", '"+
                    new Timestamp(System.currentTimeMillis())+"', '"+reason+"')";
            stmt.executeUpdate(sql);
            DBM.con.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
}
