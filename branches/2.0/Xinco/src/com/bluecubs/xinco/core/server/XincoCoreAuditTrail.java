/*
 * XincoCoreAuditServer.java
 *
 * Created on November 28, 2006, 9:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.core.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author ortizbj
 */
public class XincoCoreAuditTrail {
    /** Creates a new instance of XincoCoreAuditServer */
    public XincoCoreAuditTrail() {
    }
    
    public void updateAuditTrail(String table,String [] keys, XincoDBManager DBM,String reason,int id){
        try {
            //"Copy and Paste" the original record in the audit tables
            String where="";
            for(int i=0;i<keys.length;i++){
                where+=keys[i];
                if(i<keys.length-1)
                    where+=" and ";
            }
            Statement stmt = DBM.getConnection().createStatement();
            int record_ID=0;
            String sql="select * from "+table+" where "+where;
            ResultSet rs = stmt.executeQuery(sql);
            try {
                record_ID=DBM.getNewID("xinco_core_user_modified_record");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(rs.next()) {
                sql="insert into "+table+"_t values('"+record_ID+"', ";
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++){
                    if(rs.getString(i)==null)
                        sql+=rs.getString(i);
                    else
                        sql+="'"+rs.getString(i)+"'";
                    if(i<rs.getMetaData().getColumnCount())
                        sql+=", ";
                    else
                        sql+=")";
                }
                stmt.executeUpdate(sql);
            }
            sql="insert into xinco_core_user_modified_record (id, record_id, mod_Time, " +
                    "mod_Reason) values ("+id+", "+record_ID+", '"+
                    new Timestamp(System.currentTimeMillis())+"', '"+reason+"')";
            stmt.executeUpdate(sql);
            //System.out.println(sql);
            DBM.getConnection().commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
