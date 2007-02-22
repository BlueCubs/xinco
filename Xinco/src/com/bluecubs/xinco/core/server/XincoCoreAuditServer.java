/*
 * XincoCoreAuditServer.java
 *
 * Created on January 30, 2007, 8:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoCoreAudit;
import com.bluecubs.xinco.core.XincoException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author javydreamercsw
 */
public class XincoCoreAuditServer extends XincoCoreAudit{
    
    /**
     * Creates a new instance of XincoCoreAuditServer
     */
    public XincoCoreAuditServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            
            Statement stmt = DBM.con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_audit WHERE id=" + attrID);
            
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setSchedule_id(rs.getInt("id"));
                setData_id(rs.getInt("xinco_core_data_id"));
                setSchedule_type_id(rs.getInt("schedule_type_id"));
                setScheduled_date(new Timestamp(rs.getDate("scheduled_date").getTime()));
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
    
    public XincoCoreAuditServer(int s_id,int d_id,int s_type_id,Timestamp s_date) throws XincoException{
        setSchedule_id(s_id);
        setData_id(d_id);
        setSchedule_type_id(s_type_id);
        setScheduled_date(s_date);
        try {
            write2DB(new XincoDBManager());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public int write2DB(XincoDBManager DBM) throws XincoException {
        
        try {
            XincoCoreAuditTrailManager audit= new XincoCoreAuditTrailManager();
            
            if (getSchedule_id() > 0) {
                Statement stmt = DBM.con.createStatement();
                stmt.executeUpdate("UPDATE xinco_audit SET id=" + getSchedule_id() +
                        ", xinco_core_data_id=" + getData_id() + ", xinco_schedule_type_id=" + getSchedule_type_id() +
                        ", xinco_scheduled_date=" + getScheduled_date() +
                        " WHERE schedule_id=" + getSchedule_id());
                stmt.close();
                DBM.con.commit();
                audit.updateAuditTrail("xinco_schedule_audit",new String [] {"schedule_id ="+getSchedule_id()},
                        DBM,"audit.scheduledaudit.change",this.getChangerID());
            } else {
                setSchedule_id(DBM.getNewID("xinco_schedule_audit"));
                
                Statement stmt = DBM.con.createStatement();
                stmt.executeUpdate("INSERT INTO xinco_audit VALUES (" + getSchedule_id() +
                        ", " + getData_id() + ", " + getSchedule_type_id() + ", " + getScheduled_date() +")");
                stmt.close();
                DBM.con.commit();
                audit.updateAuditTrail("xinco_audit",new String [] {"id ="+getSchedule_id()},
                        DBM,"audit.general.create",this.getChangerID());
            }
            
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
