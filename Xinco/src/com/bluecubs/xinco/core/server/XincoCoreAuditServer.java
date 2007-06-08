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
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author javydreamercsw
 */
public class XincoCoreAuditServer extends XincoCoreAudit{
    private int changerID;
    private XincoCoreAuditTrail audit=null;
    /**
     * Creates a new instance of XincoCoreAuditServer
     */
    public XincoCoreAuditServer(int attrID, XincoDBManager DBM) throws XincoException {
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM xinco_audit WHERE id=" + attrID);
            //throw exception if no result found
            while (rs.next()) {
                setSchedule_id(rs.getInt("id"));
                setData_id(rs.getInt("xinco_core_data_id"));
                setSchedule_type_id(rs.getInt("xinco_audit_type_id"));
                setScheduled_date(new Timestamp(rs.getDate("scheduled_date").getTime()));
                if(rs.getDate("completion_date")==null)
                    setCompletion_date(null);
                else
                    setCompletion_date(new Timestamp(rs.getDate("completion_date").getTime()));
                setCompletedBy(rs.getInt("xinco_core_user_id"));
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new XincoException();
        }
    }
    
    public XincoCoreAuditServer(int schedule_id,
            int data_id,
            int schedule_type_id,
            java.util.Date scheduled_date,
            java.util.Date completion_date,
            int completedBy) throws XincoException{
        setSchedule_id(schedule_id);
        setData_id(data_id);
        setSchedule_type_id(schedule_type_id);
        setScheduled_date(scheduled_date);
        setCompletion_date(completion_date);
        setCompletedBy(completedBy);
    }
    
    public int write2DB(XincoDBManager DBM) throws XincoException {
        try {
            if(audit==null)
                audit= new XincoCoreAuditTrail();
            if (getSchedule_id() > 0) {
                System.out.println("Updating xinco audit");
                Statement stmt = DBM.getConnection().createStatement();
                stmt.executeUpdate("UPDATE xinco_audit SET id=" + getSchedule_id() +
                        ", xinco_core_data_id=" + getData_id() + ", xinco_audit_type_id=" + getSchedule_type_id() +
                        ", scheduled_date='" + getScheduled_date() + "', completion_date ='"+getCompletion_date()+
                        "' WHERE schedule_id=" + getSchedule_id());
                stmt.close();
                DBM.getConnection().commit();
                audit.updateAuditTrail("xinco_audit",new String [] {"id ="+getSchedule_id()},
                        DBM,"audit.scheduledaudit.change",this.getChangerID());
                System.out.println("Done!");
            } else {
                System.out.println("Creating xinco audit");
                setSchedule_id(DBM.getNewID("xinco_audit"));
                Statement stmt = DBM.getConnection().createStatement();
                if(getCompletedBy()>0){
                    java.sql.Date date= new java.sql.Date(getScheduled_date().getTime());
                    stmt.executeUpdate("INSERT INTO xinco_audit VALUES (" + getSchedule_id() +
                            ", " + getSchedule_type_id() + ", "+getData_id()+", " + getData_id()+
                            ", '" +date + "', "+getCompletedBy()+")");
                } else{
                    String sql="INSERT INTO xinco_audit VALUES (" + getSchedule_id() +
                            ", " + getSchedule_type_id() + ", "+getData_id()+", " + getData_id()+
                            ", '"+new Timestamp(getScheduled_date().getTime())+"', null)";
                    System.out.println(sql);
                    stmt.executeUpdate(sql);
                }
                stmt.close();
                DBM.getConnection().commit();
                audit.updateAuditTrail("xinco_audit",new String [] {"id ="+getSchedule_id()},
                        DBM,"audit.general.create",this.getChangerID());
                System.out.println("Done!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
        return getSchedule_id();
    }
    
    public int getChangerID() {
        return changerID;
    }
    
    public void setChangerID(int changerID) {
        this.changerID = changerID;
    }
    
    public void deleteFromDB(XincoDBManager DBM) throws XincoException{
        try {
            if(audit==null)
                audit= new XincoCoreAuditTrail();
            audit.updateAuditTrail("xinco_audit",new String [] {"id ="+getSchedule_id()},
                    DBM,"audit.general.delete",this.getChangerID());
            Statement stmt = DBM.getConnection().createStatement();
            stmt.executeUpdate("delete from xinco_audit where id ="+getSchedule_id());
            stmt.close();
            DBM.getConnection().commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new XincoException();
        }
    }
}
