/*
 * XincoCoreAuditDataScheduleServer.java
 *
 * Created on January 30, 2007, 8:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoCoreAuditDataSchedule;
import com.bluecubs.xinco.core.XincoException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author javydreamercsw
 */
public class XincoCoreAuditDataScheduleServer extends XincoCoreAuditDataSchedule{
    
    /** Creates a new instance of XincoCoreAuditDataScheduleServer */
    public XincoCoreAuditDataScheduleServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_schedule_audit WHERE schedule_id=" + attrID);
            
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setSchedule_id(rs.getInt("schedule_id"));
                setData_id(rs.getInt("xinco_core_data_id"));
                setSchedule_type_id(rs.getInt("schedule_type_id"));
                setScheduled_date(new Timestamp(rs.getDate("scheduled_date").getTime()));
                setCompleted(rs.getBoolean("schedule_completed"));
            }
            if (RowCount < 1) {
                throw new XincoException();
            }
            stmt.close();
            
        } catch (Exception e) {
            throw new XincoException();
        }
        try {
            write2DB(new XincoDBManager());
        } catch (XincoException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public XincoCoreAuditDataScheduleServer(int s_id,int d_id,int s_type_id,Timestamp s_date,boolean c) throws XincoException{
        setSchedule_id(s_id);
        setData_id(d_id);
        setSchedule_type_id(s_type_id);
        setScheduled_date(s_date);
        setCompleted(c);
        try {
            write2DB(new XincoDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public int write2DB(XincoDBManager DBM) throws XincoException {
        
        try {
            XincoCoreAuditServer audit= new XincoCoreAuditServer();
            
            if (getSchedule_id() > 0) {
                Statement stmt = DBM.con.createStatement();
                stmt.executeUpdate("UPDATE xinco_schedule_audit SET schedule_id=" + getSchedule_id() +
                        ", xinco_core_data_id=" + getData_id() + ", xinco_schedule_type_id=" + getSchedule_type_id() +
                        ", xinco_scheduled_date=" + getScheduled_date() + ", schedule_completed=" + isCompleted() +
                        " WHERE schedule_id=" + getSchedule_id());
                audit.updateAuditTrail("xinco_schedule_audit",new String [] {"schedule_id ="+getSchedule_id()},
                        DBM,"audit.scheduledaudit.change",this.getIdChanger());
                stmt.close();
            } else {
                setSchedule_id(DBM.getNewID("xinco_schedule_audit"));
                
                Statement stmt = DBM.con.createStatement();
                stmt.executeUpdate("INSERT INTO xinco_schedule_audit VALUES (" + getSchedule_id() +
                        ", " + getData_id() + ", " + getSchedule_type_id() + ", " + getScheduled_date() +
                        ", " + isCompleted()+")");
                audit.updateAuditTrail("xinco_schedule_audit",new String [] {"schedule_id ="+getSchedule_id()},
                        DBM,"audit.general.create",this.getIdChanger());
                stmt.close();
            }
            
            DBM.con.commit();
            
        } catch (Exception e) {
            try {
                DBM.con.rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        
        return getSchedule_id();
        
    }
}
