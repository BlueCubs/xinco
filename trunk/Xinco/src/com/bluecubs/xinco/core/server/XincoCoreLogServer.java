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
 * Name:            XincoCoreLogServer
 *
 * Description:     log
 *
 * Original Author: Alexander Manes
 * Date:            2004
 *
 * Modifications:
 *
 * Who?             When?             What?
 * -                -                 -
 *
 *************************************************************
 */

/**
 *  Op Codes 
 *  2. Modification
 *  3. Checkout
 *  4. Checkout undone
 *  5. Checkin
 *  6. Publish
 *  7. Lock
 *  9. Comment
 *  10.Add audit data 
 *  11.Edit audit data
 */
package com.bluecubs.xinco.core.server;

import java.util.Vector;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.*;

import com.bluecubs.xinco.core.*;
import java.util.ResourceBundle;

public class XincoCoreLogServer extends XincoCoreLog {
    private XincoCoreUser user;
    //create single log object for data structures
    public XincoCoreLogServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_log WHERE id=" + attrID);
            
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setXinco_core_data_id(rs.getInt("xinco_core_data_id"));
                setXinco_core_user_id(rs.getInt("xinco_core_user_id"));
                setOp_code(rs.getInt("op_code"));
                setOp_datetime(new GregorianCalendar());
                getOp_datetime().setTime(rs.getTimestamp("op_datetime"));
                setOp_description(rs.getString("op_description"));
                setVersion(new XincoVersion());
                getVersion().setVersion_high(rs.getInt("version_high"));
                getVersion().setVersion_mid(rs.getInt("version_mid"));
                getVersion().setVersion_low(rs.getInt("version_low"));
                getVersion().setVersion_postfix(rs.getString("version_postfix"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
            stmt.close();
        } catch (Exception e) {
            throw new XincoException();
        }
        
    }
    
    public void setUser(XincoCoreUserServer user){
        this.user=user;
    }
    
    //create single log object for data structures
    public XincoCoreLogServer(int attrID, int attrCDID, int attrUID, int attrOC, Calendar attrODT,  String attrOD, int attrVH, int attrVM, int attrVL, String attrVP) throws XincoException {
        setId(attrID);
        setXinco_core_data_id(attrCDID);
        setXinco_core_user_id(attrUID);
        setOp_code(attrOC);
        setOp_datetime(attrODT);
        setOp_description(attrOD);
        setVersion(new XincoVersion());
        getVersion().setVersion_high(attrVH);
        getVersion().setVersion_mid(attrVM);
        getVersion().setVersion_low(attrVL);
        getVersion().setVersion_postfix(attrVP);
    }
    
    //write to db
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            if (getId() > 0) {
                Statement stmt = DBM.getConnection().createStatement();
                XincoCoreAuditTrail audit= new XincoCoreAuditTrail();
                ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
                audit.updateAuditTrail("xinco_core_log",new String [] {"id ="+getId()},
                        DBM,xerb.getString("audit.log.change"),this.getChangerID());
                stmt.executeUpdate("UPDATE xinco_core_log SET xinco_core_data_id=" + getXinco_core_data_id() + ", xinco_core_user_id=" + getXinco_core_user_id() + ", op_code=" + getOp_code() + ", op_datetime=now(), op_description='" + getOp_description().replaceAll("'","\\\\'") + "', version_high=" + getVersion().getVersion_high() + ", version_mid=" + getVersion().getVersion_mid() + ", version_low=" + getVersion().getVersion_low() + ", version_postfix='" + getVersion().getVersion_postfix().replaceAll("'","\\\\'") + "' WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_log"));
                Statement stmt = DBM.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO xinco_core_log VALUES (" + getId() + ", " + getXinco_core_data_id() + ", " + getXinco_core_user_id() + ", " + getOp_code() + ", now(), '" + getOp_description().replaceAll("'","\\\\'") + "', " + getVersion().getVersion_high() + ", " + getVersion().getVersion_mid() + ", " + getVersion().getVersion_low() + ", '" + getVersion().getVersion_postfix().replaceAll("'","\\\\'") + "')");
                stmt.close();
            }
            DBM.getConnection().commit();
        } catch (Exception e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return getId();
    }
    
    //create complete log list for data
    public static Vector getXincoCoreLogs(int attrID, XincoDBManager DBM) {
        Vector core_log = new Vector();
        GregorianCalendar cal = new GregorianCalendar();
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_core_log WHERE xinco_core_data_id=" + attrID);
            while (rs.next()) {
                cal = new GregorianCalendar();
                cal.setTime( rs.getTimestamp("op_datetime"));
                core_log.addElement(new XincoCoreLogServer(rs.getInt("id"), rs.getInt("xinco_core_data_id"), rs.getInt("xinco_core_user_id"), rs.getInt("op_code"), cal, rs.getString("op_description"), rs.getInt("version_high"), rs.getInt("version_mid"), rs.getInt("version_low"), rs.getString("version_postfix")));
            }
            stmt.close();
        } catch (Exception e) {
            core_log.removeAllElements();
        }
        return core_log;
    }
}
