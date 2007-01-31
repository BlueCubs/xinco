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
    }
    
    public XincoCoreAuditDataScheduleServer(int s_id,int d_id,int s_type_id,Timestamp s_date,boolean c) {
        setSchedule_id(s_id);
        setData_id(d_id);
        setSchedule_type_id(s_type_id);
        setScheduled_date(s_date);
        setCompleted(c);
    }
}
