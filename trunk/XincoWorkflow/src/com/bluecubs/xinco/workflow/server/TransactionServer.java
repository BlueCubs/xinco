/**
 *Copyright June 8, 2007 blueCubs.com
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
 * Name:            TransactionServer.java
 *
 * Description:     TransactionServer
 *
 * Original Author: Javier A. Ortiz
 * Date:            June 8, 2007, 10:21 AM
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
import com.bluecubs.xinco.workflow.Transaction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TransactionServer extends Transaction{
    private ResultSet rs;
    private boolean completed=false;
    /** Creates a new instance of TransactionServer */
    public TransactionServer(int id, WorkflowDBManager DBM) {
        if(id>0){
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Creating transaction with id: "+id);
            try {
                rs=DBM.getStatement().executeQuery("select * from transaction where id="+id);
                rs.next();
                setId(rs.getInt("id"));
                setDescription(rs.getString("description"));
                setTo(new NodeServer(rs.getInt("node_id"),DBM));
                rs=DBM.getStatement().executeQuery("select node_id from " +
                        "Node_has_Transaction where transaction_id="+id);
                rs.next();
                setFrom(new NodeServer(rs.getInt("node_id"),DBM));
                loadActivities(DBM);
                loadProperties(DBM);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Done!");
        }
    }
    
    private void loadActivities(WorkflowDBManager DBM) throws SQLException{
        Vector values = new Vector();
        rs=DBM.getStatement().executeQuery("select activity_id from " +
                "Transaction_has_Activity where transaction_id="+getId());
        values.clear();
        while(rs.next()){
            values.addElement(new ActivityServer(rs.getInt("activity_id"),DBM));
        }
        setActivities(values);
    }
    
    private void loadProperties(WorkflowDBManager DBM) throws SQLException{
        Vector values = new Vector();
        values=new PropertyServer().getPropertiesForTransaction(getId(),DBM);
        setProperties(values);
    }
    
   public TransactionServer instanceSetup(int instance_id, WorkflowDBManager DBM){
        try {
            ResultSet rs2=DBM.getStatement().executeQuery("select * from workflow_instance_has_transaction " +
                           "where node_id="+getId()+" and workflow_instance_id="+instance_id);
            rs2.next();
            setCompleted(rs2.getBoolean("completed"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    public void write2DB(WorkflowDBManager DBM){
        WorkflowAuditTrail wat= new WorkflowAuditTrail();
        try {
            if(getId()>0){
                //Update Transaction
                DBM.getConnection().createStatement().executeUpdate("update transaction set id="+
                        getId()+", description='"+getDescription()+"', node_id="+getTo().getId()+" where id="+getId());
                wat.updateAuditTrail("transaction",new String [] {"id="+getId()},DBM,"audit.general.modified",getChangerID());
                DBM.getConnection().commit();
            } else{
                setId(DBM.getNewID("transaction"));
                DBM.getConnection().createStatement().executeUpdate("INSERT INTO transaction (id, description,Node_id) VALUES("+
                        getId()+", '"+getDescription()+"',"+getTo().getId()+")");
                wat.updateAuditTrail("transaction",new String [] {"id="+getId()},DBM,"audit.general.create",getChangerID());
                DBM.getConnection().commit();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
