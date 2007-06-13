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
 * Name:            WorkflowLogServer.java
 *
 * Description:     WorkflowLogServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 13, 2007, 8:36 AM
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *************************************************************
 */

package com.bluecubs.xinco.workflow.server;

import com.bluecubs.xinco.core.server.WorkflowAuditTrail;
import com.bluecubs.xinco.core.server.WorkflowDBManager;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.workflow.WorkflowException;
import com.bluecubs.xinco.workflow.WorkflowLog;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Vector;

public class WorkflowLogServer extends WorkflowLog{
    
    /** Creates a new instance of WorkflowLogServer */
    public WorkflowLogServer(int id, WorkflowDBManager DBM) throws WorkflowException {
        try {
            ResultSet rs = DBM.getStatement().executeQuery("SELECT * FROM workflow_log WHERE id=" + id);
            
            //throw exception if no result found
            int RowCount = 0;
            while (rs.next()) {
                RowCount++;
                setId(rs.getInt("id"));
                setWorkflow_instance_id(rs.getInt("workflow_instance_id"));
                setWorkflow_template_id(rs.getInt("workflow_template_id"));
                setOp_code(rs.getInt("op_code"));
                setOp_datetime(new GregorianCalendar());
                getOp_datetime().setTime(rs.getTimestamp("op_datetime"));
                setOp_description(rs.getString("op_description"));
            }
            if (RowCount < 1) {
                throw new WorkflowException();
            }
        } catch (Exception e) {
            throw new WorkflowException();
        }
    }
    
    public WorkflowLogServer(int id,int resource_id, int workflow_template_id, int workflow_instance_id,
            int op_code, java.util.Calendar op_datetime, java.lang.String op_description) {
        setId(id);
        setWorkflow_instance_id(workflow_instance_id);
        setWorkflow_template_id(workflow_template_id);
        setOp_code(op_code);
        setOp_datetime(new GregorianCalendar());
        getOp_datetime().setTime(op_datetime.getTime());
        setOp_description(op_description);
        setXinco_core_user_id(resource_id);
    }
    
    public int write2DB(WorkflowDBManager DBM) throws WorkflowException {
        try {
            if (getId() > 0) {
                Statement stmt = DBM.getStatement();
                WorkflowAuditTrail audit= new WorkflowAuditTrail();
                ResourceBundle xerb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
                audit.updateAuditTrail("workflow_log",new String [] {"id ="+getId()},
                        DBM,xerb.getString("audit.log.change"),getXinco_core_user_id());
                stmt.executeUpdate("UPDATE workflow_log SET workflow_instance_id=" +
                        getWorkflow_instance_id() + ", workflow_template_id=" +
                        getWorkflow_template_id() + ", op_code=" + getOp_code() +
                        ", op_datetime=now(), op_description='" +
                        getOp_description().replaceAll("'","\\\\'") +
                        ", xinco_core_user="+getXinco_core_user_id()+
                        "' WHERE id=" + getId());
                stmt.close();
            } else {
                setId(DBM.getNewID("xinco_core_log"));
                Statement stmt = DBM.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO workflow_log VALUES (" + getId() + ", " +
                        getXinco_core_user_id() + ", " + getWorkflow_template_id() + ", " +
                        getWorkflow_instance_id()+", "+getOp_code() + ", now(), '" +
                        getOp_description().replaceAll("'","\\\\'")+
                        "')");
                stmt.close();
            }
            DBM.getConnection().commit();
        } catch (Exception e) {
            try {
                DBM.getConnection().rollback();
            } catch (Exception erollback) {
            }
            throw new WorkflowException();
        }
        return getId();
    }
    
    //create complete log list for data
    public static Vector getWorkflowLogs(int attrID, XincoDBManager DBM) {
        Vector core_log = new Vector();
        GregorianCalendar cal = new GregorianCalendar();
        try {
            Statement stmt = DBM.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM workflow_log WHERE workflow_instance_id=" + attrID);
            while (rs.next()) {
                cal = new GregorianCalendar();
                cal.setTime( rs.getTimestamp("op_datetime"));
                core_log.addElement(new WorkflowLogServer(rs.getInt("id"),
                        rs.getInt("xinco_core_user_id"),  rs.getInt("workflow_template_id"),
                        rs.getInt("workflow_instance_id"),
                        rs.getInt("op_code"), cal, rs.getString("op_description")));
            }
            stmt.close();
        } catch (Exception e) {
            core_log.removeAllElements();
        }
        return core_log;
    }
}
