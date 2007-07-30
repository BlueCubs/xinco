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
    private boolean instanceReady=false;
    private int instance_template_id,instance_id;
    private WorkflowAuditTrail wat;
    
    /** Creates a new instance of TransactionServer */
    public TransactionServer() {}
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
    
    public void loadActivities(WorkflowDBManager DBM) throws SQLException{
        Vector values = new Vector();
        String sql="select activity_id from " +
                "Transaction_has_Activity where transaction_id="+getId();
        if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
            System.out.println(sql);
        rs=DBM.getStatement().executeQuery(sql);
        values.clear();
        while(rs.next()){
            values.addElement(new ActivityServer(rs.getInt("activity_id"),DBM));
        }
        setActivities(values);
    }
    
    public void loadProperties(WorkflowDBManager DBM) throws SQLException{
        Vector values = new Vector();
        values=new PropertyServer().getPropertiesForTransaction(getId(),DBM);
        setProperties(values);
    }
    
    public TransactionServer instanceSetup(int instance_id, WorkflowDBManager DBM){
        String sql="select * from workflow_instance_has_transaction " +
                "where transaction_id="+getId()+" and id="+instance_id;
        try {
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println(sql);
            ResultSet rs2=DBM.getStatement().executeQuery(sql);
            rs2.next();
            setCompleted(rs2.getBoolean("completed"));
            setInstance_id(instance_id);
            setInstanceReady(true);
            setTo(new NodeServer(rs2.getInt("id"),DBM));
            setFrom(new NodeServer(rs2.getInt("node_id"),DBM));
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
        wat= new WorkflowAuditTrail();
        String sql="";
        try {
            if(getId()>0){
                //Update Transaction
                sql="update transaction set id="+
                        getId()+", description='"+getDescription()+"', node_id="+getTo().getId()+" where id="+getId();
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                wat.updateAuditTrail("transaction",new String [] {"id="+getId()},DBM,"audit.general.modified",getChangerID());
                DBM.getConnection().commit();
                instanceWrite2DB(DBM);
            } else{
                setId(DBM.getNewID("transaction"));
                sql="INSERT INTO transaction (id, description,Node_id) VALUES("+
                        getId()+", '"+getDescription()+"',"+getTo().getId()+")";
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                wat.updateAuditTrail("transaction",new String [] {"id="+getId()},DBM,"audit.general.create",getChangerID());
                DBM.getConnection().commit();
                instanceWrite2DB(DBM);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DBM.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isInstanceReady() {
        return instanceReady;
    }
    
    public void setInstanceReady(boolean instanceReady) {
        this.instanceReady = instanceReady;
    }
    
    public int getInstance_template_id() {
        return instance_template_id;
    }
    
    public void setInstance_template_id(int instance_template_id) {
        this.instance_template_id = instance_template_id;
    }
    
    public int getInstance_id() {
        return instance_id;
    }
    
    public void setInstance_id(int instance_id) {
        this.instance_id = instance_id;
    }
    
    private void instanceWrite2DB(WorkflowDBManager DBM){
        if(isInstanceReady()){
            String sql="update Workflow_Instance_has_transaction set completed="+(isCompleted() ? 1:0)+
                    " where id="+ getInstance_id()+" and transaction_id="+getId()+" and Workflow_template_id="+
                    getInstance_template_id();
            try {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    System.out.println(sql);
                DBM.getConnection().createStatement().executeUpdate(sql);
                DBM.getConnection().commit();
            } catch (SQLException ex) {
                if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                    ex.printStackTrace();
            }
        }else{
            if(DBM.getWorkflowSettingServer().getSetting("general.setting.enable.developermode").isBool_value())
                System.out.println("Not instance ready...");
        }
    }
    
    public Vector loadTransactions(int instance_id,WorkflowDBManager DBM){
        Vector transactions = new Vector();
        try {
            ResultSet rs= DBM.getConnection().createStatement().executeQuery("select transactions_id from " +
                    "workflow_instance_has_transactions where id="+instance_id);
            while(rs.next()){
                transactions.add(new TransactionServer(rs.getInt("transactions_id"),DBM).instanceSetup(instance_id,DBM));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return transactions;
    }
}
