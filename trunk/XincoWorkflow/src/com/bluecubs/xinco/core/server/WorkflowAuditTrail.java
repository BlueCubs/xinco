/**
 *Copyright June 13, 2007 blueCubs.com
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
 * Name:            WorkflowAuditTrail.java
 *
 * Description:     WorkflowAuditTrail
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 13, 2007, 9:23 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
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
public class WorkflowAuditTrail {
    /**
     * Creates a new instance of WorkflowAuditTrail
     */
    public WorkflowAuditTrail() {
    }
    
    public void updateAuditTrail(String table,String [] keys, WorkflowDBManager DBM,String reason,int id){
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
                sql="insert into "+table+"_t values('"+record_ID+"', ";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(rs.next()) {
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
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                stmt.executeUpdate(sql);
                sql="insert into xinco_core_user_modified_record (id, record_id, mod_Time, " +
                        "mod_Reason) values ("+id+", "+record_ID+", '"+
                        new Timestamp(System.currentTimeMillis())+"', '"+reason+"')";
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                stmt.executeUpdate(sql);
            }
            DBM.getConnection().commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
            return;
        }
    }
}

